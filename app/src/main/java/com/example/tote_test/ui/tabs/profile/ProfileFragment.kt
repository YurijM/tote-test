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
import com.example.tote_test.utils.EMPTY
import com.example.tote_test.utils.checkFieldBlank
import com.example.tote_test.utils.toLog
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var launcher: ActivityResultLauncher<Intent>

    var photoUri = EMPTY

    var isNicknameFilled = false
    var isFamilyFilled = false
    var isNameFilled = false
    var isGenderFilled = false
    var isPhotoUrlFilled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        viewModel.getGamblerLiveData()

        observeProfile()
        observeInProgress()

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

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
            isGenderFilled = (checkedId == binding.profileMan.id || checkedId == binding.profileWoman.id)

            binding.profileSave.isEnabled = isFieldsFilled()
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                photoUri = result.data?.data.toString()
                val size = resources.getDimensionPixelSize(R.dimen.profile_size_photo)
                Picasso.get()
                    .load(result.data?.data)
                    .resize(size, size)
                    .centerCrop()
                    .into(binding.profilePhoto)
                //binding.profilePhoto.setImageURI(result.data?.data)

                toLog("photoUri: $photoUri")
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
                //&& (binding.profilePhoto)

    private fun observeProfile() = viewModel.profile.observe(viewLifecycleOwner) {
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

        //binding.profilePhoto.loadImage(it.photoUrl)

        viewModel.hideProgress()
    }

    private fun observeInProgress() = viewModel.inProgress.observe(viewLifecycleOwner) {
        if (it) {
            binding.profileProgressBar.visibility = View.VISIBLE
            binding.profileSave.isEnabled = false
        } else {
            binding.profileProgressBar.visibility = View.INVISIBLE
            binding.profileSave.isEnabled = true
        }
    }

    private fun getImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        launcher.launch(intent)
    }

}