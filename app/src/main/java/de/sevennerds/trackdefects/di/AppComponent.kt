package de.sevennerds.trackdefects.di

import dagger.Component
import de.sevennerds.trackdefects.presentation.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ContextModule::class,
    DatabaseModule::class,
    NetworkModule::class]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}