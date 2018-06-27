package de.sevennerds.trackdefects.core.di

import dagger.Component
import de.sevennerds.trackdefects.data.defect_list.DefectListRepository
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanRepository
import de.sevennerds.trackdefects.data.street_address.StreetAddressRepository
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantRepository
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.feature.enter_street_address.EnterStreetAddressFragment
import de.sevennerds.trackdefects.presentation.feature.preview_image.PreviewImageFragment
import de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list.SelectParticipantsFragment
import de.sevennerds.trackdefects.presentation.feature.take_ground_plan_picture.TakeGroundPlanPictureFragment
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AppModule::class,
            ContextModule::class,
            DatabaseModule::class,
            NetworkModule::class,
            LruCacheModule::class
        ]
)
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(enterStreetAddressFragment: EnterStreetAddressFragment)
    fun inject(selectParticipantsFragment: SelectParticipantsFragment)
    fun inject(takeGroundPlanPictureFragment: TakeGroundPlanPictureFragment)
    fun inject(previewImageFragment: PreviewImageFragment)

    // Exposed sub-graphs mostly for testing
    fun floorPlanRepository(): FloorPlanRepository

    fun viewParticipantRepository(): ViewParticipantRepository
    fun streetAddressRepository(): StreetAddressRepository
    fun defectRepository(): DefectListRepository


}