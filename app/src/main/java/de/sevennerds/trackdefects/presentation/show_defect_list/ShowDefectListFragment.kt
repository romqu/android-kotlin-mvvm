package de.sevennerds.trackdefects.presentation.show_defect_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.common.snack
import kotlinx.android.synthetic.main.fragment_create_defect_list.*


class CreateDefectListFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance(): CreateDefectListFragment {
            return CreateDefectListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_create_defect_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        create_defect_list_fab.setOnClickListener { view?.snack("Hello")}
    }
}

