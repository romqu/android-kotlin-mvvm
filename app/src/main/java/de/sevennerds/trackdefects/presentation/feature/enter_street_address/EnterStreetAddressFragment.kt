package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.textChanges
import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_enter_street_address.*
import javax.inject.Inject

class EnterStreetAddressFragment : BaseFragment() {

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var viewModel: EnterStreetAddressViewModel

    override fun onAttach(context: Context?) {

        TrackDefectsApp.get(context!!)
                .appComponent
                .inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_enter_street_address,
                container,
                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        compositeDisposable += enterStreetAddressStreetNameExtEditTxt
                .textChanges()
                .skipInitialValue()
                .map { EnterStreetAddressView.Event.StreetNameTextChange(it.toString()) }
                .compose(viewModel.eventToRenderState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Logger.d(it)
                    enterStreetAddressNextBtn.isEnabled = (it as EnterStreetAddressView.RenderState.SetButtonState).isEnabled
                }
    }

}