package de.sevennerds.trackdefects.presentation.base

import io.reactivex.ObservableTransformer

abstract class BaseViewModel<EVENT, STATE> {
    abstract val eventToRenderState: ObservableTransformer<EVENT, STATE>
}