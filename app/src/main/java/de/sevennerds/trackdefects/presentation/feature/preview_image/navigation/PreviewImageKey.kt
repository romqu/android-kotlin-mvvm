package de.sevennerds.trackdefects.presentation.feature.preview_image.navigation

import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import de.sevennerds.trackdefects.presentation.feature.preview_image.PreviewImageFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PreviewImageKey(val tag: String) : BaseKey() {
    constructor() : this("CreateDefectListSummaryKey")

    override fun createFragment() = PreviewImageFragment()
}