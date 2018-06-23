package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EnterStreetAddressViewModel @Inject constructor() {

    private var viewState = EnterStreetAddressView.State.initial()

    val eventToRenderState = ObservableTransformer<EnterStreetAddressView.Event,
            EnterStreetAddressView.RenderState> { upstream ->

        upstream.observeOn(Schedulers.io())
                .publish { shared ->
                    Observable.merge(
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetNameTextChange::class.java)
                                    .compose(eventStreetNameTextChange),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetNumberTextChange::class.java)
                                    .compose(eventStreetNumberTextChange))
                }
    }

    private val eventStreetNameTextChange =
            ObservableTransformer<EnterStreetAddressView.Event.StreetNameTextChange,
                    EnterStreetAddressView.RenderState> { upstream ->

                upstream
                        .map { it.text.trim() }
                        .distinctUntilChanged()
                        .map { EnterStreetAddressView.Result.StreetNameTextChange(it, it.isNotEmpty()) }
                        .compose(resultToViewState)
                        .map {
                            EnterStreetAddressView.RenderState.SetButtonState(it.isButtonEnabled)
                        }

            }

    private val eventStreetNumberTextChange =
            ObservableTransformer<EnterStreetAddressView.Event.StreetNumberTextChange,
                    EnterStreetAddressView.RenderState> { upstream ->

                upstream
                        .map { it.text.trim() }
                        .distinctUntilChanged()
                        .map { EnterStreetAddressView.Result.StreetNumberTextChange(it) }
                        .compose(resultToViewState)
                        .map {
                            EnterStreetAddressView.RenderState.Nothing
                        }

            }

    private val eventStreetAdditionalTextChange = ObservableTransformer<EnterStreetAddressView.Event.StreetAdditionalTextChange,
            EnterStreetAddressView.RenderState> { upstream ->

        upstream
                .map { it.text.trim() }
                .distinctUntilChanged()
                .map { EnterStreetAddressView.Result.StreetNumberTextChange(it) }
                .compose(resultToViewState)
                .map {
                    EnterStreetAddressView.RenderState.Nothing
                }

    }

    private val resultToViewState = ObservableTransformer<EnterStreetAddressView.Result,
            EnterStreetAddressView.State> { upstream ->

        upstream.scan(EnterStreetAddressView.State.initial()) { previousState,
                                                                result ->

            when (result) {

                is EnterStreetAddressView.Result.StreetNameTextChange -> {

                    val newState = previousState.copy(streetName = result.text,
                                                      isButtonEnabled = result.isNotEmpty)

                    viewState = newState

                    newState
                }

                is EnterStreetAddressView.Result.StreetNumberTextChange -> {
                    val newState = previousState.copy(streetNumber = result.text)

                    viewState = newState

                    newState
                }

                is EnterStreetAddressView.Result.StreetAdditionalTextChange -> {

                    val newState = previousState.copy(streetAdditional = result.text)

                    viewState = newState

                    newState
                }
            }

        }.skip(1)

    }
}