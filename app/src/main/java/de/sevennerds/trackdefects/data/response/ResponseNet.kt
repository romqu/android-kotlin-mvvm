package de.sevennerds.trackdefects.data.response

import com.squareup.moshi.Json
import de.sevennerds.trackdefects.data.error.ErrorNet
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class ResponseNet<T>(@Json(name = "is_success") val isSuccess: Boolean,
                          val data: T,
                          val errors: List<ErrorNet>) {
}