package de.sevennerds.trackdefects

import io.reactivex.Observable
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        Observable.just(1, 2, 3, 4)
                .flatMap { t ->
                    if (t == 2) {
                        println(t)
                        Observable.just(2)
                    } else Observable.empty()
                }
    }
}
