package com.example.androidcourse.di

import com.example.androidcourse.BuildConfig
import com.example.androidcourse.auth.AuthInterceptor
import com.example.androidcourse.auth.AuthRepository
import com.example.androidcourse.auth.InternalAuthentificator
import com.example.androidcourse.net.Api
import com.example.androidcourse.net.MockApi
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.internal.userAgent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetModule {

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient, moshi: Moshi): Api =
        if (BuildConfig.USE_MOCK_BACKEND_API) {
            MockApi()
        } else {
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.BACKEND_API_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(NetworkResponseAdapterFactory())
                .build()
                .create(Api::class.java)
        }

    @Provides
    @Singleton
    fun provideOkhttpClient(authRepository: Provider<AuthRepository>): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                readTimeout(60, TimeUnit.SECONDS)
                connectTimeout(60, TimeUnit.SECONDS)
                addNetworkInterceptor(AuthInterceptor(authRepository))
                authenticator(InternalAuthentificator(authRepository))
            }
            .build()
}