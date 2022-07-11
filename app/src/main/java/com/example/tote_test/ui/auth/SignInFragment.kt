package com.example.tote_test.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentSignInBinding
import com.example.tote_test.utils.*

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        binding.signInToAuth.setOnClickListener {
            EMAIL = binding.signInInputEmail.text.toString()
            PASSWORD = binding.signInInputPassword.text.toString()

            REPOSITORY.signIn {
                AppPreferences.setIsAuth(true)
                findTopNavController().navigate(R.id.action_signInFragment_to_tabsFragment)
            }
        }

        return binding.root
    }
}