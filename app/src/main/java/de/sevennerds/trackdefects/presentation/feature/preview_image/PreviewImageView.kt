package de.sevennerds.trackdefects.presentation.feature.preview_image

import android.graphics.Bitmap
import android.os.Parcelable
import de.sevennerds.trackdefects.presentation.model.FileModel
import kotlinx.android.parcel.Parcelize

class PreviewImageView {

    /**
     * Events created by the view (Fragment)
     */
    sealed class Event {

        object DismissImage : Event()

        object AcceptImage : Event()

        object LoadImage : Event()
    }

    /**
     * Result returned by the domain
     */
    sealed class Result {
        object DismissImage : Result()

        object AcceptImage : Result()

        object LoadImageError : Result()

        class LoadImage(val image: FileModel<Bitmap>) : Result()
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
        object DismissImage : RenderState()

        object AcceptImage : RenderState()

        class LoadImage(val bitmap: Bitmap) : RenderState()

        object None : RenderState()
    }

    /**
     * The Parcelable version of the ViewState
     */
    @Parcelize
    data class StateParcel(val imageName: String) : Parcelable

}