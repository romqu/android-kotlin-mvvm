package de.sevennerds.trackdefects.data

sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Failure(val error: String) : Response<Nothing>()
}