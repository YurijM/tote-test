package com.example.a2022

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.a2022.databinding.ActivityMainBinding
import com.example.a2022.utils.APP_ACTIVITY
import com.example.a2022.utils.toLog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    private var topLevelDestinations = setOf(
        getMainDestination(),
        getGamblersDestination()
    )

    private val destinationListener = NavController.OnDestinationChangedListener { _, destination, _ ->
        toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        //supportActionBar?.title = prepareTitle(destination.label, arguments)

        supportActionBar?.title = destination.label
        supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

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

        prepareRootNavController(isSignedIn())
        onNavControllerActivated()
    }

    private fun isSignedIn(): Boolean = true
    private fun isProfileFilled(): Boolean = false

    private fun prepareRootNavController(isSignedIn: Boolean) {
        toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        val graph = navController.navInflater.inflate(getMainNavigationGraphId())

        if (isSignedIn) {
            graph.setStartDestination(
                if (isProfileFilled()) {
                    getGamblersDestination()
                } else {
                    getProfileDestination()
                }
            )
        }

        if (graph.startDestinationId != navController.graph.startDestinationId) {
            navController.graph = graph
        }
    }

    private fun getMainNavigationGraphId(): Int = R.navigation.main_graph

    private fun getMainDestination(): Int = R.id.startFragment
    private fun getGamblersDestination(): Int = R.id.gamblersFragment
    private fun getProfileDestination(): Int = R.id.profileFragment

    private fun isStartDestination(destination: NavDestination?): Boolean {
        toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        if (destination == null) return false

        destination.parent ?: return false

        if (destination.id == getProfileDestination()) {
            if (!isProfileFilled() && !topLevelDestinations.contains(destination.id)) {
                topLevelDestinations = topLevelDestinations + destination.id
            }
        }

        return topLevelDestinations.contains(destination.id)
    }

    private fun onNavControllerActivated() {
        navController.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
    }

    override fun onSupportNavigateUp(): Boolean = (navController.navigateUp() || super.onSupportNavigateUp())

    override fun onBackPressed() {
        if (isStartDestination(navController.currentDestination)) {
            super.onBackPressed()
        } else {
            navController.popBackStack()
        }
    }
}
