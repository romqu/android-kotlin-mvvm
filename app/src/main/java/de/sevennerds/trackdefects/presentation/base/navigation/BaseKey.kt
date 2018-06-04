package de.sevennerds.trackdefects.presentation.base.navigation

import android.os.Bundle
import android.os.Parcelable
import de.sevennerds.trackdefects.presentation.base.BaseFragment

abstract class BaseKey : Parcelable {
    val fragmentTag: String
        get() = toString()

    fun newFragment(): BaseFragment = createFragment().apply {
        arguments = (arguments ?: Bundle()).also { bundle ->
            bundle.putParcelable("KEY", this@BaseKey)
        }
    }

    protected abstract fun createFragment(): BaseFragment
}