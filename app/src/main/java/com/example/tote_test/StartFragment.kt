package com.example.tote_test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tote_test.databinding.FragmentStartBinding
import com.example.tote_test.utils.findTopNavController
import com.example.tote_test.utils.toLog

class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentStartBinding.inflate(layoutInflater, container, false)

        binding.startSignIn.setOnClickListener {
            findTopNavController().navigate(R.id.action_startFragment_to_signInFragment)
        }

        binding.startSignUp.setOnClickListener {
            findTopNavController().navigate(R.id.action_startFragment_to_signUpFragment)
        }

        return binding.root
    }
}