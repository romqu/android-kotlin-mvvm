package de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list

import android.os.Parcelable
import de.sevennerds.trackdefects.presentation.base.IDiffable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class
ParticipantModel(val name: String,
                 val phoneNumber: String,
                 val email: String,
                 override val uuid: String = UUID.randomUUID().toString())
    : Parcelable, IDiffable