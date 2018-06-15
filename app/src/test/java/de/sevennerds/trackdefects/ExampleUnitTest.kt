package de.sevennerds.trackdefects

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    var viewState: TestView.State = TestView.State.initial()

    val eventTransformer = ObservableTransformer<TestView.Event, TestView.RenderState> { upstream ->

        /*Observable.merge(upstream.ofType(TestView.Event.Test::class.java)
                                 .compose(testEventTransformer),
                         upstream.ofType(TestView.Event.Test2::class.java)
                                 .compose(test2EventTransformer))*/

        upstream.publish { shared ->

            Observable.merge(shared.ofType(TestView.Event.Test::class.java)
                                     .compose(testEventTransformer),
                             shared.ofType(TestView.Event.Test2::class.java)
                                     .compose(test2EventTransformer))
        }
    }

    val testEventTransformer = ObservableTransformer<TestView.Event.Test, TestView.RenderState> { upstream ->

        upstream.map { TestView.Result.Test }
                .compose(resultTransformer)
                .map { viewState ->
                    TestView.RenderState.Test(viewState.text)
                }
    }

    val test2EventTransformer = ObservableTransformer<TestView.Event.Test2, TestView.RenderState> { upstream ->

        upstream
                .map { TestView.Result.Test }
                .compose(resultTransformer)
                .map { viewState ->
                    TestView.RenderState.Test2(viewState.text)
                }
    }

    val resultTransformer = ObservableTransformer<TestView.Result, TestView.State> { upstream ->

        upstream.scan(viewState) { _, result ->
            when (result) {

                is TestView.Result.Test -> viewState
                is TestView.Result.Test2 -> viewState
            }
        }.skip(1)
    }

    @Test
    fun addition_isCorrect() {

        Observable.just("text")
                .map { TestView.Event.Test }
                .compose(eventTransformer)
                .test().assertValue { t: TestView.RenderState ->
                    render(t)
                    true
                }

        Observable.just("text")
                .map { TestView.Event.Test2 }
                .compose(eventTransformer)
                .test().assertValue { t: TestView.RenderState ->
                    render(t)
                    true
                }


    }

    fun render(renderState: TestView.RenderState) {
        return when (renderState) {

            is TestView.RenderState.Test -> println("TEXT")
            is TestView.RenderState.Test2 -> println("TEXT2")
        }
    }


}

class TestView {

    sealed class Event {

        object Test : Event()
        object Test2 : Event()

    }

    /**
     * Result returned by the domain
     */
    sealed class Result {

        object Test : Result()
        object Test2 : Result()

    }

    /**
     * The "global" view state, kept in the ViewModel
     */
    data class State(val text: String) {
        companion object {
            fun initial() = State("")
        }
    }

    /**
     * The states the view receives and uses to render its ui, hence RenderState
     */
    sealed class RenderState {
        data class Test(val text: String) : RenderState()
        data class Test2(val text: String) : RenderState()

    }

}
