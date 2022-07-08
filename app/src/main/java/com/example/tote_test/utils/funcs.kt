package com.example.tote_test.utils

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.tote_test.R

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_LONG).show()
}

fun toLog(message: String) {
    Log.i(KEY_LOG, message)
}

fun fixError(error: String) {
    showToast(error)
    Log.i(KEY_LOG, error)
}

fun Fragment.findTopNavController(): NavController {
    val topLevelHost = requireActivity().supportFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}
