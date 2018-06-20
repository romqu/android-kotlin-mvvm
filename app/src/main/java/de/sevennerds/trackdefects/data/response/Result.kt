package de.sevennerds.trackdefects.data.response

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
    class NetworkError(val message: String) : Error()
    class RegistrationFailedError(val message: String) : Error()
    class LoginFailedError(val message: String) : Error()

    class FileNotFoundError(val message: String) : Error()
    class DuplicateFileError(val message: String) : Error()
}