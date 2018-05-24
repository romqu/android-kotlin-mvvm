package de.sevennerds.trackdefects.data.client.net

import de.sevennerds.trackdefects.data.client.LoginCredentials
import de.sevennerds.trackdefects.data.client.RegistrationData
import de.sevennerds.trackdefects.data.response.LoginResponse
import de.sevennerds.trackdefects.data.response.RegistrationResponse
import de.sevennerds.trackdefects.data.response.ResponseNet
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ClientNetDataSource {

    @POST("login")
    fun login(@Body loginCredentials: LoginCredentials): Single<Response<ResponseNet<LoginResponse>>>

    @POST("registration")
    fun register(@Body registrationData: RegistrationData):
            Single<Response<ResponseNet<RegistrationResponse>>>
}