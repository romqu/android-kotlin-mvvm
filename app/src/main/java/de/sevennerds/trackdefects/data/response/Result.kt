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
    // Network failures
    data class NetworkError(val message: String) : Error()
    data class RegistrationFailedError(val message: String) : Error()
    data class LoginFailedError(val message: String) : Error()

    // File operation failures
    data class SavingFiles(val message: String) : Error()
    data class DeletionFailed(val message: String) : Error()
    data class FileNotFoundError(val message: String) : Error()
    data class DuplicateFileError(val message: String) : Error()

    // Database operation failures
    data class DatabaseError(val message: String) : Error()


    // Feature specific failures, just extend from this class to impl.
    abstract class FeatureError: Error()
}