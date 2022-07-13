package com.example.tote_test.ui.tabs.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentProfileBinding
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.*
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private var profile = GamblerModel()

    private var isNicknameFilled = false
    private var isFamilyFilled = false
    private var isNameFilled = false
    private var isGenderFilled = false
    private var isPhotoUriFilled = false
    private var isStakeFilled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        viewModel.getGamblerLiveData()

        observeProfile()
        observePhotoUri()
        observeInProgress()

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        binding.profileStake.addTextChangedListener {
            if (it != null) {
                isStakeFilled = (GAMBLER.stake != 0)

                if (isStakeFilled) {
                    binding.profileErrorStake.visibility = View.GONE
                } else {
                    binding.profileErrorStake.visibility = View.VISIBLE
                }
            }

            binding.profileSave.isEnabled = isFieldsFilled()
        }

        binding.profileInputNickname.addTextChangedListener {
            if (it != null) {
                isNicknameFilled = !checkFieldBlank(it.toString(), binding.profileLayoutNickname, getString(R.string.nickname))

                binding.profileSave.isEnabled = isFieldsFilled()
            }
        }

        binding.profileInputFamily.addTextChangedListener {
            if (it != null) {
                isFamilyFilled = !checkFieldBlank(it.toString(), binding.profileLayoutFamily, getString(R.string.family))

                binding.profileSave.isEnabled = isFieldsFilled()
            }
        }

        binding.profileInputName.addTextChangedListener {
            if (it != null) {
                isNameFilled = !checkFieldBlank(it.toString(), binding.profileLayoutName, getString(R.string.name))

                binding.profileSave.isEnabled = isFieldsFilled()
            }
        }

        binding.profileGenderGroup.setOnCheckedChangeListener { _, checkedId ->
            toLog("setOnCheckedChangeListener-binding.profileGenderGroup.checkedRadioButtonId: ${binding.profileGenderGroup.checkedRadioButtonId.toString()}")
            toLog("setOnCheckedChangeListener-checkedId: ${checkedId.toString()}")

            if (binding.profileGenderGroup.checkedRadioButtonId != checkedId) {
                profile.gender = when (checkedId) {
                    binding.profileMan.id -> resources.getString(R.string.man)
                    binding.profileWoman.id -> resources.getString(R.string.woman)
                    else -> ""
                }

                toLog("setOnCheckedChangeListener: $profile")
                viewModel.changeProfile(profile)
            }

            //isGenderFilled = (checkedId == binding.profileMan.id || checkedId == binding.profileWoman.id)
            isGenderFilled = profile.gender.isNotBlank()

            if (isGenderFilled) {
                binding.profileErrorGender.visibility = View.GONE
            } else {
                binding.profileErrorGender.visibility = View.VISIBLE
            }

            binding.profileSave.isEnabled = isFieldsFilled()
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val size = resources.getDimensionPixelSize(R.dimen.profile_size_photo)
                Picasso.get()
                    .load(result.data?.data)
                    .resize(size, size)
                    .centerCrop()
                    .into(binding.profilePhoto)

                result.data?.data?.let { viewModel.changePhotoUri(it) }
            }
        }

        binding.profileLoadPhoto.setOnClickListener {
            getImage()
        }

        return binding.root
    }

    private fun isFieldsFilled(): Boolean =
        isNicknameFilled
                && isFamilyFilled
                && isNameFilled
                && isGenderFilled
                && isPhotoUriFilled
                && isStakeFilled

    private fun observeProfile() = viewModel.profile.observe(viewLifecycleOwner) {
        toLog("observeProfile: $it")

        profile = it

        binding.profileEmail.text = it.email
        binding.profileStake.text = getString(R.string.stake, it.stake)
        binding.profilePoints.text = getString(R.string.points, it.points)

        binding.profileInputNickname.setText(it.nickname)
        binding.profileInputFamily.setText(it.family)
        binding.profileInputName.setText(it.name)
        binding.profileGenderGroup.check(
            when (it.gender) {
                "м" -> binding.profileMan.id
                "ж" -> binding.profileWoman.id
                else -> 0
            }
        )

        binding.profilePhoto.loadImage(it.photoUrl)

        viewModel.hideProgress()
    }

    private fun observePhotoUri() = viewModel.photoUri.observe(viewLifecycleOwner) {
        binding.profilePhoto.setImageURI(it)

        val uri = it.toString()
        isPhotoUriFilled = (uri.isNotBlank() && uri != EMPTY )

        if (isPhotoUriFilled) {
            binding.profileErrorPhoto.visibility = View.GONE
        } else {
            binding.profileErrorPhoto.visibility = View.VISIBLE
        }

        binding.profileSave.isEnabled = isFieldsFilled()
    }

    private fun observeInProgress() = viewModel.inProgress.observe(viewLifecycleOwner) {
        if (it) {
            binding.profileProgressBar.visibility = View.VISIBLE
        } else {
            binding.profileProgressBar.visibility = View.INVISIBLE
        }

        binding.profileSave.isEnabled = (!it && isFieldsFilled())
    }

    private fun getImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        launcher.launch(intent)
    }
}