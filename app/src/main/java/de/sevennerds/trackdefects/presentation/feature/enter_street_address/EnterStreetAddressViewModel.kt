package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EnterStreetAddressViewModel @Inject constructor() {

    val eventToRenderState = ObservableTransformer<EnterStreetAddressView.Event,
            EnterStreetAddressView.RenderState> { upstream ->

        upstream.observeOn(Schedulers.io())
                .publish { shared ->

                    Observable.merge(
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetNameTextChange::class.java)
                                    .compose(eventStreetNameTextChangeToResult),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetNumberTextChange::class.java)
                                    .compose(eventStreetNumberTextChangeToResult),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetAdditionalTextChange::class.java)
                                    .compose(eventStreetAdditionalTextChangeToResult))
                            .compose(resultToViewState)
                            .compose(viewStateToRenderState)
                }
    }

    private val eventStreetNameTextChangeToResult =
            ObservableTransformer<EnterStreetAddressView.Event.StreetNameTextChange,
                    EnterStreetAddressView.Result> { upstream ->

                upstream
                        .map { it.text.trim() }
                        .distinctUntilChanged()
                        .map { EnterStreetAddressView.Result.StreetNameTextChange(it, it.isNotEmpty()) }
            }


    private val eventStreetNumberTextChangeToResult =
            ObservableTransformer<EnterStreetAddressView.Event.StreetNumberTextChange,
                    EnterStreetAddressView.Result> { upstream ->

                upstream.map { EnterStreetAddressView.Result.StreetNumberTextChange(it.text) }
            }

    private val eventStreetAdditionalTextChangeToResult =
            ObservableTransformer<EnterStreetAddressView.Event.StreetAdditionalTextChange,
                    EnterStreetAddressView.Result> { upstream ->

                upstream.map { it.text.trim() }
                        .distinctUntilChanged()
                        .map { EnterStreetAddressView.Result.StreetAdditionalTextChange(it) }
            }


    private val viewStateToRenderState = ObservableTransformer<EnterStreetAddressView.State,
            EnterStreetAddressView.RenderState> { upstream ->

        upstream.map { viewState ->
            when (viewState.currentResult) {

                is EnterStreetAddressView.Result.StreetNameTextChange -> {
                    EnterStreetAddressView.RenderState
                            .SetButtonState(viewState.isButtonEnabled,
                                            viewStateToParcelState(viewState))
                }

                is EnterStreetAddressView.Result.StreetNumberTextChange -> {
                    EnterStreetAddressView.RenderState.Nothing(viewStateToParcelState(viewState))
                }

                is EnterStreetAddressView.Result.StreetAdditionalTextChange -> TODO()

                null ->
                    EnterStreetAddressView.RenderState.Nothing(viewStateToParcelState(viewState))
            }
        }
    }

    private val resultToViewState =
            ObservableTransformer<EnterStreetAddressView.Result,
                    EnterStreetAddressView.State> { upstream ->


                upstream.scan(EnterStreetAddressView.State.initial())
                { previousState,
                  result ->

                    when (result) {

                        is EnterStreetAddressView.Result.StreetNameTextChange -> {
                            previousState.copy(streetName = result.text,
                                               isButtonEnabled = result.isNotEmpty,
                                               currentResult = result)
                        }

                        is EnterStreetAddressView.Result.StreetNumberTextChange -> {
                            previousState.copy(streetNumber = result.text,
                                               currentResult = result)
                        }

                        is EnterStreetAddressView.Result.StreetAdditionalTextChange -> {
                            previousState.copy(streetAdditional = result.text)
                        }

                        is EnterStreetAddressView.Result.Next -> {
                            previousState
                        }
                    }

                }.skip(1)
            }

    private fun viewStateToParcelState(viewState: EnterStreetAddressView.State) =
            with(viewState) {
                EnterStreetAddressView
                        .StateParcel(streetName,
                                     streetNumber,
                                     streetAdditional,
                                     isButtonEnabled)
            }

}



