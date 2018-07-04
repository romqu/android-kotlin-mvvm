package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class EnterStreetAddressView {

    sealed class Event {

        class Init(val parcelState: ParcelState?) : Event()
        class StreetNameTextChange(val text: String) : Event()
        class StreetNumberTextChange(val text: String) : Event()
        class StreetAdditionalTextChange(val text: String) : Event()
        object Next : Event()
    }

    /**
     * Result returned by the domain
     */
    sealed class Result {

        class Init(val parcelState: ParcelState?) : Result()
        class StreetNameTextChange(val text: String) : Result()
        class StreetNumberTextChange(val text: String) : Result()
        class StreetAdditionalTextChange(val text: String) : Result()
        object Next : Result()
    }

    /**
     * The "global" view state, kept in the ViewModel
     */
    data class State(val streetName: String,
                     val streetNumber: String,
                     val streetAdditional: String,
                     val isButtonEnabled: Boolean,
                     val renderState: RenderState) {

        companion object {
            fun initial() = State(
                    "",
                    "",
                    "",
                    false,
                    renderState = RenderState.None)
        }
    }

    /**
     * The states the view receives and uses to render its ui, hence RenderState
     */
    sealed class RenderState {
        data class SetButtonState(
                val isEnabled: Boolean,
                val parcelState: ParcelState) : RenderState()
        object Init : RenderState()
        data class UpdateStateParcel(val parcelState: ParcelState): RenderState()
        object None : RenderState()
        object Next : RenderState()
    }

    /**
     * The Parcelable version of the ViewState
     */
    @Parcelize
    data class ParcelState(val streetName: String,
                           val streetNumber: String,
                           val streetAdditional: String,
                           val isButtonEnabled: Boolean) : Parcelable
}