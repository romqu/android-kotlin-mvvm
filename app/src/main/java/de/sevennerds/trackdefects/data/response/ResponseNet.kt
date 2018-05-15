package de.sevennerds.trackdefects.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.sevennerds.trackdefects.data.error.ErrorNet

// @JsonSerializable
@JsonClass(generateAdapter = true)
data class ResponseNet<T>(@Json(name = "is_success") val isSuccess: Boolean,
                          val data: T,
                          val errors: List<ErrorNet>) {
}