package com.example.tote_test.ui.tabs.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.databinding.FragmentProfileBinding
import com.example.tote_test.ui.auth.SignUpViewModel
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

        observeInProgress()

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        return binding.root
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