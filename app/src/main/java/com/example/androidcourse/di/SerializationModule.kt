package com.example.androidcourse.di

import com.example.androidcourse.auth.AuthTokens
import com.example.androidcourse.auth.AuthTokensAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SerializationModule {
    @Provides
    @Singleton
    fun provideMoshi(
        authTokensAdapter: AuthTokensAdapter,
    ): Moshi =
        Moshi.Builder()
            .add(AuthTokens::class.java, authTokensAdapter.nullSafe())
            .build()
}