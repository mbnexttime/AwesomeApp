package com.example.androidcourse.auth

import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Provider

class InternalAuthentificator(
    private val authRepository: Provider<AuthRepository>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (1 < responseCount(response)) {
            return null
        }
        val newAccessToken = runBlocking {
            try {
                val authTokens =
                    authRepository.get().getAuthTokensFlow().first()
                        ?: return@runBlocking ""
                val refreshToken =
                    authTokens.refreshToken
                try {
                    when (val resp = authRepository.get().generateRefreshedAuthTokens(refreshToken)) {
                        is NetworkResponse.Success -> {
                            authRepository.get().saveAuthTokens(
                                authTokens.copy(
                                    accessToken = resp.body.accessToken,
                                    accessTokenExpiration = resp.body.accessTokenExpiration
                                )
                            )
                            return@runBlocking resp.body.accessToken
                        }
                        is NetworkResponse.Error -> {
                            Timber.e(resp.error)
                            authRepository.get().saveAuthTokens(null)
                            return@runBlocking ""
                        }
                        else -> {
                            Timber.e("Unknown error while refreshing tokens.")
                            authRepository.get().saveAuthTokens(null)
                            return@runBlocking ""
                        }
                    }
                } catch (error: Throwable) {
                    Timber.e(error)
                    authRepository.get().saveAuthTokens(null)
                    return@runBlocking ""
                }
            } catch (error: Throwable) {
                Timber.e(error)
                return@runBlocking ""
            }
        }
        return response
            .request
            .newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var resp = response
        var result = 1
        while (resp.priorResponse?.also { resp = it } != null) {
            result++
        }
        return result
    }
}