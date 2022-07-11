package com.example.tote_test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.REPOSITORY

class MainViewModel() : ViewModel() {
    private val _currentGambler = MutableLiveData<GamblerModel>()
    val currentGambler: LiveData<GamblerModel> = _currentGambler

    fun getGamblerLiveData() {
        REPOSITORY.getGamblerLiveData(_currentGambler)
    }

    fun signOut() {
        REPOSITORY.signOut()
    }
}