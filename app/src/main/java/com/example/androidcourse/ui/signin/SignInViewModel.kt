package com.example.androidcourse.ui.signin

import androidx.lifecycle.viewModelScope
import com.example.androidcourse.auth.AuthInteractor
import com.example.androidcourse.infra.BaseViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
) : BaseViewModel() {
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
           authInteractor.signInWithEmail(email, password)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authInteractor.logout()
        }

    }
}