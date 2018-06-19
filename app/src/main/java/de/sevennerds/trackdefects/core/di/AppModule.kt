package de.sevennerds.trackdefects.di

import android.app.Application
import dagger.Module
import dagger.Provides
import de.sevennerds.trackdefects.TrackDefectsApp
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {
    @Provides
    @Singleton
    fun provideApp() = application
}