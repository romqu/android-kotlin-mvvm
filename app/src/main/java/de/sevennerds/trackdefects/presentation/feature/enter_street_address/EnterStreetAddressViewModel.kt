package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import de.sevennerds.trackdefects.presentation.realm_db.CreateBasicDefectListSummaryRealm
import de.sevennerds.trackdefects.presentation.realm_db.RealmManager
import de.sevennerds.trackdefects.presentation.realm_db.StreetAddressRealm
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EnterStreetAddressViewModel @Inject constructor() {

    // entry point for the view
    // ---------------------------------------------------------------------------------------------
    val eventToRenderState = ObservableTransformer<EnterStreetAddressView.Event,
            EnterStreetAddressView.State> { upstream ->

        upstream.observeOn(Schedulers.io())
                .publish { shared ->

                    Observable.mergeArray(
                            shared.ofType(EnterStreetAddressView.Event
                                                  .Init::class.java)
                                    .compose(eventInitToResult),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetNameTextChange::class.java)
                                    .compose(eventStreetNameTextChangeToResult),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetNumberTextChange::class.java)
                                    .compose(eventStreetNumberTextChangeToResult),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .StreetAdditionalTextChange::class.java)
                                    .compose(eventStreetAdditionalTextChangeToResult),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .Next::class.java)
                                    .compose(eventNextToResult))
                            .compose(resultToViewState)
                }
    }

    // domain logic
    // ---------------------------------------------------------------------------------------------
    private val eventInitToResult = ObservableTransformer<EnterStreetAddressView.Event.Init,
            EnterStreetAddressView.Result> { upstream ->

        upstream.map { EnterStreetAddressView.Result.Init(it.parcelState) }
    }

    private val eventStreetNameTextChangeToResult =
            ObservableTransformer<EnterStreetAddressView.Event.StreetNameTextChange,
                    EnterStreetAddressView.Result> { upstream ->

                upstream
                        .map { it.text.trim() }
                        .distinctUntilChanged()
                        .map { EnterStreetAddressView.Result.StreetNameTextChange(it) }
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

    private val eventNextToResult =
            ObservableTransformer<EnterStreetAddressView.Event.Next,
                    EnterStreetAddressView.Result> { upstream ->

                upstream.map { EnterStreetAddressView.Result.Next }
            }

    // view and render and parcel state
    // ---------------------------------------------------------------------------------------------
    private val resultToViewState =
            ObservableTransformer<EnterStreetAddressView.Result,
                    EnterStreetAddressView.State> { upstream ->

                upstream.scan(EnterStreetAddressView.State.initial()) { previousState,
                                                                        result ->
                    when (result) {

                        is EnterStreetAddressView.Result.Init -> {

                            val parcelState = result.parcelState
                            val renderState = EnterStreetAddressView.RenderState.Init

                            parcelState?.let {
                                val state = previousState.copy(
                                        streetName = parcelState.streetName,
                                        streetNumber = parcelState.streetNumber,
                                        streetAdditional = parcelState.streetAdditional,
                                        isButtonEnabled = parcelState.isButtonEnabled)

                                state.copy(renderState = renderState)
                            }
                                    ?: previousState.copy(renderState = renderState)
                        }

                        is EnterStreetAddressView.Result.StreetNameTextChange -> {

                            val state = previousState.copy(streetName = result.text,
                                                           isButtonEnabled = result.text.isNotEmpty()
                                                                   && previousState.streetNumber.isNotEmpty())

                            val renderState = EnterStreetAddressView.RenderState
                                    .SetButtonState(state.isButtonEnabled,
                                                    viewStateToParcelState(state))

                            state.copy(renderState = renderState)
                        }

                        is EnterStreetAddressView.Result.StreetNumberTextChange -> {

                            val state = previousState.copy(streetNumber = result.text,
                                                           isButtonEnabled = result.text.isNotEmpty()
                                                                   && previousState.streetName.isNotEmpty())

                            val renderState = EnterStreetAddressView.RenderState
                                    .SetButtonState(state.isButtonEnabled,
                                                    viewStateToParcelState(state))

                            state.copy(renderState = renderState)
                        }

                        is EnterStreetAddressView.Result.StreetAdditionalTextChange -> {
                            val state = previousState.copy(streetAdditional = result.text)

                            val renderState = EnterStreetAddressView.RenderState
                                    .UpdateStateParcel(viewStateToParcelState(state))

                            state.copy(renderState = renderState)
                        }

                        is EnterStreetAddressView.Result.Next -> {

                            // TODO impure
                            updateSharedRealmObject(previousState)

                            previousState.copy(renderState = EnterStreetAddressView.RenderState.Next)
                        }
                    }

                }.skip(1)
            }

    private fun viewStateToParcelState(viewState: EnterStreetAddressView.State) =
            with(viewState) {
                EnterStreetAddressView
                        .ParcelState(streetName,
                                     streetNumber,
                                     streetAdditional,
                                     isButtonEnabled)
            }

    // TODO impure
    private fun updateSharedRealmObject(viewState: EnterStreetAddressView.State) {

        RealmManager.insertOrUpdate(
                CreateBasicDefectListSummaryRealm(
                        streetAddressRealm =
                        StreetAddressRealm(
                                streetName = viewState.streetName,
                                streetNumber = viewState.streetNumber,
                                streetAdditional = viewState.streetAdditional)))
    }
}



