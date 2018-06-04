package de.sevennerds.trackdefects.presentation.select_contacts

import androidx.recyclerview.widget.DiffUtil
import de.sevennerds.trackdefects.common.applySchedulers
import de.sevennerds.trackdefects.presentation.select_contacts.list.ContactDiffCallback
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.rxkotlin.toObservable


class SelectContactsViewModel {


    sealed class State {
        data class SelectContactResult(val contactModelList: List<ContactModel>,
                                       val diffResult: DiffUtil.DiffResult) : State()

        data class RemoveContactResult(val contactModelList: List<ContactModel>,
                                       val diffResult: DiffUtil.DiffResult) : State()
    }


    val eventTransformer = ObservableTransformer<SelectContactsEvent, State> { observable ->
        observable.publish { shared ->
            Observable
                    .merge(shared.ofType(
                            SelectContactsEvent.SelectContactEvent::class.java)
                                   .compose(selectContactTransformer),
                           shared.ofType(
                                   SelectContactsEvent.RemoveContactEvent::class.java)
                                   .compose(removeContactTransformer))
        }
    }

    private val selectContactTransformer = ObservableTransformer<SelectContactsEvent.SelectContactEvent,
            State> { observable ->
        observable.flatMap { selectContactEvent ->

            val (contactResultList, currentContactModelList) = selectContactEvent

            contactResultList
                    .toObservable()
                    .map { contactResult ->
                        ContactModel(
                                contactResult.displayName,
                                contactResult.phoneNumbers.firstOrNull() ?: "",
                                contactResult.emails.firstOrNull() ?: "")
                    }
                    .toList()
                    .map { newContactModelList ->
                        State.SelectContactResult(
                                newContactModelList,
                                DiffUtil.calculateDiff(
                                        ContactDiffCallback(
                                                currentContactModelList,
                                                newContactModelList)))
                    }
                    .toObservable()
                    .applySchedulers()
        }

    }

    private val removeContactTransformer = ObservableTransformer<SelectContactsEvent.RemoveContactEvent,
            State> { observable ->
        observable.flatMap { removeContactEvent ->

            val (contactPosition, currentContactModelList) = removeContactEvent

            currentContactModelList
                    .toObservable()
                    .filter { contactModel ->
                        contactModel != currentContactModelList[contactPosition]
                    }
                    .toList()
                    .map { newContactModelList ->
                        State.RemoveContactResult(
                                newContactModelList,
                                DiffUtil.calculateDiff(
                                        ContactDiffCallback(currentContactModelList,
                                                            newContactModelList)))
                    }
                    .toObservable()
                    .applySchedulers()
        }
    }
}

