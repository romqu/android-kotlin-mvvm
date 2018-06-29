package de.sevennerds.trackdefects.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StreetAddressModel(val name: String,
                              val number: String,
                              val additional: String) : Parcelable