package com.example.androidcourse.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileRequest(
    @Json(name = "access_token") val token: String,
)