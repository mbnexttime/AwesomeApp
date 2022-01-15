package com.example.androidcourse.auth

data class PendingAuthData(
    val emailConfirmationCode: EmailConfirmationCode,
    val email: String,
    val password: String,
)