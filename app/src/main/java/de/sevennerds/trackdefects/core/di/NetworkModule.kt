package de.sevennerds.trackdefects.core.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import de.sevennerds.trackdefects.common.Constants.Database.BASE_API_URL
import de.sevennerds.trackdefects.data.client.net.ClientNetDataSource
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
abstract class NetworkModule {

    @Module
    companion object {

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
                .baseUrl(BASE_API_URL)
                .build()

        @Provides
        @Singleton
        @JvmStatic
        fun provideClientNetDataSource(retrofit: Retrofit): ClientNetDataSource =
                retrofit.create(ClientNetDataSource::class.java)
    }

}