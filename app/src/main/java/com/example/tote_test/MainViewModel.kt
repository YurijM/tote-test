package com.example.tote_test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val _profile = MutableLiveData<GamblerModel>()
    val profile: LiveData<GamblerModel> = _profile

    private fun getGamblerLiveData() = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.getGamblerLiveData(_profile)
    }

    fun initGambler(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.initGambler {
            onSuccess()
        }
    }

    fun signOut() {
        REPOSITORY.signOut()
    }
}