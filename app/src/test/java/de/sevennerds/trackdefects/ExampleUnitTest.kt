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
                .scan(10) { t1: Int, t2: Int ->
                    println("$t1 + $t2")
                    t1 + t2
                }
                .startWith(1)
                .doOnNext { println("Next: $it") }
                .test()
        //.assertResult(2);
    }
}
