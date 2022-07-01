package com.example.a2022

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.a2022.databinding.ActivityMainBinding
import com.example.a2022.utils.APP_ACTIVITY
import com.example.a2022.utils.YEAR_START
import com.example.a2022.utils.toLog
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    private var topLevelDestinations = setOf(
        getMainDestination(),
        getRatingDestination()
    )

    private val destinationListener = NavController.OnDestinationChangedListener { _, destination, _ ->
        toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        //supportActionBar?.title = prepareTitle(destination.label, arguments)

        supportActionBar?.title = destination.label
        supportActionBar?.setDisplayHomeAsUpEnabled(!isTopLevelDestination(destination))
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
        navController.addOnDestinationChangedListener(destinationListener)

        setCopyright()

        setStartFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        if (true)
        if (menu != null) {
            menu.findItem(R.id.menu_item_admin).isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_exit -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isSignedIn(): Boolean = false
    private fun isProfileFilled(): Boolean = false

    private fun setStartFragment() {
        toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        val graph = navController.navInflater.inflate(getMainNavigationGraphId())

        if (isSignedIn()) {
            graph.setStartDestination(
                if (isProfileFilled()) {
                    getRatingDestination()
                } else {
                    getProfileDestination()
                }
            )
        }

        if (graph.startDestinationId != navController.graph.startDestinationId) {
            navController.graph = graph
        }
    }

    private fun isTopLevelDestination(destination: NavDestination?): Boolean {
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

    private fun getMainNavigationGraphId(): Int = R.navigation.main_graph

    private fun getMainDestination(): Int = R.id.startFragment
    private fun getRatingDestination(): Int = R.id.ratingFragment
    private fun getProfileDestination(): Int = R.id.profileFragment

    private fun setCopyright() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val copyright = binding.mainFooter.copyrightYear

        if (year != YEAR_START) {
            val strYear = "$YEAR_START-$year"
            copyright.text = strYear
        } else {
            copyright.text = YEAR_START.toString()
        }
    }


    override fun onSupportNavigateUp(): Boolean = (navController.navigateUp() || super.onSupportNavigateUp())

    override fun onBackPressed() {
        if (isTopLevelDestination(navController.currentDestination)) {
            super.onBackPressed()
        } else {
            navController.popBackStack()
        }
    }
}
