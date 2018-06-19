package de.sevennerds.trackdefects.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class ContextModule {

    @Binds abstract fun context(app: Application): Context

    /*@Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        fun provideContext(app: Application): Context = app.applicationContext
    }*/
}