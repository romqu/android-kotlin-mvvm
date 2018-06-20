package de.sevennerds.trackdefects.presentation.preview_image.navigation

import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import de.sevennerds.trackdefects.presentation.enter_street_address.EnterStreetAddressFragment
import de.sevennerds.trackdefects.presentation.preview_image.PreviewImageFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EnterStreetAddressKey(val tag: String) : BaseKey() {
    constructor() : this("EnterStreetAddressKey")

    override fun createFragment() = EnterStreetAddressFragment()
}