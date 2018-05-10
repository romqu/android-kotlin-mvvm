package de.sevennerds.trackdefects.data

import com.squareup.moshi.JsonAdapter
import se.ansman.kotshi.KotshiJsonAdapterFactory

@KotshiJsonAdapterFactory
abstract class AppJsonAdapterFactory : JsonAdapter.Factory {
    companion object {
        val INSTANCE: AppJsonAdapterFactory = KotshiAppJsonAdapterFactory()
    }
}