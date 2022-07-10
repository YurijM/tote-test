package com.example.tote_test.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel() : ViewModel() {
    fun registration(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.signup(
            { onSuccess() },
            { showToast(it) }
        )
    }
}