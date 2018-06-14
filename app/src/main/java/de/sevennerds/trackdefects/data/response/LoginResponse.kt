package de.sevennerds.trackdefects.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
        @Json(name = "session_token") val sessionToken: String,
        @Json(name = "client_id") val clientId: Long,
        @Json(name = "login_id") val loginId: Long,
        @Json(name = "forename") val forename: String,
        @Json(name = "surname") val surname: String
)