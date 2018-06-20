package de.sevennerds.trackdefects.presentation.enter_street_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.presentation.base.BaseFragment

class EnterStreetAddressFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_enter_street_address,
                                container,
                                false)
    }
}