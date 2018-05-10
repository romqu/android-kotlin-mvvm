package de.sevennerds.trackdefects.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class ContextModule {

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        fun provideContext(app: Application): Context = app.applicationContext
    }
}