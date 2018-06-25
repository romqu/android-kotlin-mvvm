package de.sevennerds.trackdefects

import com.google.common.truth.Truth
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import org.junit.Test
import java.util.concurrent.TimeUnit


class UnitTest {

    // the expected view state after emitting "text2"
    private val expectedViewState =
            TestView.State(text1 = "text1", text2 = "text2")

    @Test
    fun test1() {

        /*val longOperationObservable = Observable.fromCallable {
            println("Doing some heavy work")

            Thread.sleep(500)
            "done"
        }*/

        val longOperationObservable = Observable
                .interval(1, TimeUnit.SECONDS)
                .map { "emit" }

        longOperationObservable.doOnNext { println(it + 1)  }
                .test().dispose()

        longOperationObservable.doOnNext { println(it + 2)  }
                .test().await(10, TimeUnit.SECONDS)

        // longOperationObservable.connect()
    }

    @Test
    fun test() {

        /*"text1".asObservable()
                .map { TestView.Event.Event1(it) }
                .compose(eventToRenderStateTransformer)
                .test()
                .assertValue { renderState ->
                    render(renderState)
                    true
                }

        "text2".asObservable()
                .map { TestView.Event.Event2(it) }
                .compose(eventToRenderStateTransformer)
                .test()
                .assertValue { renderState ->
                    render(renderState)
                    true
                }*/

        val r = Observable.merge(Observable.fromCallable { "text1" }
                                          .map { TestView.Event.Event1(it) }
                                          .compose(eventToRenderStateTransformer),
                                 Observable.fromCallable { "text11" }
                                         .map { TestView.Event.Event1(it) }
                                         .compose(eventToRenderStateTransformer),
                                  Observable.fromCallable { "text2" }
                                          .map { TestView.Event.Event2(it) }
                                          .compose(eventToRenderStateTransformer)
        ).test().await()

        println(r)

    }

    private val eventToRenderStateTransformer =
            ObservableTransformer<TestView.Event, TestView.RenderState> { upstream ->

                upstream.publish { shared ->

                    Observable.merge(shared.ofType(TestView.Event.Event1::class.java)
                                             .compose(testEvent1ToRenderStateTransformer),
                                     shared.ofType(TestView.Event.Event2::class.java)
                                             .compose(testEvent2ToRenderStateTransformer))
                }
            }

    private val testEvent1ToRenderStateTransformer =
            ObservableTransformer<TestView.Event.Event1, TestView.RenderState> { upstream ->

                upstream.map { TestView.Result.Result1(it.text1) }
                        .compose(resultToViewStateTransformer)
                        .map { viewState ->
                            TestView.RenderState.RenderState1(viewState.text1)
                        }
            }

    private val testEvent2ToRenderStateTransformer =
            ObservableTransformer<TestView.Event.Event2, TestView.RenderState> { upstream ->

                upstream
                        .map { TestView.Result.Result2(it.text2) }
                        .compose(resultToViewStateTransformer)
                        .map { viewState ->
                            TestView.RenderState.RenderState2(viewState.text2)
                        }
            }


    /**
     * Generates a new ViewState from the result.
     * Expected is the following:
     *
     * 1. Initial:
     *      State(text1 = "", text2 = "") - but gets skipped -> skip(1)
     * 2. Event1:
     *      State(text1 = "text1", text2 = "")
     * 3. Event2:
     *      State(text1 = "text1", text2 = "text2")
     *
     * But 3. actually is:
     *      State(text1 = "", text2 = "text2")
     *
     * So the change to the ViewState from 2. is missing in the next call
     * And the only idea I have, is that one previousState exists for each Observable in the Observable.merge() above
     */
    private val resultToViewStateTransformer =
            ObservableTransformer<TestView.Result, TestView.State> { upstream ->

                upstream.scan(TestView.State.initial()) { previousState, result ->
                    when (result) {

                        is TestView.Result.Result1 -> {
                            println("State: $previousState")
                            previousState.copy(text1 = result.text1)
                        }


                        is TestView.Result.Result2 -> {

                            println("State: $previousState")

                            /*Truth.assertThat(previousState.copy(text2 = result.text2))

                                    .isEqualTo(expectedViewState)*/

                            previousState.copy(text2 = result.text2)
                        }
                    }
                }.skip(1)
            }


    private fun render(renderState: TestView.RenderState) {
        return when (renderState) {

            is TestView.RenderState.RenderState1 -> println("Render State 1: ${renderState.text1}")
            is TestView.RenderState.RenderState2 -> println("Render State 2: ${renderState.text2}")
        }
    }
}

class TestView {

    // The events generated by the ui (Fragment)
    sealed class Event {

        data class Event1(val text1: String) : Event()
        data class Event2(val text2: String) : Event()
    }

    /**
     * Result returned by the domain
     */
    sealed class Result {

        data class Result1(val text1: String) : Result()
        data class Result2(val text2: String) : Result()
    }

    /**
     * The "global" view state
     */
    data class State(val text1: String, val text2: String) {
        companion object {
            fun initial() = State("", "")
        }
    }

    /**
     * The states the view receives and uses to render its ui, hence RenderState
     */
    sealed class RenderState {
        data class RenderState1(val text1: String) : RenderState()
        data class RenderState2(val text2: String) : RenderState()

    }
}
