package de.sevennerds.trackdefects.core.di

import android.graphics.Bitmap
import androidx.collection.LruCache
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class LruCacheModule {

    @Module
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideBitmapLruCache(): LruCache<String, Bitmap> {
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

            // Use 1/8th of the available memory for this memory cache.
            val cacheSize = maxMemory / 8;

            return LruCache(cacheSize)
        }

    }

}