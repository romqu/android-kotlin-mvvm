package de.sevennerds.trackdefects.data.cache

import android.graphics.Bitmap
import android.util.LruCache
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitmapCache @Inject constructor() {

    private val bitmapLruCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        bitmapLruCache = LruCache(cacheSize)
    }

    fun clearAndInsert(key: String, bitmap: Bitmap) {
        bitmapLruCache.evictAll()
        insert(key, bitmap)
    }

    fun insert(key: String, bitmap: Bitmap) {
        bitmapLruCache.put(key, bitmap)
    }

    fun get(key: String): Result<Bitmap> {
        val bitmap: Bitmap? = bitmapLruCache.get(key)

        return if (bitmap == null) {
            Result.failure(Error.FileNotFoundError("null"))
        } else {
            Result.success(bitmap)
        }
    }
}