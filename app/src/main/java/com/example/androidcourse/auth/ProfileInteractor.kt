package com.example.androidcourse.auth

import com.example.androidcourse.net.Api
import com.haroldadmin.cnradapter.invoke
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ProfileInteractor @Inject constructor(
    private val api_: Lazy<Api>,
    private val authRepository: AuthRepository,
) {
    private val api: Api
        get() {
            return api_.get()
        }

    private val profileFlow: MutableStateFlow<Profile?> = MutableStateFlow(null)

    suspend fun refreshProfile() {
        val tokens = authRepository.getAuthTokensFlow().value ?: return
        val profile = api.getProfile(ProfileRequest(token = tokens.accessToken)).invoke() ?: return
        profileFlow.value = profile
    }

    suspend fun getProfile(): Flow<Profile?> {
        refreshProfile()
        return profileFlow
    }
}