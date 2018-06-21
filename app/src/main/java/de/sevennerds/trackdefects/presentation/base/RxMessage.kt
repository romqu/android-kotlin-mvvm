package de.sevennerds.trackdefects.presentation.base

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RxMessage @Inject constructor() {

    private val subject = PublishSubject.create<Any>()

    fun <T : Message> send(message: T) = subject.onNext(message)

    fun <T : Message> get(type: Class<T>): Observable<T> =
            subject.ofType(type)
                    .toFlowable(BackpressureStrategy.BUFFER)
                    .toObservable()
}

interface Message