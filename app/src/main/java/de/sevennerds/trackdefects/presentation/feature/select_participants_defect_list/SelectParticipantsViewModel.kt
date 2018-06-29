package de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list

import androidx.recyclerview.widget.DiffUtil
import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.presentation.base.BaseDiffCallback
import de.sevennerds.trackdefects.presentation.base.BaseViewModel
import de.sevennerds.trackdefects.presentation.realm_db.CreateBasicDefectListSummaryRealm
import de.sevennerds.trackdefects.presentation.realm_db.RealmManager
import de.sevennerds.trackdefects.presentation.realm_db.ViewParticipantRealm
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import io.realm.RealmList
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


class SelectParticipantsViewModel @Inject constructor() :
        BaseViewModel<SelectParticipantsView.Event, SelectParticipantsView.State>() {

    /**
     * This transformer is used by the the view (here Fragment).
     * It takes an (ui) event and returns a (ui) render state.
     * For this to work it assigns an transformer to each Event and the merges it back.
     * I'm not entirely sure how it works ^^
     * It makes use of what was presented here: https://speakerdeck.com/jakewharton/the-state-of-managing-state-with-rxjava-devoxx-us-2017?slide=180
     */
    override val eventToViewState = ObservableTransformer<SelectParticipantsView.Event,
            SelectParticipantsView.State> { upstream ->

        upstream
                .observeOn(Schedulers.io())
                .publish { shared ->
                    Observable
                            .mergeArray(
                                    shared.ofType(
                                            SelectParticipantsView.Event.Init::class.java)
                                            .compose(eventInitToResult),
                                    shared.ofType(
                                            SelectParticipantsView.Event.Add::class.java)
                                            .compose(eventAddParticipantsToResult),
                                    shared.ofType(
                                            SelectParticipantsView.Event.Remove::class.java)
                                            .compose(eventRemoveParticipantToResult),
                                    shared.ofType(
                                            SelectParticipantsView.Event.ShowContacts::class.java)
                                            .compose(eventShowContactsToResult),
                                    shared.ofType(
                                            SelectParticipantsView.Event.Next::class.java)
                                            .compose(eventNextToResult))
                            .compose(resultToViewState)

                }
    }

    private val eventInitToResult = ObservableTransformer<SelectParticipantsView.Event.Init,
            SelectParticipantsView.Result> { upstream: Observable<SelectParticipantsView.Event.Init> ->

        upstream.map { SelectParticipantsView.Result.Init(it.parcelState) }
    }

    private val eventShowContactsToResult = ObservableTransformer<SelectParticipantsView.Event.ShowContacts,
            SelectParticipantsView.Result> { upstream ->

        upstream.map { SelectParticipantsView.Result.ShowContacts }
    }

    private val eventNextToResult = ObservableTransformer<SelectParticipantsView.Event.Next,
            SelectParticipantsView.Result> { upstream ->

        upstream.map {
            SelectParticipantsView.Result.Next
        }
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

                is SelectParticipantsView.Result.ShowContacts ->
                    previousState.copy(renderState = SelectParticipantsView.RenderState.ShowContacts)

                is SelectParticipantsView.Result.Init -> {

                    val state = previousState.copy(participantModelList = result.parcelState.participantModelList)

                    state.copy(renderState = SelectParticipantsView.RenderState.Init(convertViewToParcelState(state)))
                }

                is SelectParticipantsView.Result.Add -> {

                    val state = previousState.copy(participantModelList = result.participantModelList,
                                                   nextOrSkipButtonText = "Next")

                    state.copy(renderState = SelectParticipantsView
                            .RenderState.Add(result.diffResult,
                                             convertViewToParcelState(state)))
                }

                is SelectParticipantsView.Result.Remove -> {

                    val state = previousState.copy(participantModelList = result.participantModelList)

                    state.copy(renderState = SelectParticipantsView
                            .RenderState.Remove(result.diffResult,
                                                convertViewToParcelState(state)))
                }

                is SelectParticipantsView.Result.Next -> {

                    // TODO impure
                    updateSharedRealmObject(previousState)

                    previousState.copy(renderState = SelectParticipantsView.RenderState.Next)
                }
            }
        }.skip(1)
    }

    private fun convertViewToParcelState(viewState: SelectParticipantsView.State) =
            SelectParticipantsView.ParcelState(viewState.participantModelList)

    // TODO impure
    private fun updateSharedRealmObject(viewState: SelectParticipantsView.State) {

        val participantRealmList = viewState.participantModelList
                .map {
                    ViewParticipantRealm(name = it.name,
                                         phoneNumber = it.phoneNumber,
                                         email = it.email)
                }
                .toList()

        RealmManager.insertOrUpdate(
                CreateBasicDefectListSummaryRealm(
                        viewParticipantRealmList = RealmList(*participantRealmList.toTypedArray())))
    }
}

