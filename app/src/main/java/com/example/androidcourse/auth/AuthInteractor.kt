package com.example.androidcourse.auth

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.invoke
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.security.InvalidParameterException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileInteractor: ProfileInteractor,
) {
    private var pendingAuthData: PendingAuthData? = null

    suspend fun isAuthorizedFlow(): Flow<Boolean> = authRepository.isAuthorizedFlow()

    suspend fun signInWithEmail(
        email: String,
        password: String
    ): NetworkResponse<AuthTokens, Unit> {
        val response = authRepository.generateAuthTokensByEmail(email, password)
        when (response) {
            is NetworkResponse.Success -> {
                authRepository.saveAuthTokens(response.body)
                profileInteractor.refreshProfile()
            }
            is NetworkResponse.Error -> {
                Timber.e(response.error)
            }
            else -> throw InvalidParameterException()
        }
        return response
    }

    suspend fun logout() {
        authRepository.saveAuthTokens(null)
    }

    suspend fun getProfile(): Flow<Profile?> {
        return profileInteractor.getProfile()
    }

    suspend fun signUp(
        firstname: String,
        lastname: String,
        nickname: String,
        email: String,
        password: String
    ) {
        val code = authRepository.signUp(
            firstname, lastname, nickname, email, password
        ).invoke()
        if (code != null) {
            pendingAuthData = PendingAuthData(
                code, email, password
            )
        }
    }

    fun getPendingData(): PendingAuthData? {
        return pendingAuthData
    }

    fun clearPending() {
        pendingAuthData = null
    }

    suspend fun approveSignUp(id: String) {
        authRepository.approveSignUp(id)
    }
}