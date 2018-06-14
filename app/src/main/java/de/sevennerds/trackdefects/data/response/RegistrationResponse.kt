package de.sevennerds.trackdefects.data.response

import com.squareup.moshi.Json


data class RegistrationResponse(
        @Json(name = "session_token") val sessionToken: String,
        @Json(name = "client_id") val clientId: Long,
        @Json(name = "login_id") val loginId: Long
)