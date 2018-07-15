package de.sevennerds.trackdefects.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton

@Module
abstract class FileModule() {

    @Module
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideRoom(context: Context): File = context.filesDir

    }
}