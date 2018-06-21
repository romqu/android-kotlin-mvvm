package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import io.reactivex.ObservableTransformer
import javax.inject.Inject

class EnterStreetAddressViewModel @Inject constructor() {

    val eventToRenderState = ObservableTransformer<EnterStreetAddressView.Event,
            EnterStreetAddressView.RenderState> { upstream ->

    }

    val resultToViewState = ObservableTransformer<EnterStreetAddressView.Result,
            EnterStreetAddressView.State> { upstream ->

    }
}