package com.example.androidcourse.net

import com.example.androidcourse.auth.*
import com.example.androidcourse.contacts.User
import com.haroldadmin.cnradapter.NetworkResponse

class MockApi : Api {
    override suspend fun getUsers(): NetworkResponse<List<User>, Unit> {
        val user = User(
            "https://sun9-79.userapi.com/impg/Q-e0Ch6V4cjmoIjQknD4JFpf8ygt5EthRsgatg/N6f1AQ_EJ64.jpg?size=827x1063&quality=95&sign=77d79a9245aeb0609e462cdc41ccb4db&type=album",
            "Kirill",
            "Б09"
        )
        return NetworkResponse.Success(
            body = listOf(user, user, user, user, user, user, user, user, user, user, user, user, user),
            code = 200
        )
    }

    override suspend fun signInWithEmail(request: SignInWithEmailRequest): NetworkResponse<AuthTokens, Unit> {
        return NetworkResponse.Success(
            AuthTokens(
                accessToken = ".",
                refreshToken = ".",
                accessTokenExpiration = 2040871771000,
                refreshTokenExpiration = 2040871771000,
            ),
            code = 200
        )
    }

    override suspend fun refreshAuthTokens(request: RefreshAuthTokensRequest): NetworkResponse<AuthTokens, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfile(request: ProfileRequest): NetworkResponse<Profile, Unit> {
        return NetworkResponse.Success(
            Profile(
                imageUrl = "https://sun9-79.userapi.com/impg/Q-e0Ch6V4cjmoIjQknD4JFpf8ygt5EthRsgatg/N6f1AQ_EJ64.jpg?size=827x1063&quality=95&sign=77d79a9245aeb0609e462cdc41ccb4db&type=album",
                name = "Кирилл",
                likeCount = "2",
                postsCount = "3",
            ),
            code = 200
        )
    }

    override suspend fun signUp(request: SignUpRequest): NetworkResponse<EmailConfirmationCode, Unit> {
        return NetworkResponse.Success(
            EmailConfirmationCode("2211", "."),
            code = 200,
        )
    }

    override suspend fun approveSignUp(request: SignUpApproveRequest) {

    }
}