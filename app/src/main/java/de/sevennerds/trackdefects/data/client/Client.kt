package de.sevennerds.trackdefects.data.client

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Client(
        val forename: String,
        val surname: String
)