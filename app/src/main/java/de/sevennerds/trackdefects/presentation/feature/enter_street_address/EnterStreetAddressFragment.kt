package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.orhanobut.logger.Logger
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.common.asObservable
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
    private var state: EnterStreetAddressView.StateParcel? = null
    private var isRotation = false

    override fun onAttach(context: Context?) {

        TrackDefectsApp.get(context!!)
                .appComponent
                .inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val stateParcel = savedInstanceState?.getParcelable<EnterStreetAddressView.StateParcel>("KEY")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        isRotation = false

        return inflater.inflate(R.layout.fragment_enter_street_address,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        enterStreetAddressNextBtn.isEnabled = false

        setupEvents()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("KEY", state)
    }

    override fun onPause() {
        super.onPause()

        isRotation = true

        val renderState = EnterStreetAddressView.Event.Restart
                .asObservable()
                .compose(viewModel.eventToRenderState)
                .blockingLast()

        render(renderState)

        compositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()

        if (isRotation) {
            setupEvents()
            isRotation = false
        }
    }

    private fun setupEvents() {

        compositeDisposable += enterStreetAddressStreetNameExtEditTxt
                .textChanges()
                .skipInitialValue()
                .map { EnterStreetAddressView.Event.StreetNameTextChange(it.toString()) }
                .compose(viewModel.eventToRenderState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::render)

        compositeDisposable += enterStreetAddressNumberExtEditTxt
                .textChanges()
                .skipInitialValue()
                .map { EnterStreetAddressView.Event.StreetNumberTextChange(it.toString()) }
                .compose(viewModel.eventToRenderState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::render)

        compositeDisposable += enterStreetAddressAdditionalExtEditTxt
                .textChanges()
                .skipInitialValue()
                .map { EnterStreetAddressView.Event.StreetAdditionalTextChange(it.toString()) }
                .compose(viewModel.eventToRenderState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::render)



        compositeDisposable += enterStreetAddressNextBtn
                .clicks()
                .subscribe {}
    }


    private fun render(renderState: EnterStreetAddressView.RenderState) =

            when (renderState) {

                is EnterStreetAddressView.RenderState.SetButtonState ->
                    enterStreetAddressNextBtn.isEnabled = renderState.isEnabled

                is EnterStreetAddressView.RenderState.Nothing -> {
                }

                is EnterStreetAddressView.RenderState.Restart ->
                    state = renderState.stateParcel
            }


/*    private fun requestRuntimePermissions() {

        compositeDisposable += TedRx2Permission.with(context)
                .setRationaleTitle("Permissions")
                .setRationaleMessage("WE NEED 'EM!")
                .setPermissions(Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA)
                .request()
                .subscribe({ tedPermissionResult ->
                               if (tedPermissionResult.isGranted) {
                                   Toast.makeText(context,
                                                  "Permission Granted",
                                                  Toast.LENGTH_SHORT).show()
                               } else {
                                   Toast.makeText(context,
                                                  "Permission Denied\n" +
                                                          tedPermissionResult.deniedPermissions.toString(),
                                                  Toast.LENGTH_SHORT)
                                           .show()
                               }
                           }, { throwable -> Logger.e("$throwable") })
    }*/
}