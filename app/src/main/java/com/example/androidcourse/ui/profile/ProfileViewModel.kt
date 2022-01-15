package com.example.androidcourse.ui.profile

import androidx.lifecycle.viewModelScope
import com.example.androidcourse.auth.AuthInteractor
import com.example.androidcourse.auth.Profile
import com.example.androidcourse.infra.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
) : BaseViewModel() {
    suspend fun profileFlow(): Flow<Profile?> = authInteractor.getProfile()

    fun logout() {
        viewModelScope.launch {
            authInteractor.logout()
        }
    }
}