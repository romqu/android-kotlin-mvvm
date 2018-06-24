package de.sevennerds.trackdefects.presentation.feature.create_defect_list_summary.navigation

import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import de.sevennerds.trackdefects.presentation.feature.create_defect_list_summary.CreateDefectListSummaryFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateDefectListSummary(val tag: String) : BaseKey() {
    constructor() : this("CreateDefectListSummary")

    override fun createFragment() = CreateDefectListSummaryFragment()
}