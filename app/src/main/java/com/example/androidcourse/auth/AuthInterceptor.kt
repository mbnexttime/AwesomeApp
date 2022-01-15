package com.example.androidcourse.auth

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Provider

class AuthInterceptor(
    private val authRepository: Provider<AuthRepository>,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain
                .request()
                .newBuilder()
                .apply {
                    runBlocking {
                        authRepository.get().getAuthTokensFlow().first()?.let {
                            addHeader("Authorization", "Bearer ${it.accessToken}")
                        }
                    }
                }
                .build()
        )
}