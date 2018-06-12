package de.sevennerds.trackdefects.presentation.select_participants_defect_list

import androidx.recyclerview.widget.DiffUtil
import de.sevennerds.trackdefects.common.applySchedulers
import de.sevennerds.trackdefects.presentation.select_participants_defect_list.list.ParticipantDiffCallback
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.rxkotlin.toObservable


class SelectContactsViewModel(var viewState: ViewState) {


    val eventTransformer = ObservableTransformer<SelectContactsEvent, ViewRenderState> { observable ->
        observable.publish { shared ->
            Observable
                    .merge(shared.ofType(
                            SelectContactsEvent.Init::class.java)
                                   .compose(initTransformer),
                           shared.ofType(
                                   SelectContactsEvent.SelectContactEvent::class.java)
                                   .compose(selectContactTransformer),
                           shared.ofType(
                                   SelectContactsEvent.RemoveContactEvent::class.java)
                                   .compose(removeContactTransformer))
        }


    }

    private val initTransformer = ObservableTransformer<SelectContactsEvent.Init,
            ViewRenderState> { upstream: Observable<SelectContactsEvent.Init> ->
        upstream.map { ViewResult.Init }
                .compose(resultTransformer)
                //.doOnNext { Logger.d("AAAA") }
                .map { ViewRenderState.Init(it.participantModelList) }
    }

    private val selectContactTransformer = ObservableTransformer<SelectContactsEvent.SelectContactEvent,
            ViewRenderState> { observable ->
        observable.flatMap { selectContactEvent ->

            val (contactResultList, currentContactModelList) = selectContactEvent


            contactResultList
                    .toObservable()
                    // domain
                    .map { contactResult ->
                        ParticipantModel(
                                contactResult.displayName,
                                contactResult.phoneNumbers.firstOrNull() ?: "",
                                contactResult.emails.firstOrNull() ?: "")
                    }
                    .toList()
                    .toObservable()
                    .map { newContactModelList ->
                        ViewResult.SelectContact(DiffUtil.calculateDiff(
                                ParticipantDiffCallback(
                                        currentContactModelList,
                                        newContactModelList)), newContactModelList)
                    }
                    // presentation
                    .compose(resultTransformer)
                    .skipWhile { it.diffResult == null }
                    //.skip(1)
                    .map { viewState ->
                        ViewRenderState.SelectContact(
                                viewState.participantModelList,
                                viewState.diffResult!!)
                    }
                    .applySchedulers()
        }

    }

    private val removeContactTransformer = ObservableTransformer<SelectContactsEvent.RemoveContactEvent,
            ViewRenderState> { observable ->
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
                        ViewResult.RemoveContact(
                                newContactModelList,
                                DiffUtil.calculateDiff(
                                        ParticipantDiffCallback(currentContactModelList,
                                                                newContactModelList)))
                    }
                    .compose(resultTransformer)
                    .skip(1)
                    .map { viewState ->
                        ViewRenderState.RemoveContact(
                                viewState.participantModelList,
                                viewState.diffResult!!)
                    }
                    .applySchedulers()
        }
    }

    private val resultTransformer = ObservableTransformer<ViewResult,
            ViewState> { upstream: Observable<ViewResult> ->

        upstream.scan(viewState) { previousState, result ->
            when (result) {

                is ViewResult.Init -> viewState

                is ViewResult.SelectContact -> {
                    viewState = previousState.copy(participantModelList = result.participantModelList,
                                                   diffResult = result.diffResult)
                    viewState
                }

                is ViewResult.RemoveContact -> {
                    viewState = previousState.copy(participantModelList = result.participantModelList,
                                                   diffResult = result.diffResult)
                    viewState
                }
            }
        }
    }

    fun getViewStateP(): ViewStateP =
            ViewStateP(viewState.participantModelList)

}

