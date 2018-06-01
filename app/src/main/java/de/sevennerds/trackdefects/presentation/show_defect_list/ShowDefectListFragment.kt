package de.sevennerds.trackdefects.presentation.show_defect_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.common.addFragment
import de.sevennerds.trackdefects.common.hideAddFragment
import de.sevennerds.trackdefects.common.snack
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.MainActivity_MembersInjector
import de.sevennerds.trackdefects.presentation.create_defect_list.CreateDefectListFragment
import kotlinx.android.synthetic.main.fragment_show_defect_list.*


class ShowDefectListFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance(): ShowDefectListFragment {
            return ShowDefectListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_show_defect_list,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        create_defect_list_fab.setOnClickListener {

            (activity as MainActivity).hideAddFragment(this,
                                                       CreateDefectListFragment.newInstance(),
                                                       R.id.fragment_container)
        }
    }
}

