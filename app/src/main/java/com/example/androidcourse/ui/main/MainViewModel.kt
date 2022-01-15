package com.example.androidcourse.ui.main

import com.example.androidcourse.auth.AuthInteractor
import com.example.androidcourse.infra.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {
    suspend fun isAuthorizedFlow(): Flow<Boolean> = authInteractor.isAuthorizedFlow()
}