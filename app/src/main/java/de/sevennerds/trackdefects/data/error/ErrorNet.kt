package de.sevennerds.trackdefects.data.error

data class ErrorNet(
        val status: String,
        val code: String,
        val title: String,
        val detail: String
)