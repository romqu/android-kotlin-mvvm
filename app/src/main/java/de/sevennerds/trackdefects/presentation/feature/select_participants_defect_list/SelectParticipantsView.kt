package de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.wafflecopter.multicontactpicker.ContactResult
import de.sevennerds.trackdefects.core.di.MessageQueue
import kotlinx.android.parcel.Parcelize

class SelectParticipantsView {

    /**
     * Events created by the view (Fragment)
     */
    sealed class Event {

        class Init(val parcelState: ParcelState) : Event()

        object ShowContacts : Event()

        data class Add(val contactResultList: List<ContactResult>,
                       val currentParticipantModelList: List<ParticipantModel>) : Event()

        data class Remove(val contactPosition: Int,
                          val currentParticipantModelList: List<ParticipantModel>) : Event()

        object Next : Event()


    }

    /**
     * Result returned by the domain
     */
    sealed class Result {

        class Init(val parcelState: ParcelState) : Result()

        object ShowContacts : Result()

        data class Add(val diffResult: DiffUtil.DiffResult,
                       val participantModelList: List<ParticipantModel>) : Result()

        data class Remove(val participantModelList: List<ParticipantModel>,
                          val diffResult: DiffUtil.DiffResult) : Result()

        object Next : Result()
    }

    data class State(val participantModelList: List<ParticipantModel>,
                     val nextOrSkipButtonText: String,
                     val renderState: RenderState) {
        companion object {
            fun initial() = State(emptyList(), "Skip", RenderState.None)
        }
    }

    sealed class RenderState {

        class Init(val parcelState: ParcelState) : RenderState()

        object ShowContacts : RenderState()

        class Add(val diffResult: DiffUtil.DiffResult,
                  val parcelState: ParcelState) : RenderState()

        class Remove(val diffResult: DiffUtil.DiffResult,
                     val parcelState: ParcelState) : RenderState()

        object Next : RenderState()

        object None : RenderState()
    }

    /**
     * The Parcelable version of the ViewState
     */
    @Parcelize
    data class ParcelState(val participantModelList: List<ParticipantModel>) : Parcelable

    sealed class Message : MessageQueue.Message {

        class StreetAddress(val streetName: String,
                            val streetNumber: String,
                            val streetAdditional: String) : Message()
    }


}









