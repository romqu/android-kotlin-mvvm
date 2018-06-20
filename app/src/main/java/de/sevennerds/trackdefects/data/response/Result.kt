package de.sevennerds.trackdefects.data.response

/*
sealed class Result<out T, out E : Error> {
    data class Success<out T>(val data: T) : Result<T, Nothing>()
    data class Failure<out E : Error>(val error: E) : Result<Nothing, E>()

    companion object {
        fun <T, E : Error> success(data: T) = Success(data) as Result<T, E>
        fun <T, E : Error> failure(data: E) = Failure(data) as Result<T, E>
    }
}

sealed class Error {

    class MyError(val message: String) : Error()
}*/

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val error: Error) : Result<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data) as Result<T>
        fun failure(data: Error) = Failure(data) as Result<Nothing>
    }
}

sealed class Error {

    class MyError(val message: String) : Error()
}