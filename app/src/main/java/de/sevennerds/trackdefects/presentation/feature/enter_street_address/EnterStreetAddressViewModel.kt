package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EnterStreetAddressViewModel @Inject constructor() {


    private fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private var viewState = EnterStreetAddressView.State.initial()

    val eventToRenderState = ObservableTransformer<EnterStreetAddressView.Event,
            EnterStreetAddressView.RenderState> { upstream ->

        upstream.observeOn(Schedulers.io())
                .publish { shared ->
                    Observable.merge(shared.ofType(EnterStreetAddressView.Event.StreetNameTextChange::class.java)
                            .compose(eventStreetNameTextChange),
                            Observable.empty())
                }
    }

    private val eventStreetNameTextChange = ObservableTransformer<EnterStreetAddressView.Event.StreetNameTextChange,
            EnterStreetAddressView.RenderState> { upstream ->

        upstream
                .map { it.text.trim() }
                .distinctUntilChanged()
                .map { EnterStreetAddressView.Result.StreetNameTextChange(it, it.isNotEmpty()) }
                .compose(resultToViewState)
                .map {
                    Logger.d(it)
                    EnterStreetAddressView.RenderState.SetButtonState(it.isButtonEnabled)
                }

    }

    private val resultToViewState = ObservableTransformer<EnterStreetAddressView.Result,
            EnterStreetAddressView.State> { upstream ->

        upstream.scan(EnterStreetAddressView.State.initial()) { previousState,
                                                                result ->

            when (result) {

                is EnterStreetAddressView.Result.StreetNameTextChange -> {
                    viewState = previousState.copy(isButtonEnabled = result.isNotEmpty)
                    viewState
                }
                is EnterStreetAddressView.Result.StreetNumberTextChange -> TODO()
                is EnterStreetAddressView.Result.StreetAdditionalTextChange -> TODO()
            }

        }.skip(1)

    }
}