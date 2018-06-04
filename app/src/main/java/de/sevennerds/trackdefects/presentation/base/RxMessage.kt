package de.sevennerds.trackdefects.presentation.base

import androidx.leanback.widget.PlaybackSeekUi
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


@Singleton
class RxMessage @Inject constructor() {

    private val subject = PublishSubject.create<Any>()

    fun <T: Message> send(message: T) = subject.onNext(message)

    fun <T: Message> get(type: Class<T>): Observable<T> = subject.ofType(type)
}

interface Message