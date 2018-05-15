package de.sevennerds.trackdefects.data.error

import com.squareup.moshi.JsonClass

// @JsonSerializable
@JsonClass(generateAdapter = true)
data class ErrorNet(val status: String,
                    val code: String,
                    val title: String,
                    val detail: String) {
}