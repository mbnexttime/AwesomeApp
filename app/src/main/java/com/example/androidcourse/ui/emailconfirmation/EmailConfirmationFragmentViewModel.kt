package com.example.androidcourse.ui.emailconfirmation

import androidx.lifecycle.viewModelScope
import com.example.androidcourse.auth.AuthInteractor
import com.example.androidcourse.infra.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailConfirmationFragmentViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
) : BaseViewModel() {
    private var enteredCode = ""

    fun updateCode(code: String) {
        enteredCode = code
    }

    fun tryToApprove(): Boolean {
        val pendingData = authInteractor.getPendingData() ?: return false
        if (pendingData.emailConfirmationCode.code == enteredCode) {
            authInteractor.clearPending()
            viewModelScope.launch {
                authInteractor.approveSignUp(pendingData.emailConfirmationCode.id)
                authInteractor.signInWithEmail(
                    pendingData.email, pendingData.password
                )
            }
            return true
        }
        return false
    }


}