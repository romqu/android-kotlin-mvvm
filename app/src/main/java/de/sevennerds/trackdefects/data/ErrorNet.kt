package de.sevennerds.trackdefects.data

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class ErrorNet(val status: String,
                    val code: String,
                    val title: String,
                    val detail: String) {
}