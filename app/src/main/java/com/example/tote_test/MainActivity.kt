package com.example.tote_test

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.tote_test.databinding.ActivityMainBinding
import com.example.tote_test.firebase.FirebaseRepository
import com.example.tote_test.ui.tabs.TabsFragment
import com.example.tote_test.utils.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private lateinit var viewModel: MainViewModel

    private var topLevelDestinations = setOf(
        getMainDestination(),
        getTabsDestination(),
        getProfileDestination()
    )

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment) return
            onNavControllerActivated(f.findNavController())
        }
    }

    private val destinationListener = NavController.OnDestinationChangedListener { _, destination, _ ->
        supportActionBar?.title = destination.label
        supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        REPOSITORY = FirebaseRepository()

        initFirebase()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        AppPreferences.getPreferences(this)

        APP_ACTIVITY = this

        setSupportActionBar(binding.toolbar)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        delegate.applyDayNight()

        // Если в шаблоне (xml) используется тэг <fragment>
        //navController = findNavController(R.id.mainContainer)

        // Если в шаблоне (xml) используется view <...FragmentContainerView>
        //navController = (supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment).navController
        //navController.addOnDestinationChangedListener(destinationListener)
        val navController = getRootNavController()
        prepareRootNavController(navController)
        onNavControllerActivated(navController)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

        setCopyright()

        // Write a message to the database
        /*val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu.findItem(R.id.menu_item_admin).isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_exit -> {
                viewModel.signOut()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean = (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    override fun onBackPressed() {
        if (isStartDestination(navController?.currentDestination)) {
            super.onBackPressed()
        } else {
            navController?.popBackStack()
        }
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
    }

    private fun prepareRootNavController(navController: NavController) {
        val graph = navController.navInflater.inflate(getMainNavigationGraphId())

        /* graph.setStartDestination(
             if (isSignedIn) {
                 getTabsDestination()
             } else {
                 getSignInDestination()
             }
         )
         navController.graph = graph*/

        if (AppPreferences.getIsAuth()) {
            graph.setStartDestination(
                if (isProfileFilled()) {
                    getTabsDestination()
                } else {
                    getProfileDestination()
                }
            )
        }

        if (graph.startDestinationId != this.navController?.graph?.startDestinationId) {
            navController.graph = graph
        }
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController?.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
        this.navController = navController
    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        return navHost.navController
    }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false

        val graph = destination.parent ?: return false

        val startDestinations = topLevelDestinations + graph.startDestinationId

        return startDestinations.contains(destination.id)
    }

    /*private fun setStartFragment() {
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
    }*/

    private fun getMainNavigationGraphId(): Int = R.navigation.main_graph

    private fun getMainDestination(): Int = R.id.startFragment
    private fun getTabsDestination(): Int = R.id.tabsFragment
    private fun getProfileDestination(): Int = R.id.profileFragment

    private fun initFirebase() {
        REPOSITORY = FirebaseRepository()
        REPOSITORY.initDB()
    }

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
}
