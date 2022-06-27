package com.example.a2022.ui.gamblers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2022.databinding.FragmentGamblersBinding
import com.example.a2022.utils.toLog

class GamblersFragment : Fragment() {
    private lateinit var binding: FragmentGamblersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentGamblersBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}