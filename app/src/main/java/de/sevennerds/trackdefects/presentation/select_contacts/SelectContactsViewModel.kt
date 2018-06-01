package de.sevennerds.trackdefects.presentation.select_contacts

import androidx.recyclerview.widget.DiffUtil
import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.common.applySchedulers
import io.reactivex.ObservableTransformer
import io.reactivex.rxkotlin.toObservable


class SelectContactsViewModel {

    data class SelectContactResult(val contactModelList: List<ContactModel>,
                                   val diffResult: DiffUtil.DiffResult)

    val transformerContact = ObservableTransformer<SelectContactsFragment.SelectContactEvent,
            SelectContactResult> { observable ->
        observable.flatMap { selectContactEvent ->

            selectContactEvent
                    .contactResultList
                    .toObservable()
                    .map { contactResult ->
                        ContactModel(contactResult.displayName,
                                     contactResult.phoneNumbers.first(),
                                     contactResult.emails.first())
                    }
                    .toList()
                    .map { newContactModelList ->
                        SelectContactResult(newContactModelList,
                                            DiffUtil.calculateDiff(
                                                    ContactDiffCallback(
                                                            selectContactEvent
                                                                    .currentContactModelList,
                                                            newContactModelList)))
                    }
                    .toObservable()

        }.applySchedulers()
    }
}

