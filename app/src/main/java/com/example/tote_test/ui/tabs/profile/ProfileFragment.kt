package com.example.tote_test.ui.tabs.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentProfileBinding
import com.example.tote_test.ui.auth.SignUpViewModel
import com.example.tote_test.utils.GAMBLER
import com.example.tote_test.utils.toLog

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

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

        return binding.root
    }

    private fun observeProfile() = viewModel.profile.observe(viewLifecycleOwner) {
        toLog("ProfileFragment-observeProfile: $it")
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
}