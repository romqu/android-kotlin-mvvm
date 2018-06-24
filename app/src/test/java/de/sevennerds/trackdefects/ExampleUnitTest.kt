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

    private val eventTransformer = ObservableTransformer<TestView.Event, TestView.RenderState> { upstream ->

        upstream.publish { shared ->

            Observable.merge(shared.ofType(TestView.Event.Test::class.java)
                                     .compose(testEventTransformer),
                             shared.ofType(TestView.Event.Test2::class.java)
                                     .compose(test2EventTransformer))
        }
    }

    private val testEventTransformer = ObservableTransformer<TestView.Event.Test, TestView.RenderState> { upstream ->

        upstream.map { TestView.Result.Test(it.text) }
                .compose(resultTransformer)
                .map { viewState ->
                    TestView.RenderState.Test(viewState.text)
                }
    }

    private val test2EventTransformer = ObservableTransformer<TestView.Event.Test2, TestView.RenderState> { upstream ->

        upstream
                .map { TestView.Result.Test2(it.text2) }
                .compose(resultTransformer)
                .map { viewState ->
                    TestView.RenderState.Test2(viewState.text)
                }
    }

    private val resultTransformer = ObservableTransformer<TestView.Result, TestView.State> { upstream ->

        upstream.scan(TestView.State.initial()) { previousState, result ->
            when (result) {

                is TestView.Result.Test -> previousState.copy(text = result.text)
                is TestView.Result.Test2 -> previousState.copy(text2 = result.text2)
            }
        }
                .skip(1)
                .doOnNext { println(it) }
    }

    @Test
    fun addition_isCorrect() {

        Observable.just("text")
                .map { TestView.Event.Test(it) }
                .compose(eventTransformer)
                .test()
                .assertValue { t: TestView.RenderState ->
                    render(t)
                    true
                }

        Observable.just("text2")
                .map { TestView.Event.Test2(it) }
                .compose(eventTransformer)
                .test()
                .assertValue { t: TestView.RenderState ->
                    render(t)
                    true
                }
    }

    private fun render(renderState: TestView.RenderState) {
        return when (renderState) {

            is TestView.RenderState.Test -> println("TEXT")
            is TestView.RenderState.Test2 -> println("TEXT2")
        }
    }
}

class TestView {

    sealed class Event {

        data class Test(val text: String) : Event()
        data class Test2(val text2: String) : Event()

    }

    /**
     * Result returned by the domain
     */
    sealed class Result {

        data class Test(val text: String) : Result()
        data class Test2(val text2: String) : Result()

    }

    /**
     * The "global" view state
     */
    data class State(val text: String, val text2: String) {
        companion object {
            fun initial() = State("", "")
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
