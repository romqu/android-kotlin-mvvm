package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class EnterStreetAddressView {

    sealed class Event {

        class StreetNameTextChange(val text: String) : Event()
        class StreetNumberTextChange(val text: String) : Event()
        class StreetAdditionalTextChange(val text: String) : Event()
    }

    /**
     * Result returned by the domain
     */
    sealed class Result {

        class StreetNameTextChange(val text: String, val isNotEmpty: Boolean) : Result()
        class StreetNumberTextChange(val text: String) : Result()
        class StreetAdditionalTextChange(val text: String) : Result()
    }

    /**
     * The "global" view state, kept in the ViewModel
     */
    data class State(val streetName: String,
                     val streetNumber: String,
                     val streetAdditional: String,
                     val isButtonEnabled: Boolean,
                     val currentResult: Result?) {

        companion object {
            fun initial() = State(
                    "",
                    "",
                    "",
                    false,
                    currentResult = null)
        }
    }

    /**
     * The states the view receives and uses to render its ui, hence RenderState
     */
    sealed class RenderState() {
        class SetButtonState(val isEnabled: Boolean, val stateParcel: StateParcel) : RenderState()
        class Nothing(val stateParcel: StateParcel) : RenderState()
    }

    /**
     * The Parcelable version of the ViewState
     */
    @Parcelize
    data class StateParcel(val streetName: String,
                           val streetNumber: String,
                           val streetAdditional: String,
                           val isButtonEnabled: Boolean) : Parcelable
}