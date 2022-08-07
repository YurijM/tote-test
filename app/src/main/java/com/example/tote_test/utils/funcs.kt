package com.example.tote_test.utils

import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.tote_test.R
import com.example.tote_test.models.GamblerModel
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso

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

fun isProfileFilled(profile: GamblerModel): Boolean =
    !(profile.nickname.isBlank()
            || profile.name.isBlank()
            || profile.family.isBlank()
            || profile.gender.isBlank()
            || (profile.photoUrl.isBlank() || profile.photoUrl == "empty")
            || profile.stake == 0
            )

fun ImageView.loadImage(url: String) {
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.user)
        .into(this)
}

fun ImageView.loadImage(url: String, width: Int, height: Int) {
    Picasso.get()
        .load(url)
        .resize(width, height)
        .centerCrop()
        .placeholder(R.drawable.user)
        .into(this)
}

fun loadAppBarPhoto() {
    toLog("loadAppBarPhoto")
    val gamblerPhoto = APP_ACTIVITY.findViewById<ImageView>(R.id.gamblerPhoto)
    val size = APP_ACTIVITY.resources
        .getDimensionPixelSize(com.google.android.material.R.dimen.action_bar_size) * 3

    gamblerPhoto.loadImage(GAMBLER.photoUrl, size, size)
    gamblerPhoto.visibility = View.VISIBLE
}

fun Fragment.findTopNavController(): NavController {
    val topLevelHost = requireActivity().supportFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}
