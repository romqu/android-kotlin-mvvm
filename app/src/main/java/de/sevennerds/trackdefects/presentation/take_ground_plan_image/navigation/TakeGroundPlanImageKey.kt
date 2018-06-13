package de.sevennerds.trackdefects.presentation.take_ground_plan_image.navigation

import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import de.sevennerds.trackdefects.presentation.take_ground_plan_image.TakeGroundPlanImageFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TakeGroundPlanImageKey(val tag: String) : BaseKey() {
    constructor() : this("TakeGroundPlanImageKey")

    override fun createFragment() = TakeGroundPlanImageFragment()
}