package de.sevennerds.trackdefects.presentation.feature.enter_street_address.navigation

import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import de.sevennerds.trackdefects.presentation.feature.enter_street_address.EnterStreetAddressFragment
import de.sevennerds.trackdefects.presentation.feature.preview_image.PreviewImageFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EnterStreetAddressKey(val tag: String) : BaseKey() {
    constructor() : this("CreateDefectListSummary")

    override fun createFragment() = EnterStreetAddressFragment()
}