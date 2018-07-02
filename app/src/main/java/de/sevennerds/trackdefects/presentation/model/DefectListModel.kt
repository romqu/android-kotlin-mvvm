package de.sevennerds.trackdefects.presentation.model

import android.graphics.Bitmap

data class DefectListModel(val name: String,
                           val imageModel: FileModel<Bitmap>,
                           val streetAddressModel: StreetAddressModel,
                           val viewParticipantModelList: List<ViewParticipantModel>)