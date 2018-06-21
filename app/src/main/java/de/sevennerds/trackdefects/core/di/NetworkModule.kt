package de.sevennerds.trackdefects.core.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import de.sevennerds.trackdefects.data.client.local.ClientLocalDataSource
import de.sevennerds.trackdefects.data.client.net.ClientNetDataSource
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
abstract class NetworkModule {

    @Module
    companion object {

        private const val BASE_URL = "http://10.0.2.2:3000/"

        @Provides
        @Singleton
        @JvmStatic
        fun provideMoshi(): Moshi = Moshi
                .Builder()
                .build()

        @Provides
        @Singleton
        @JvmStatic
        fun provideRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                        MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .build()

        @Provides
        @Singleton
        @JvmStatic
        fun provideClientNetDataSource(retrofit: Retrofit): ClientNetDataSource =
                retrofit.create(ClientNetDataSource::class.java)
    }

}