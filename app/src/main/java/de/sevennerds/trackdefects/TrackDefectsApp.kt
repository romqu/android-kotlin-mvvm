package de.sevennerds.trackdefects

import android.app.Application
import android.content.Context
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
    }

    private fun initLeakCanary(): Unit {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        LeakCanary.install(this);
    }

    companion object {
        fun get(context: Context): TrackDefectsApp =
                context.applicationContext as TrackDefectsApp
    }
}