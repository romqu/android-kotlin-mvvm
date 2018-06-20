package de.sevennerds.trackdefects.data.client

import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.data.client.net.ClientNetDataSource
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.LoginResponse
import de.sevennerds.trackdefects.data.response.RegistrationResponse
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepo @Inject constructor(private val clientNet: ClientNetDataSource) {

    fun login(loginCredentials: LoginCredentials): Single<Result<LoginResponse>> =
            clientNet.login(loginCredentials)
                    .map { t -> t.body()?.let { responseNet ->
                        if(responseNet.isSuccess){
                            Result.success(responseNet.data!!)
                        } else {
                            Result.failure(Error.NetworkError(responseNet.errors.toString()))
                        }
                    } ?: Result.failure(Error.NetworkError(R.string.network_request_failed.toString())) }
                    .onErrorReturn { t: Throwable -> Result.failure(Error.LoginFailedError(t.toString())) }

    fun register(registrationData: RegistrationData): Single<Result<RegistrationResponse>> =
            clientNet.register(registrationData)
                    .map { t -> t.body()?.let { responseNet ->
                        if(responseNet.isSuccess){
                            Result.success(responseNet.data!!)
                        } else {
                            Result.failure(Error.NetworkError(responseNet.errors.toString()))
                        }
                    } ?: Result.failure(Error.NetworkError(R.string.network_request_failed.toString())) }
                    .onErrorReturn { t: Throwable -> Result.failure(Error.RegistrationFailedError(t.toString())) }
}
