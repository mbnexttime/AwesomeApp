package com.example.androidcourse.net

import android.service.autofill.UserData
import com.example.androidcourse.auth.*
import com.example.androidcourse.contacts.User
import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("users")
    suspend fun getUsers(): NetworkResponse<List<User>, Unit>

     @POST("auth/sign-in-email")
    suspend fun signInWithEmail(
            @Body request: SignInWithEmailRequest
    ): NetworkResponse<AuthTokens, Unit>

    @POST("auth/refresh-tokens")
    suspend fun refreshAuthTokens(
            @Body request: RefreshAuthTokensRequest
    ): NetworkResponse<AuthTokens, Unit>

    @GET("users/profile")
    suspend fun getProfile(
        @Body request: ProfileRequest
    ): NetworkResponse<Profile, Unit>

    @GET("auth/sign-up")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): NetworkResponse<EmailConfirmationCode, Unit>

    @POST("auth/approve")
    suspend fun approveSignUp(
        @Body request: SignUpApproveRequest
    )
}

@JsonClass(generateAdapter = true)
data class GetUsersResponse(
    @Json(name = "data") val data: List<User>
)

