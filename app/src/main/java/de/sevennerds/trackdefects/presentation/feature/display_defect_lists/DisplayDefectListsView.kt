package de.sevennerds.trackdefects.presentation.feature.display_defect_lists

import android.os.Parcelable
import de.sevennerds.trackdefects.presentation.model.DefectListModel
import kotlinx.android.parcel.Parcelize

class DisplayDefectListsView {

    sealed class Event {

        object Init : Event()
    }

    /**
     * Result returned by the domain
     */
    sealed class Result {

        class Init(val defectListModelList: List<DefectListModel>) : Result()
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
        class Init(val defectListModelList: List<DefectListModel>) : RenderState()
        object None : RenderState()
    }

    /**
     * The Parcelable version of the ViewState
     */
    @Parcelize
    data class ParcelState(val name: String) : Parcelable
}