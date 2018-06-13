package de.sevennerds.trackdefects.data.client

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginCredentials(
        @Json(name = "email") val email: String,
        @Json(name = "password") val password: String
)