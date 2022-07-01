package com.example.a2022.ui.tabs.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2022.databinding.FragmentProfileBinding
import com.example.a2022.utils.toLog

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}