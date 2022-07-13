package com.example.tote_test.ui.tabs.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel() : ViewModel() {
    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> = _inProgress

    private val _profile = MutableLiveData<GamblerModel>()
    val profile: LiveData<GamblerModel> = _profile

    private val _nickname = MutableLiveData<String>()
    private val _family = MutableLiveData<String>()
    private val _name = MutableLiveData<String>()
    private val _gender = MutableLiveData<String>()

    private val _photoUri = MutableLiveData<Uri>()
    val photoUri: LiveData<Uri> = _photoUri

    init {
        toLog("CURRENT_ID: $CURRENT_ID")
        getGamblerLiveData()
    }

    fun changeNickname(nickname: String) {
        _profile.value?.nickname = nickname
    }

    fun changeFamily(family: String) {
        _profile.value?.family = family
    }

    fun changeName(name: String) {
        _profile.value?.name = name
    }

    fun changeGender(gender: String) {
        _profile.value?.gender = gender
    }

    fun changePhotoUri(uri: Uri) {
        _photoUri.value = uri
    }

    private fun getGamblerLiveData() = viewModelScope.launch(Dispatchers.Main) {
        showProgress()
        REPOSITORY.getGamblerLiveData(_profile)

        /*viewModelScope.launch(Dispatchers.Main) {
            hideProgress()
        }*/
    }

    fun hideProgress() {
        _inProgress.value = false
    }

    private fun showProgress() {
        _inProgress.value = true
    }
}