package com.example.a2022.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2022.R
import com.example.a2022.databinding.FragmentSignInBinding
import com.example.a2022.utils.APP_ACTIVITY
import com.example.a2022.utils.findTopNavController
import com.example.a2022.utils.toLog

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        binding.signInToAuth.setOnClickListener {
            findTopNavController().navigate(R.id.action_signInFragment_to_tabsFragment)
        }

        return binding.root
    }
}