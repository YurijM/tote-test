package com.example.tote_test.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentSignInBinding
import com.example.tote_test.utils.*

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel

    private var isEmailFilled = false
    private var isPasswordFilled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]

        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        initFields()

        binding.signInToAuth.isEnabled = isFieldsFilled()

        binding.signInToAuth.setOnClickListener {
            login()
        }

        return binding.root
    }

    private fun initFields() {
        initFieldEmail()
        initFieldPassword()
    }

    private fun initFieldEmail() {
        binding.signInInputEmail.addTextChangedListener {
            if (it != null) {
                isEmailFilled = !checkFieldBlank(it.toString(), binding.signInLayoutInputEmail, getString(R.string.email))

                binding.signInToAuth.isEnabled = isEmailFilled && isPasswordFilled
            }
        }
    }

    private fun initFieldPassword() {
        binding.signInInputPassword.addTextChangedListener {
            if (it != null) {
                isPasswordFilled =
                    !checkFieldBlank(it.toString(), binding.signInLayoutInputPassword, getString(R.string.password))

                binding.signInToAuth.isEnabled = isFieldsFilled()
            }
        }
    }

    private fun isFieldsFilled(): Boolean = isEmailFilled && isPasswordFilled

    private fun login() {
        EMAIL = binding.signInInputEmail.text.toString().trim()
        PASSWORD = binding.signInInputPassword.text.toString().trim()

        viewModel.auth(
            {
                AppPreferences.setIsAuth(true)
                findTopNavController().navigate(R.id.action_signInFragment_to_tabsFragment)
            },
            {
                showToast(it)
            }
        )
    }
}