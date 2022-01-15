package com.example.androidcourse.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignUpRequest(
    @Json(name="firstname") val firstname: String,
    @Json(name="lastname") val lastname: String,
    @Json(name="nickname") val nickname: String,
    @Json(name="email") val email: String,
    @Json(name="password") val password: String,
)