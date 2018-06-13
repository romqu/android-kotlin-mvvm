package de.sevennerds.trackdefects.presentation.select_participants_defect_list

import androidx.recyclerview.widget.DiffUtil
import de.sevennerds.trackdefects.common.applySchedulers
import de.sevennerds.trackdefects.presentation.base.BaseDiffCallback
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.rxkotlin.toObservable


/**
 * The flow looks like this:
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
class SelectParticipantsViewModel(private var viewState: SelectParticipantsView.State) {


    /**
     * This transformer is used by the the view (here Fragment).
     * It takes an (ui) event and returns a (ui) render state.
     * For this to work it assigns an transformer to each Event and the merges it back.
     * I'm not entirely sure how it works ^^
     * It makes use of what was presented here: https://speakerdeck.com/jakewharton/the-state-of-managing-state-with-rxjava-devoxx-us-2017?slide=180
     */
    val eventTransformer = ObservableTransformer<SelectParticipantsView.Event,
            SelectParticipantsView.RenderState> { observable ->

        observable.publish { shared ->
            Observable
                    .merge(shared.ofType(
                            SelectParticipantsView.Event.Init::class.java)
                                   .compose(initTransformer),
                           shared.ofType(
                                   SelectParticipantsView.Event.Add::class.java)
                                   .compose(addParticipantsTransformer),
                           shared.ofType(
                                   SelectParticipantsView.Event.Remove::class.java)
                                   .compose(removeContactTransformer))
        }
    }


    /**
     * returns the initial render state, which only consists of a list
     */
    private val initTransformer = ObservableTransformer<SelectParticipantsView.Event.Init,
            SelectParticipantsView.RenderState> { upstream: Observable<SelectParticipantsView.Event.Init> ->

        upstream.map { SelectParticipantsView.Result.Init }
                .compose(resultTransformer)
                .map { SelectParticipantsView.RenderState.Init(it.participantModelList) }
    }


    /**
     * it receives the people selected from the contact picker plus the current view participant list,
     * maps them into a ParticipantModel, calculates the Diff with DiffUtil
     * and turn that into a Result (comes from the domain layer)
     * it then forwards it to the resultTransformer and uses the returned ViewState to form a RenderState for the ui
     */
    private val addParticipantsTransformer = ObservableTransformer<SelectParticipantsView.Event.Add,
            SelectParticipantsView.RenderState> { observable ->

        observable.flatMap { addParticipantsEvent ->

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
                    .map { newContactModelList ->
                        SelectParticipantsView.Result.Add(DiffUtil.calculateDiff(
                                BaseDiffCallback(
                                        currentViewParticipantList,
                                        newContactModelList)), newContactModelList)
                    }
                    // presentation
                    .compose(resultTransformer)
                    .map { viewState ->
                        SelectParticipantsView.RenderState.Add(
                                viewState.participantModelList,
                                viewState.diffResult!!)
                    }
                    .applySchedulers()
        }

    }

    /**
     * Pretty much same as above, only that it removes the selected view participant from the list
     */
    private val removeContactTransformer = ObservableTransformer<SelectParticipantsView.Event.Remove,
            SelectParticipantsView.RenderState> { observable ->

        observable.flatMap { removeContactEvent ->

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
                    .compose(resultTransformer)
                    .map { viewState ->
                        SelectParticipantsView.RenderState.Remove(
                                viewState.participantModelList,
                                viewState.diffResult!!)
                    }
                    .applySchedulers()
        }
    }


    /**
     * I use skip(1) because the Init case gets called anyway, so I would receive two "Init" states.
     * This makes the version of scan with a seed kinda useless tho. I just supplied one, because
     * "previousState" is then of type SelectParticipantsView.State, instead of SelectParticipantsView.Result.
     *
     */
    private val resultTransformer = ObservableTransformer<SelectParticipantsView.Result,
            SelectParticipantsView.State> { upstream: Observable<SelectParticipantsView.Result> ->

        upstream.scan(viewState) { previousState, result ->
            when (result) {

                is SelectParticipantsView.Result.Init -> viewState

                is SelectParticipantsView.Result.Add -> {
                    viewState = previousState.copy(participantModelList = result.participantModelList,
                                                   diffResult = result.diffResult)
                    viewState
                }

                is SelectParticipantsView.Result.Remove -> {
                    viewState = previousState.copy(participantModelList = result.participantModelList,
                                                   diffResult = result.diffResult)
                    viewState
                }
            }
        }.skip(1)
    }

    /**
     * The parcel version of the view state, because some (well, for now one) attributes are not needed
     * DiffResult in this case, which is not parcelable anyway.
     */
    fun getViewStateParcel(): SelectParticipantsView.StateParcel =
            SelectParticipantsView.StateParcel(viewState.participantModelList)

}

