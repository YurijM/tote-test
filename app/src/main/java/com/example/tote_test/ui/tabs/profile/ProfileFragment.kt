package com.example.tote_test.ui.tabs.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentProfileBinding
import com.example.tote_test.utils.*
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private lateinit var inputNickname: TextView
    private lateinit var inputFamily: TextView
    private lateinit var inputName: TextView
    private var gender = ""

    private var isNicknameFilled = false
    private var isFamilyFilled = false
    private var isNameFilled = false
    private var isGenderFilled = false
    private var isPhotoUriFilled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        //viewModel.getGamblerLiveData()

        observeProfile()
        observePhotoUri()
        observeInProgress()

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        initFields()

        binding.profileLoadPhoto.setOnClickListener {
            getImage()
        }

        binding.profileSave.setOnClickListener {
            saveProfile()
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let {
                    toLog("loadProfilePhoto - launcher")
                    it.path?.let { path -> loadProfilePhoto(path) }
                    viewModel.changePhotoUrl(it)
                }

                //result.data?.data?.let { viewModel.changePhotoUri(it) }
            }
        }

        return binding.root
    }

    private fun initFields() {
        initFieldStake()
        initFieldNickname()
        initFieldFamily()
        initFieldName()
        initFieldGender()
        //initFieldPhotoUri()
    }

    private fun initFieldStake() {
        binding.profileStake.addTextChangedListener {
            if (it != null) {
                val isStakeFilled = (GAMBLER.stake != 0)

                binding.profileErrorStake.visibility = if (isStakeFilled) View.GONE else View.VISIBLE
            }
        }
    }

    private fun initFieldNickname() {
        inputNickname = binding.profileInputNickname

        inputNickname.addTextChangedListener {
            if (it != null) {
                viewModel.changeNickname(it.toString())

                isNicknameFilled = !checkFieldBlank(it.toString(), binding.profileLayoutNickname, getString(R.string.nickname))

                binding.profileSave.isEnabled = isFieldsFilled()
            }
        }
    }

    private fun initFieldFamily() {
        inputFamily = binding.profileInputFamily

        inputFamily.addTextChangedListener {
            if (it != null) {
                viewModel.changeFamily(it.toString())

                isFamilyFilled = !checkFieldBlank(it.toString(), binding.profileLayoutFamily, getString(R.string.family))

                binding.profileSave.isEnabled = isFieldsFilled()
            }
        }
    }

    private fun initFieldName() {
        inputName = binding.profileInputName

        inputName.addTextChangedListener {
            if (it != null) {
                viewModel.changeName(it.toString())

                isNameFilled = !checkFieldBlank(it.toString(), binding.profileLayoutName, getString(R.string.name))

                binding.profileSave.isEnabled = isFieldsFilled()
            }
        }
    }

    private fun initFieldGender() {
        binding.profileGenderGroup.setOnCheckedChangeListener { _, checkedId ->
            gender = when (checkedId) {
                binding.profileMan.id -> resources.getString(R.string.man)
                binding.profileWoman.id -> resources.getString(R.string.woman)
                else -> ""
            }

            viewModel.changeGender(gender)

            isGenderFilled = gender.isNotBlank()

            binding.profileErrorGender.visibility = if (isGenderFilled) View.GONE else View.VISIBLE

            binding.profileSave.isEnabled = isFieldsFilled()
        }
    }

    private fun initFieldPhotoUri() {
        toLog("binding.profilePhoto.tag: ${binding.profilePhoto.tag}")
        isPhotoUriFilled = binding.profilePhoto.tag != EMPTY

        binding.profileErrorPhoto.visibility = if (isPhotoUriFilled) View.GONE else View.VISIBLE

        binding.profileSave.isEnabled = isFieldsFilled()
    }

    private fun loadProfilePhoto(photoUrl: String) {
        toLog("photoUrl: $photoUrl")
        val size = resources.getDimensionPixelSize(R.dimen.profile_size_photo)
        Picasso.get()
            .load(photoUrl)
            .resize(size, size)
            .centerCrop()
            .into(binding.profilePhoto)

        binding.profilePhoto.tag = photoUrl

        initFieldPhotoUri()
    }

    private fun isFieldsFilled(): Boolean =
        isNicknameFilled
                && isFamilyFilled
                && isNameFilled
                && isGenderFilled
                && isPhotoUriFilled

    private fun observeProfile() = viewModel.profile.observe(viewLifecycleOwner) {
        toLog("observeProfile: $it")

        binding.profileEmail.text = it.email
        binding.profileStake.text = getString(R.string.stake, it.stake)
        binding.profilePoints.text = getString(R.string.points, it.points)

        inputNickname.text = it.nickname
        inputFamily.text = it.family
        inputName.text = it.name
        binding.profileGenderGroup.check(
            when (it.gender) {
                "м" -> binding.profileMan.id
                "ж" -> binding.profileWoman.id
                else -> 0
            }
        )

        /*toLog("tag: ${binding.profilePhoto.tag}")
        if (binding.profilePhoto.tag != resources.getString(R.string.no_photo)) {
            loadProfilePhoto(it.photoUrl)
        }*/

        toLog("loadProfilePhoto - observeProfile")
        toLog("${it.photoUrl} <=> ${binding.profilePhoto.tag}")
        if (it.photoUrl.isNotBlank() && it.photoUrl != binding.profilePhoto.tag ) {
            loadProfilePhoto(it.photoUrl)
        } else {
            initFieldPhotoUri()
        }

        viewModel.hideProgress()
    }

    private fun observePhotoUri() = viewModel.photoUri.observe(viewLifecycleOwner) {
        //binding.profilePhoto.setImageURI(it)
        //binding.profilePhoto.tag = it.toString()
        toLog("loadProfilePhoto - observePhotoUri")
        loadProfilePhoto(it.toString())

        initFieldPhotoUri()
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

    private fun saveProfilePhoto() {
        val tag = binding.profilePhoto.tag.toString()
        if (tag.isNotBlank() && tag != EMPTY) {
            viewModel.saveImageToStorage() {
                showToast("Фото сохранено")
            }
        }
    }

    private fun saveProfile() {
        if (isNicknameFilled
            && isFamilyFilled
            && isNameFilled
            && isGenderFilled
        ) {
            viewModel.saveGamblerToDB {
                saveProfilePhoto()
                showToast("OK")
            }
        }
    }
}