package de.sevennerds.trackdefects

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
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

        initLeakCanary()
        initStetho()
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        LeakCanary.install(this)
    }

    private fun initStetho() = Stetho.initializeWithDefaults(this)

    companion object {
        fun get(context: Context): TrackDefectsApp =
                context.applicationContext as TrackDefectsApp
    }
}