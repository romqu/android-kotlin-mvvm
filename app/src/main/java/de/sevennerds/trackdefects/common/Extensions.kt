package de.sevennerds.trackdefects.common

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.hideAddFragment(fragmentToHide: Fragment, fragmentToAdd: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
        hide(fragmentToHide)
        add(frameId, fragmentToAdd)
    }
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.show()
}

val Fragment.requireArguments
    get() = this.arguments ?: throw IllegalStateException("Arguments should exist!")

fun <T> Observable<T>.applySchedulers(): Observable<T> =
        this.compose(
                { observable: Observable<T> ->
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                })


fun <T> T.asObservable(): Observable<T> = Observable.just<T>(this)