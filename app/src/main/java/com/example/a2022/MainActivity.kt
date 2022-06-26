package com.example.a2022

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.a2022.databinding.ActivityMainBinding
import com.example.a2022.utils.APP_ACTIVITY
import com.example.a2022.utils.toLog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toLog("${javaClass.simpleName} - ${object{}.javaClass.enclosingMethod.name}")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        APP_ACTIVITY = this

        setSupportActionBar(binding.toolbar)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        delegate.applyDayNight()

        // Если в шаблоне (xml) используется тэг <fragment>
        //navController = findNavController(R.id.mainContainer)

        // Если в шаблоне (xml) используется view <...FragmentContainerView>
        navController = (supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment).navController
    }
}