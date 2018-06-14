package de.sevennerds.trackdefects.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.sevennerds.trackdefects.data.error.ErrorNet

@JsonClass(generateAdapter = true)
data class ResponseNet<T>(
        @Json(name = "is_success") val isSuccess: Boolean,
        @Json(name = "data") val data: T?,
        @Json(name = "errors") val errors: List<ErrorNet>
)