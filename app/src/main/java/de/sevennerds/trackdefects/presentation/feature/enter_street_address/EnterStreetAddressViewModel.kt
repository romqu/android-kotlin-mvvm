package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import com.orhanobut.logger.Logger
import com.vicpin.krealmextensions.save
import de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list.SelectParticipantsView
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
                                    .compose(eventStreetAdditionalTextChangeToResult),
                            shared.ofType(EnterStreetAddressView.Event
                                                  .Next::class.java)
                                    .compose(eventNextToResult))
                            .compose(resultToViewState)
                            .compose(viewStateToRenderState)
                }
    }


    // domain logic
    // ---------------------------------------------------------------------------------------------
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
                            previousState.copy(streetAdditional = result.text,
                                               currentResult = result)
                        }

                        is EnterStreetAddressView.Result.Next -> {
                            previousState.copy(currentResult = result)
                        }
                    }

                }.skip(1)
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

                is EnterStreetAddressView.Result.StreetAdditionalTextChange ->
                    EnterStreetAddressView.RenderState.Nothing(viewStateToParcelState(viewState))

                is EnterStreetAddressView.Result.Next -> {

                    // TODO impure
                    updateSharedRealmObject(viewState)

                    with(viewState) {
                        EnterStreetAddressView.RenderState.Next(
                                SelectParticipantsView.Message
                                        .StreetAddress(streetName,
                                                       streetNumber,
                                                       streetAdditional))
                    }
                }

                null ->
                    EnterStreetAddressView.RenderState.Nothing(viewStateToParcelState(viewState))

            }
        }
    }

    private fun viewStateToParcelState(viewState: EnterStreetAddressView.State) =
            with(viewState) {
                EnterStreetAddressView
                        .StateParcel(streetName,
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



