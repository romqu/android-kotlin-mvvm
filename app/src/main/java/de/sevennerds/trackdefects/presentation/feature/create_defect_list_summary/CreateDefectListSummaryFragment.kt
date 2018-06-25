package de.sevennerds.trackdefects.presentation.feature.create_defect_list_summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_create_defect_list_summary.*

class CreateDefectListSummaryFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_create_defect_list_summary,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createDefectListSummaryStreetAddressExtEditTxt.isEnabled = false
    }
}