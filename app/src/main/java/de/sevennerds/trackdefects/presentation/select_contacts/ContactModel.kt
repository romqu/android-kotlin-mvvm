package de.sevennerds.trackdefects.presentation.select_contacts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactModel(val name: String, val phoneNumber: String, val email: String) : Parcelable