package de.sevennerds.trackdefects.presentation.feature.preview_image

import android.graphics.Bitmap
import android.os.Parcelable
import de.sevennerds.trackdefects.core.di.MessageQueue
import de.sevennerds.trackdefects.presentation.model.FileModel
import kotlinx.android.parcel.Parcelize

class PreviewImageView {

    /**
     * Events created by the view (Fragment)
     */
    sealed class Event {

        object DismissImage : Event()

        object AcceptImage : Event()

        class LoadImage(val imageName: String) : Event()
    }

    /**
     * Result returned by the domain
     */
    sealed class Result {
        object DismissImage : Result()

        object AcceptImage : Result()

        data class LoadImage(val image: FileModel<Bitmap>) : Result()
    }

    /**
     * The "global" view state, kept in the ViewModel
     */
    data class State(val image: FileModel<Bitmap>) {
        companion object {
            fun initial() = State(FileModel(
                    "",
                    Bitmap.createBitmap(
                            10,
                            10,
                            Bitmap.Config.ARGB_8888)))
        }
    }

    /**
     * The states the view receives and uses to render its ui, hence RenderState
     */
    sealed class RenderState {
        object DismissImage : RenderState()

        object AcceptImage : RenderState()

        data class LoadImage(val bitmap: Bitmap) : RenderState()
    }

    /**
     * The Parcelable version of the ViewState
     */
    @Parcelize
    data class StateParcel(val imageName: String) : Parcelable

    sealed class Message() : MessageQueue.Message {
        class ImageName(val imageName: String) : Message()
    }

}