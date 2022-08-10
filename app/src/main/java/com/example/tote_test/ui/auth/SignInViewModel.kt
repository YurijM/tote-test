package com.example.tote_test.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tote_test.utils.CURRENT_ID
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.toLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    /*private val _gambler = MutableLiveData<GamblerModel>()
    val gambler: LiveData<GamblerModel> = _gambler*/

    private fun initGambler(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        REPOSITORY.getGambler {
            onSuccess()
        }
    }

    fun auth(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        toLog("auth -> CURRENT_ID before: $CURRENT_ID")
        REPOSITORY.signIn {
            toLog("auth -> CURRENT_ID after: $CURRENT_ID")
            if (CURRENT_ID.isNotBlank()) {
                initGambler {
                    onSuccess()
                }
            }
        }
    }
}
