package de.sevennerds.trackdefects.presentation.feature.create_defect_list_summary.navigation

import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import de.sevennerds.trackdefects.presentation.feature.create_defect_list_summary.CreateDefectListSummaryFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateDefectListSummaryKey(val tag: String) : BaseKey() {
    constructor() : this("CreateDefectListSummaryKey")

    override fun createFragment() = CreateDefectListSummaryFragment()
}