package de.sevennerds.trackdefects.presentation.feature.display_defect_lists

import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DisplayDefectListsKey(val tag: String) : BaseKey() {
    constructor() : this("DisplayDefectListsKey")

    override fun createFragment() = DisplayDefectListsFragment()
}