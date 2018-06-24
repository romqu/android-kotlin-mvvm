package de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list.navigation

import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list.SelectParticipantsFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectParticipantsKey(val tag: String) : BaseKey() {
    constructor() : this("CreateDefectListSummary")

    override fun createFragment() = SelectParticipantsFragment()
}