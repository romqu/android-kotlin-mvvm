package de.sevennerds.trackdefects.presentation.select_contacts.navigation

import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import de.sevennerds.trackdefects.presentation.select_contacts.SelectContactsFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectContactsKey(val tag: String) : BaseKey() {
    constructor() : this("SelectContactsKey")

    override fun createFragment() = SelectContactsFragment()
}