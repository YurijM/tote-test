package com.example.a2022.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2022.databinding.FragmentSignUpBinding
import com.example.a2022.utils.APP_ACTIVITY
import com.example.a2022.utils.toLog

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        /*APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY.supportActionBar?.setDisplayShowHomeEnabled(true)*/

        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}