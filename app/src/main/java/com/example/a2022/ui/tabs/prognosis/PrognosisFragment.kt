package com.example.a2022.ui.tabs.prognosis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2022.databinding.FragmentPrognosisBinding
import com.example.a2022.utils.toLog

class PrognosisFragment : Fragment() {
    private lateinit var binding: FragmentPrognosisBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod?.name}")

        binding = FragmentPrognosisBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}