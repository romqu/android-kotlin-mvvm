package de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list

import androidx.recyclerview.widget.DiffUtil
import de.sevennerds.trackdefects.common.applySchedulers
import de.sevennerds.trackdefects.presentation.base.BaseDiffCallback
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.rxkotlin.toObservable
import javax.inject.Inject


/**
 * Presentation: https://speakerdeck.com/jakewharton/the-state-of-managing-state-with-rxjava-devoxx-us-2017?slide=1
 * Reddit comments : https://www.reddit.com/r/androiddev/comments/656ter/managing_state_with_rxjava_by_jake_wharton/
 * More comments: https://www.reddit.com/r/androiddev/comments/7gmge6/representing_view_state_with_kotlin_data_classes/
 *
 * Why two States? -> https://www.reddit.com/r/androiddev/comments/7u7vci/modeling_viewmodel_states_using_kotlins_sealed/
 *
 * The flow roughly looks like this:
 * UI creates an Event (or Intent) and forwards it to the ViewModel
 * -> ViewModel transforms that into a Request (or Action - does not exists as an object as of now, but will as soon as the domain gets implemented)
 *    and calls the domain (UseCase, Interactor, Task, whatever)
 * -> Domain does its work and provides the ViewModel with a Result
 * -> ViewModel turns that into a ViewState
 * -> ViewModel creates an RenderViewState out of it, which the
 * -> View then uses to render its ui
 *
 * "viewState" is the global State, it's mutable. But only the variable itself, not the data it holds.
 * It gets never exposed to the view, only a parcelable version of it.
 */

abstract class ViewModel<EVENT, STATE> {
    abstract val eventToRenderState: ObservableTransformer<EVENT, STATE>
}

class SelectParticipantsViewModel @Inject constructor() :
        ViewModel<SelectParticipantsView.Event, SelectParticipantsView.State>() {

    /**
     * This transformer is used by the the view (here Fragment).
     * It takes an (ui) event and returns a (ui) render state.
     * For this to work it assigns an transformer to each Event and the merges it back.
     * I'm not entirely sure how it works ^^
     * It makes use of what was presented here: https://speakerdeck.com/jakewharton/the-state-of-managing-state-with-rxjava-devoxx-us-2017?slide=180
     */
    override val eventToRenderState = ObservableTransformer<SelectParticipantsView.Event,
            SelectParticipantsView.State> { observable ->

        observable.publish { shared ->
            Observable
                    .merge(shared.ofType(
                            SelectParticipantsView.Event.Init::class.java)
                                   .compose(eventInitToResult),
                           shared.ofType(
                                   SelectParticipantsView.Event.Add::class.java)
                                   .compose(eventAddParticipantsToResult),
                           shared.ofType(
                                   SelectParticipantsView.Event.Remove::class.java)
                                   .compose(eventRemoveParticipantToResult))
                    .compose(resultToViewState)
        }
    }

    private val eventInitToResult = ObservableTransformer<SelectParticipantsView.Event.Init,
            SelectParticipantsView.Result> { upstream: Observable<SelectParticipantsView.Event.Init> ->

        upstream.map { SelectParticipantsView.Result.Init(it.stateParcel) }
    }

    private val eventAddParticipantsToResult = ObservableTransformer<SelectParticipantsView.Event.Add,
            SelectParticipantsView.Result> { upstream ->

        upstream.flatMap { addParticipantsEvent ->

            val (contactResultList, currentViewParticipantList) = addParticipantsEvent

            contactResultList
                    // domain layer
                    .toObservable()
                    .map { contactResult ->
                        ParticipantModel(
                                contactResult.displayName,
                                contactResult.phoneNumbers.firstOrNull() ?: "",
                                contactResult.emails.firstOrNull() ?: "")
                    }
                    .toList()
                    .toObservable()
                    // TODO: DO IT WITH WITH RX JAVA
                    .map { participantModelList ->
                        participantModelList
                                .union(currentViewParticipantList)
                                .distinctBy { it.phoneNumber }
                                .toList()
                    }
                    .map { newParticipantModelList ->
                        SelectParticipantsView.Result.Add(DiffUtil.calculateDiff(
                                BaseDiffCallback(
                                        currentViewParticipantList,
                                        newParticipantModelList)), newParticipantModelList)
                    }
        }
    }

    private val eventRemoveParticipantToResult = ObservableTransformer<SelectParticipantsView.Event.Remove,
            SelectParticipantsView.Result> { upstream ->

        upstream.flatMap { removeContactEvent ->

            val (contactPosition, currentContactModelList) = removeContactEvent

            currentContactModelList
                    .toObservable()
                    // domain
                    .filter { contactModel ->
                        contactModel != currentContactModelList[contactPosition]
                    }
                    .toList()
                    .toObservable()
                    .map { newContactModelList ->
                        SelectParticipantsView.Result.Remove(
                                newContactModelList,
                                DiffUtil.calculateDiff(
                                        BaseDiffCallback(currentContactModelList,
                                                         newContactModelList)))
                    }
        }
    }


    /**
     * I use skip(1) because the Init case gets called anyway, so I would receive two "Init" states.
     * This makes the version of scan with a seed kinda useless tho. I just supplied one, because
     * "previousState" is then of type SelectParticipantsView.State, instead of SelectParticipantsView.Result
     */
    private val resultToViewState = ObservableTransformer<SelectParticipantsView.Result,
            SelectParticipantsView.State> { upstream ->

        upstream.scan(SelectParticipantsView.State.initial()) { previousState,
                                                                result ->

            when (result) {

                is SelectParticipantsView.Result.Init -> {

                    previousState.copy(participantModelList = result.stateParcel.participantModelList,
                                       renderState = SelectParticipantsView.RenderState.Init)
                }


                is SelectParticipantsView.Result.Add -> {
                    previousState.copy(participantModelList = result.participantModelList,
                                       renderState = SelectParticipantsView.RenderState.Add(result.diffResult))
                }

                is SelectParticipantsView.Result.Remove -> {
                    previousState.copy(participantModelList = result.participantModelList,
                                       renderState = SelectParticipantsView.RenderState.Remove(result.diffResult))
                }
            }
        }.skip(1)
    }

}

