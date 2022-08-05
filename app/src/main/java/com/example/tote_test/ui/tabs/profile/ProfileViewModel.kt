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

class ProfileViewModel : ViewModel() {
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

    fun changePhotoUrl(uri: Uri) {
        _photoUri.value = uri
    }

    fun checkProfileFilled(): Boolean {
        return if (_profile.value != null) {
            isProfileFilled(_profile.value!!)
        } else {
            false
        }
    }

    private fun getGamblerLiveData() = viewModelScope.launch(Dispatchers.IO) {
        //showProgress()
        REPOSITORY.getGamblerLiveData(_profile)

        /*viewModelScope.launch(Dispatchers.IO) {
            hideProgress()
        }*/
    }

    fun saveGamblerToDB(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        //showProgress()

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

    fun saveImageToStorage(onSuccess: (url: String) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        //showProgress()

        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_PHOTO).child(CURRENT_ID)

        _photoUri.value?.let { it ->
            REPOSITORY.saveImageToStorage(it, path) {
                REPOSITORY.getUrlFromStorage(path) { url ->
                    viewModelScope.launch(Dispatchers.IO) {
                        REPOSITORY.savePhotoUrlToDB(url) {
                            onSuccess(url)
                        }
                    }
                }
            }
        }
    }

    fun hideProgress() {
        _inProgress.value = false
    }

    fun showProgress() {
        _inProgress.value = true
    }
}
