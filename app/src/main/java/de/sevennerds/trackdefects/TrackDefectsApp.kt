package de.sevennerds.trackdefects

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.squareup.leakcanary.LeakCanary
import de.sevennerds.trackdefects.di.AppComponent
import de.sevennerds.trackdefects.di.AppModule
import de.sevennerds.trackdefects.di.DaggerAppComponent
import com.orhanobut.logger.PrettyFormatStrategy
import com.orhanobut.logger.FormatStrategy




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
        initLogger()
        initStetho()
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        LeakCanary.install(this)
    }

    private fun initLogger() {

        val formatStrategy = PrettyFormatStrategy.newBuilder()
/*                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat*/
                .methodCount(1)
                .tag("LOGGER")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()



        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }


    private fun initStetho() =
            Stetho.initializeWithDefaults(this)

    companion object {
        fun get(context: Context): TrackDefectsApp =
                context.applicationContext as TrackDefectsApp
    }
}