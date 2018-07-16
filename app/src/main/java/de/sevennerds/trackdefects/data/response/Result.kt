package de.sevennerds.trackdefects.data.response

import io.reactivex.Single

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val error: Error) : Result<Nothing>()

    val isSuccess: Boolean
        get() = this is Success

    fun getOrThrow(): T {
        return when (this) {

            is Result.Success -> data
            is Result.Failure -> throw Error(error.toString())
        }
    }


    fun <R> onSuccessResult(invoke: (data: T) -> R): Result<R> {
        return when (this) {

            is Result.Success -> {
                val data = invoke(data)
                Result.success(data)
            }
            is Result.Failure -> this
        }
    }

    fun <R> onSuccessSingle(invoke: (data: T) -> Single<Result<R>>): Single<Result<R>> {
        return when (this) {

            is Result.Success -> {
                invoke(data)
            }
            is Result.Failure -> Single.fromCallable { failure(this.error) }
        }
    }

    fun <R> match(onSuccess: (data: T) -> R, onFailure: (error: Error) -> R): R {
        return when (this) {

            is Result.Success -> onSuccess(data)
            is Result.Failure -> onFailure(error)
        }
    }

    /*fun isSuccess() = this is Success*/

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
    abstract class FeatureError : Error()
}