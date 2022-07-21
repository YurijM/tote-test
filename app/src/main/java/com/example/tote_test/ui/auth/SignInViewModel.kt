package com.example.tote_test.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.utils.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel() : ViewModel() {
    fun auth(onSuccess: () -> Unit, onFail: (String) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.signIn(
            { onSuccess() },
            { onFail(it) }
        )
    }
}