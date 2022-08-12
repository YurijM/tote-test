package com.example.tote_test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.toLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val _gambler = MutableLiveData<GamblerModel>()
    val gambler: LiveData<GamblerModel> = _gambler

    init {
        toLog("init")
        getGamblerLiveData()
    }

    private fun getGamblerLiveData() = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.getGamblerLiveData(_gambler)
    }

    /*fun getGambler(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.getGambler {
            onSuccess()
        }
    }*/

    fun changeGambler(gambler: GamblerModel) {
        _gambler.value = gambler
    }

    fun signOut() {
        REPOSITORY.signOut()
    }
}