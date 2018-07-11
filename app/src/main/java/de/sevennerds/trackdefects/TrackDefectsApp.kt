package de.sevennerds.trackdefects

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import de.sevennerds.trackdefects.core.di.AppComponent
import de.sevennerds.trackdefects.core.di.AppModule
import de.sevennerds.trackdefects.core.di.DaggerAppComponent
import de.sevennerds.trackdefects.domain.feature.delete_temp_dir.DeleteTempDirTask
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject


class TrackDefectsApp : Application() {

    @Inject
    lateinit var deleteTempDirTask: DeleteTempDirTask

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }


    override fun onCreate() {
        super.onCreate()

        appComponent
                .inject(this)

        initLeakCanary()
        initLogger()
        initStetho()
        initRealm()
        initThreeTenAbp()

        deleteTempDir()
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
            override fun isLoggable(priority: Int, tag: String?): Boolean =
                    BuildConfig.DEBUG

        })
    }


    private fun initStetho() =
            Stetho.initializeWithDefaults(this)

    private fun initRealm() {
        Realm.init(this)
    }


    private fun initThreeTenAbp() {
        AndroidThreeTen.init(this);
    }

    private fun deleteTempDir() {
        /*deleteTempDirTask
                .execute()
                .subscribe()*/
    }

    companion object {
        fun get(context: Context): TrackDefectsApp =
                context.applicationContext as TrackDefectsApp
    }
}