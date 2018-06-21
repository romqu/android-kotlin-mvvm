package de.sevennerds.trackdefects.presentation.feature.take_ground_plan_picture.navigation

import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import de.sevennerds.trackdefects.presentation.feature.take_ground_plan_picture.TakeGroundPlanPictureFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TakeGroundPlanPictureKey(val tag: String) : BaseKey() {
    constructor() : this("TakeGroundPlanPictureKey")

    override fun createFragment() = TakeGroundPlanPictureFragment()
}