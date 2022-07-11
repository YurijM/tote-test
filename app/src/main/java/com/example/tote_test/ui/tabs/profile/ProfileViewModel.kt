package com.example.tote_test.ui.tabs.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.GAMBLER
import com.example.tote_test.utils.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel() : ViewModel() {
    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> = _inProgress

    private val _profile = MutableLiveData<GamblerModel>()
    val profile: LiveData<GamblerModel> = _profile

    fun getGamblerLiveData() = viewModelScope.launch(Dispatchers.Main) {
        showProgress()
        REPOSITORY.getGamblerLiveData(_profile)

        /*viewModelScope.launch(Dispatchers.Main) {
            hideProgress()
        }*/
    }

    fun hideProgress() {
        _inProgress.value = false
    }

    fun showProgress() {
        _inProgress.value = true
    }
}