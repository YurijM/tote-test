package com.example.tote_test.ui.tabs.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel() : ViewModel() {
    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> = _inProgress

    private fun hideProgress() {
        _inProgress.value = false
    }

    private fun showProgress() {
        _inProgress.value = true
    }
}