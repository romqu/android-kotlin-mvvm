package de.sevennerds.trackdefects.presentation.feature.create_defect_list_summary

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class CreateDefectListSummaryView {

    sealed class Event {

        object Init : Event()
    }

    /**
     * Result returned by the domain
     */
    sealed class Result {

        class Init() : Result()
    }

    /**
     * The "global" view state, kept in the ViewModel
     */
    data class State(val renderState: RenderState) {

        companion object {
            fun initial() = State(RenderState.None)
        }
    }

    /**
     * The states the view receives and uses to render its ui, hence RenderState
     */
    sealed class RenderState {
        object None : RenderState()
    }

    /**
     * The Parcelable version of the ViewState
     */
    @Parcelize
    data class StateParcel(val name: String) : Parcelable
}