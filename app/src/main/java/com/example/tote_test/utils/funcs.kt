package com.example.tote_test.utils

import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.tote_test.R
import com.example.tote_test.models.GamblerModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

fun checkFieldBlank(
    input: String,
    layout: TextInputLayout,
    fieldName: String
): Boolean {
    var result = true

    if (input.isBlank()) {
        layout.isErrorEnabled = true
        layout.error = APP_ACTIVITY.getString(R.string.error_field_empty, fieldName)
    } else {
        layout.isErrorEnabled = false
        result = false
    }

    return result
}

fun checkMinLength(
    minValue: Int,
    input: Editable,
    layout: TextInputLayout,
    fieldName: String
): Boolean {
    var result = false

    if (input.length < minValue) {
        layout.isErrorEnabled = true
        layout.error = APP_ACTIVITY.getString(R.string.error_min_length, fieldName, minValue)
    } else {
        layout.isErrorEnabled = false
        result = true
    }

    return result
}

fun isProfileFilled(): Boolean =
    !(GAMBLER.nickname.isBlank()
            || GAMBLER.name.isBlank()
            || GAMBLER.family.isBlank()
            || GAMBLER.gender.isBlank()
            || (GAMBLER.photoUrl.isBlank() || GAMBLER.photoUrl == "empty")
            || GAMBLER.stake == 0
            )

fun Fragment.findTopNavController(): NavController {
    val topLevelHost = requireActivity().supportFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}
