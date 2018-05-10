package de.sevennerds.trackdefects.di

import dagger.Component
import de.sevennerds.trackdefects.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ContextModule::class, DatabaseModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
}