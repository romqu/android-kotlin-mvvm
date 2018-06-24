package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class EnterStreetAddressViewModel @Inject constructor() {

    val eventToRenderState = ObservableTransformer<EnterStreetAddressView.Event,
            EnterStreetAddressView.RenderState> { upstream ->

        upstream//a.observeOn(Schedulers.io())
                .publish { shared ->
                    /*Observable.merge(
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetNameTextChange::class.java)
                                    .compose(eventStreetNameTextChange),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetNumberTextChange::class.java)
                                    .compose(eventStreetNumberTextChange),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetAdditionalTextChange::class.java)
                                    .compose(eventStreetAdditionalTextChange),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .Restart::class.java)
                                    .compose(eventRestart))*/

                    Observable.merge(
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetNameTextChange::class.java)
                                    .compose(eventStreetNameTextChangeToResult),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .Restart::class.java)
                                    .compose(eventRestartToResult),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetNumberTextChange::class.java)
                                    .compose(eventStreetNumberTextChangeToResult))
                            .compose(resultToViewState)
                            .compose(viewStateToRenderState)
                }
    }

    private val eventStreetNameTextChangeToResult =
            ObservableTransformer<EnterStreetAddressView.Event.StreetNameTextChange,
                    EnterStreetAddressView.Result> { upstream ->

                upstream
                        .map { it.text.trim() }
                        //.distinctUntilChanged()
                        .map { EnterStreetAddressView.Result.StreetNameTextChange(it, it.isNotEmpty()) }
            }

    private val eventRestartToResult = ObservableTransformer<EnterStreetAddressView.Event.Restart,
            EnterStreetAddressView.Result> { upstream ->

        upstream.map { EnterStreetAddressView.Result.Restart }
    }


    private val eventStreetNumberTextChangeToResult =
            ObservableTransformer<EnterStreetAddressView.Event.StreetNumberTextChange,
                    EnterStreetAddressView.Result> { upstream ->

                upstream.map { EnterStreetAddressView.Result.StreetNumberTextChange(it.text) }
            }


    private val viewStateToRenderState = ObservableTransformer<EnterStreetAddressView.State,
            EnterStreetAddressView.RenderState> { upstream ->

        upstream.map { viewState ->
            when (viewState.currentResult) {

                is EnterStreetAddressView.Result.StreetNameTextChange -> {
                    EnterStreetAddressView.RenderState
                            .SetButtonState(viewState.isButtonEnabled)
                }

                is EnterStreetAddressView.Result.StreetNumberTextChange -> {
                    EnterStreetAddressView.RenderState.Nothing
                }

                is EnterStreetAddressView.Result.StreetAdditionalTextChange -> TODO()

                is EnterStreetAddressView.Result.Restart -> {
                    EnterStreetAddressView.RenderState.Restart(
                            EnterStreetAddressView.StateParcel(
                                    viewState.streetName,
                                    viewState.streetAdditional,
                                    viewState.streetNumber,
                                    viewState.isButtonEnabled
                            ))
                }

                null -> EnterStreetAddressView.RenderState.Nothing
            }
        }
    }

    private val eventStreetNameTextChange =
            ObservableTransformer<EnterStreetAddressView.Event.StreetNameTextChange,
                    EnterStreetAddressView.RenderState> { upstream ->

                upstream
                        .map { it.text.trim() }
                        //.distinctUntilChanged()
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
                        //.map { it.text.trim() }
                        //.distinctUntilChanged()
                        .map { EnterStreetAddressView.Result.StreetNumberTextChange(it.text) }
                        .compose(resultToViewState)
                        .map {
                            EnterStreetAddressView.RenderState.Nothing
                        }
            }

    private val eventStreetAdditionalTextChange = ObservableTransformer<EnterStreetAddressView.Event.StreetAdditionalTextChange,
            EnterStreetAddressView.RenderState> { upstream ->

        upstream
                //.map { it.text.trim() }
                //.distinctUntilChanged()
                .map { EnterStreetAddressView.Result.StreetAdditionalTextChange(it.text) }
                .compose(resultToViewState)
                .map {
                    EnterStreetAddressView.RenderState.Nothing
                }
    }

    private val eventRestart = ObservableTransformer<EnterStreetAddressView.Event.Restart,
            EnterStreetAddressView.RenderState> { upstream ->

        upstream
                .map { EnterStreetAddressView.Result.Restart }
                .compose(resultToViewState)
                .map {
                    EnterStreetAddressView.RenderState.Restart(
                            EnterStreetAddressView.StateParcel(
                                    it.streetName,
                                    it.streetAdditional,
                                    it.streetNumber,
                                    it.isButtonEnabled
                            ))
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

                        is EnterStreetAddressView.Result.Restart -> {
                            Logger.d(previousState)
                            previousState.copy(currentResult = result)

                        }
                    }

                }.doOnNext { Logger.d("NEXT: $it") }
                        .skip(1)
            }
}



