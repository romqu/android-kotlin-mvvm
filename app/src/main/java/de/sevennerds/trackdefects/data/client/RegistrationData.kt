package de.sevennerds.trackdefects.data.client

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistrationData(
        @Json(name = "login_credentials") val loginCredentials: LoginCredentials,
        @Json(name = "client") val client: Client
) {
}