package de.sevennerds.trackdefects.presentation.select_participants_defect_list

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.wafflecopter.multicontactpicker.ContactResult
import kotlinx.android.parcel.Parcelize

class SelectParticipantsView {

    sealed class Event {

        data class Add(val contactResultList: List<ContactResult>,
                       val currentParticipantModelList: List<ParticipantModel>) : Event()

        data class Remove(val contactPosition: Int,
                          val currentParticipantModelList: List<ParticipantModel>) : Event()

        object Init : Event()
    }

    sealed class Result {
        data class Add(val diffResult: DiffUtil.DiffResult,
                       val participantModelList: List<ParticipantModel>) : Result()

        data class Remove(val participantModelList: List<ParticipantModel>,
                          val diffResult: DiffUtil.DiffResult) : Result()

        object Init : Result()
    }

    data class State(val participantModelList: List<ParticipantModel>,
                     val diffResult: DiffUtil.DiffResult?) {
        companion object {
            fun initial() = State(emptyList(), null)
        }
    }

    sealed class RenderState {
        data class Add(val participantModelList: List<ParticipantModel>,
                       val diffResult: DiffUtil.DiffResult) : RenderState()

        data class Remove(val participantModelList: List<ParticipantModel>,
                          val diffResult: DiffUtil.DiffResult) : RenderState()

        data class  Init(val participantModelList: List<ParticipantModel>) : RenderState()
    }

    @Parcelize
    data class StateParcel(val participantModelList: List<ParticipantModel>) : Parcelable

}









