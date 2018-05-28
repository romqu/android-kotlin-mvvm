package de.sevennerds.trackdefects.presentation.select_contacts

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.orhanobut.logger.Logger
import com.wafflecopter.multicontactpicker.MultiContactPicker
import de.sevennerds.trackdefects.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_create_defect_list.*


class SelectContacts : Fragment() {

    private val contactBuilder =
            MultiContactPicker.Builder(this) //Activity/fragment context
                    .hideScrollbar(false) //Optional - default: false
                    .showTrack(true) //Optional - default: true
                    .searchIconColor(Color.WHITE) //Option - default: White
                    .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                    .bubbleTextColor(Color.WHITE) //Optional - default: White

    private val CONTACT_PICKER_REQUEST = 991
    private val compositeDisposable = CompositeDisposable()

    companion object {

        @JvmStatic
        fun newInstance(): SelectContacts {
            return SelectContacts()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_create_defect_list,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        compositeDisposable += button_create_defect_add_contact
                .clicks()
                .subscribe {
                    contactBuilder
                            .showPickerForResult(CONTACT_PICKER_REQUEST)
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTACT_PICKER_REQUEST) {
            when (resultCode) {
                RESULT_OK -> {
                    val results = MultiContactPicker.obtainResult(data)
                    Logger.d(results[0].displayName)
                }
                RESULT_CANCELED ->
                    Logger.d("User closed the picker without selecting items.")
            }
        }
    }
}