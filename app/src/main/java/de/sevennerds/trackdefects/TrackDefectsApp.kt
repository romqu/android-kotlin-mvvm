package de.sevennerds.trackdefects

import android.app.Application
import android.content.Context
import de.sevennerds.trackdefects.di.AppComponent
import de.sevennerds.trackdefects.di.AppModule
import de.sevennerds.trackdefects.di.DaggerAppComponent

class TrackDefectsApp : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        fun get(context: Context): TrackDefectsApp =
                context.applicationContext as TrackDefectsApp
    }
}