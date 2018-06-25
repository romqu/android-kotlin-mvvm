package de.sevennerds.trackdefects.presentation.base

import androidx.fragment.app.Fragment
import de.sevennerds.trackdefects.common.requireArguments
import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey

open class BaseFragment : Fragment() {

    companion object {
        const val KEY_STATE_PARCEL = "KEY_STATE_PARCEL"
    }

    fun <T : BaseKey> getKey(): T = requireArguments.getParcelable("KEY")
}