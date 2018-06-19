package de.sevennerds.trackdefects.core.di

import dagger.Component
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.preview_image.PreviewImageFragment
import de.sevennerds.trackdefects.presentation.take_ground_plan_picture.TakeGroundPlanPictureFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ContextModule::class,
    DatabaseModule::class,
    NetworkModule::class,
    LruCacheModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(takeGroundPlanPictureFragment: TakeGroundPlanPictureFragment)
    fun inject(previewImageFragment: PreviewImageFragment)

}