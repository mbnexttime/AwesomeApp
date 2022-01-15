package com.example.androidcourse.auth

import com.example.androidcourse.di.AppCoroutineScope
import com.example.androidcourse.di.IoCoroutineDispatcher
import com.example.androidcourse.net.Api
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api_: Lazy<Api>,
    private val db_: Lazy<db>,
    @IoCoroutineDispatcher private val ioDispatcher: CoroutineDispatcher,
    @AppCoroutineScope externalCoroutineScope: CoroutineScope,
) {
    private val api: Api
        get() {
            return api_.get()
        }

    private val db: db
        get() {
            return db_.get()
        }

    private val authTokensFlow: Deferred<MutableStateFlow<AuthTokens?>> =
        externalCoroutineScope.async(context = ioDispatcher, start = CoroutineStart.LAZY) {
            Timber.d("Initializing auth tokens flow.")
            MutableStateFlow(
                db.authTokens
            )
        }

    suspend fun generateRefreshedAuthTokens(refreshToken: String): NetworkResponse<AuthTokens, Unit> {
        return api.refreshAuthTokens(RefreshAuthTokensRequest(refreshToken))
    }

    suspend fun getAuthTokensFlow(): StateFlow<AuthTokens?> {
        return authTokensFlow.await().asStateFlow()
    }

    /**
     * @param authTokens active auth tokens which must be used for signing all requests
     */
    suspend fun saveAuthTokens(authTokens: AuthTokens?) {
        withContext(ioDispatcher) {
            Timber.d("Persist auth tokens $authTokens.")
            db.authTokens = authTokens
        }
        Timber.d("Emit auth tokens $authTokens.")
        authTokensFlow.await().emit(authTokens)
    }

    /**
     * @return whether active access tokens are authorized or not
     */
    suspend fun isAuthorizedFlow(): Flow<Boolean> {
        return authTokensFlow
            .await()
            .asStateFlow()
            .map { it != null }
    }

    suspend fun generateAuthTokensByEmail(
        email: String,
        password: String
    ): NetworkResponse<AuthTokens, Unit> {
        return api.signInWithEmail(SignInWithEmailRequest(email, password))
    }

    suspend fun signUp(
        firstname: String,
        lastname: String,
        nickname: String,
        email: String,
        password: String
    ): NetworkResponse<EmailConfirmationCode, Unit> {
        return api.signUp(
            SignUpRequest(
                firstname, lastname, nickname, email, password
            )
        )
    }

    suspend fun approveSignUp(
        id: String
    ) {
        api.approveSignUp(SignUpApproveRequest(id))
    }
}