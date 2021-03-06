package de.sevennerds.trackdefects.presentation.feature.take_ground_plan_picture

import android.os.Parcelable
import io.fotoapparat.result.PhotoResult
import kotlinx.android.parcel.Parcelize

class TakeGroundPlanPictureView {

    sealed class Event {

        data class TakePicture(val photoResult: PhotoResult) : Event()
    }

    /**
     * Result returned by the domain
     */
    sealed class Result {

        data class TakePicture(val imageName: String) : Result()
    }

    /**
     * The "global" view state, kept in the ViewModel
     */
    data class State(val imageName: String,
                     val renderState: RenderState) {

        companion object {
            fun initial() = State("", RenderState.None)
        }
    }

    /**
     * The states the view receives and uses to render its ui, hence RenderState
     */
    sealed class RenderState {
        object TakePicture : RenderState()
        object None : RenderState()
    }

    /**
     * The Parcelable version of the ViewState
     */
    @Parcelize
    data class StateParcel(val imageName: String) : Parcelable

}