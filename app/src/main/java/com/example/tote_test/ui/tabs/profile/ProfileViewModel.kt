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

    private val _photoUri = MutableLiveData<Uri>()
    val photoUri: LiveData<Uri> = _photoUri

    init {
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

    fun saveGamblerToDB(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.Main) {
        val profile: GamblerModel = _profile.value as GamblerModel

        val dataMap = mutableMapOf<String, Any>()

        dataMap[GAMBLER_NICKNAME] = profile.nickname.trim()
        dataMap[GAMBLER_FAMILY] = profile.family.trim()
        dataMap[GAMBLER_NAME] = profile.name.trim()
        dataMap[GAMBLER_GENDER] = profile.gender

        REPOSITORY.saveGamblerToDB(dataMap) {
            onSuccess()
        }
    }

    fun saveImageToStorage(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.Main) {
        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_PHOTO).child(CURRENT_ID)

        _photoUri.value?.let { it ->
            REPOSITORY.saveImageToStorage(it, path) {
                REPOSITORY.getUrlFromStorage(path) {url ->
                    REPOSITORY.savePhotoUrlToDB(url) {
                        onSuccess()
                    }
                }

            }
        }
    }

    fun hideProgress() {
        _inProgress.value = false
    }

    private fun showProgress() {
        _inProgress.value = true
    }
}
