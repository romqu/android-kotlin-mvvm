package de.sevennerds.trackdefects.common

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/*inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
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
}*/

fun View.showSnack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.show()
}

val Fragment.requireArguments
    get() = this.arguments ?: throw IllegalStateException("Arguments should exist!")

fun <T> Observable<T>.applySchedulers(): Observable<T> =
        compose { observable: Observable<T> ->
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }


fun <T> T.asObservable(): Observable<T> = Observable.just<T>(this)

fun <T> T.asSingle(): Single<T> = Single.fromCallable {
    this
}

fun <T> PublishSubject<T>.toObservable() =
        this.toFlowable(io.reactivex.BackpressureStrategy.BUFFER)
                .toObservable()!!

fun <T> BehaviorSubject<T>.toObservable() =
        this.toFlowable(io.reactivex.BackpressureStrategy.BUFFER)
                .toObservable()!!

fun Fragment.hideKeyboard() {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
}