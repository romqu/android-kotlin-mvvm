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

        data class Add(val contactResultList: List<ContactResult>,
                       val currentParticipantModelList: List<ParticipantModel>) : Event()

        data class Remove(val contactPosition: Int,
                          val currentParticipantModelList: List<ParticipantModel>) : Event()

        class Init(val stateParcel: StateParcel) : Event()
    }

    /**
     * Result returned by the domain
     */
    sealed class Result {
        data class Add(val diffResult: DiffUtil.DiffResult,
                       val participantModelList: List<ParticipantModel>) : Result()

        data class Remove(val participantModelList: List<ParticipantModel>,
                          val diffResult: DiffUtil.DiffResult) : Result()

        class Init(val stateParcel: StateParcel) : Result()
    }

    /**
     * The "global" view state, kept in the ViewModel
     */
    data class State(val participantModelList: List<ParticipantModel>,
                     val diffResult: DiffUtil.DiffResult?,
                     val render: RenderState) {
        companion object {
            fun initial() = State(emptyList(), null, RenderState.None)
        }
    }

    /**
     * The states the view receives and uses to render its ui, hence RenderState
     */
    sealed class RenderState {
        data class Add(val participantModelList: List<ParticipantModel>,
                       val diffResult: DiffUtil.DiffResult) : RenderState()

        data class Remove(val participantModelList: List<ParticipantModel>,
                          val diffResult: DiffUtil.DiffResult) : RenderState()

        data class Init(val participantModelList: List<ParticipantModel>) : RenderState()

        object None: RenderState()
    }

    sealed class RenderStateTest {
        data class Add(val participantModelList: List<ParticipantModel>,
                       val diffResult: DiffUtil.DiffResult) : RenderStateTest()

        data class Remove(val participantModelList: List<ParticipantModel>,
                          val diffResult: DiffUtil.DiffResult) : RenderStateTest()

        data class Init(val participantModelList: List<ParticipantModel>) : RenderStateTest()
    }

    /**
     * The Parcelable version of the ViewState
     */
    @Parcelize
    data class StateParcel(val participantModelList: List<ParticipantModel>) : Parcelable

    sealed class Message : MessageQueue.Message {

        class StreetAddress(val streetName: String,
                            val streetNumber: String,
                            val streetAdditional: String) : Message()
    }


}









