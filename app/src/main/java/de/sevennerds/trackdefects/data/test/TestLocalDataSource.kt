package de.sevennerds.trackdefects.data.test

import de.sevennerds.trackdefects.data.ResponseNet
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface TestLocalDataSource {

    @GET("test")
    fun test(): Single<Response<ResponseNet<String>>>
}