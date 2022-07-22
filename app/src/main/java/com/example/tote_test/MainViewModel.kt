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

    init {
        getGamblerLiveData()
    }

    private fun getGamblerLiveData() = viewModelScope.launch(Dispatchers.Main) {
        REPOSITORY.getGamblerLiveData(_profile)
    }

    fun signOut() {
        REPOSITORY.signOut()
    }
}