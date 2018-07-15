package de.sevennerds.trackdefects.presentation.feature.enter_street_address

import android.Manifest
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.afterTextChangeEvents
import com.orhanobut.logger.Logger
import com.tedpark.tedpermission.rx2.TedRx2Permission
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.common.hideKeyboard
import de.sevennerds.trackdefects.common.toObservable
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import de.sevennerds.trackdefects.presentation.feature.select_participants_defect_list.navigation.SelectParticipantsKey
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_enter_street_address.*
import javax.inject.Inject


class EnterStreetAddressFragment : BaseFragment() {

    private val compositeDisposable = CompositeDisposable()
    private val evenInitSubject =
            BehaviorSubject.create<EnterStreetAddressView.Event.Init>()

    @Inject
    lateinit var viewModel: EnterStreetAddressViewModel

    private var parcelState: EnterStreetAddressView.ParcelState? = null
    private var isRotation = false


    override fun onAttach(context: Context?) {

        TrackDefectsApp.get(context!!)
                .appComponent
                .inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            parcelState = it.getParcelable(KEY_STATE_PARCEL)

        }

        evenInitSubject.onNext(EnterStreetAddressView.Event.Init(parcelState))
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

        enterStreetAddressNextFab.alpha = 0.3F

        requestRuntimePermissions()

        setup()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(KEY_STATE_PARCEL, parcelState)
    }

    override fun onPause() {
        super.onPause()

        isRotation = true

        compositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()

        if (isRotation) {
            setupEvents()
            isRotation = false
            evenInitSubject.onNext(EnterStreetAddressView.Event.Init(parcelState))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }


    //----------------------------------------------------------------------------------------------

    private fun setup() {
        setupActionBar()
        setupEvents()
    }

    private fun setupActionBar() {
        MainActivity[requireContext()].supportActionBar?.title = "Street Address"
        MainActivity[requireContext()].supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    private fun setupEvents() {

        compositeDisposable += Observable
                .mergeArray(evenInitSubject
                                    .toObservable(),
                            enterStreetAddressStreetNameExtEditTxt
                                    .afterTextChangeEvents()
                                    .skipInitialValue()
                                    .map {
                                        EnterStreetAddressView.Event
                                                .StreetNameTextChange(it.view().text.toString())
                                    },
                            enterStreetAddressNumberExtEditTxt
                                    .afterTextChangeEvents()
                                    .skipInitialValue()
                                    .map {
                                        EnterStreetAddressView.Event
                                                .StreetNumberTextChange(it.view().text.toString())
                                    },
                            enterStreetAddressAdditionalExtEditTxt
                                    .afterTextChangeEvents()
                                    .skipInitialValue()
                                    .map {
                                        EnterStreetAddressView.Event
                                                .StreetAdditionalTextChange(it.view().text.toString())
                                    },
                            enterStreetAddressNextFab
                                    .clicks()
                                    .map { EnterStreetAddressView.Event.Next }
                )
                .compose(viewModel.eventToRenderState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::render)
    }


    private fun render(viewState: EnterStreetAddressView.State) =

            when (viewState.renderState) {

                is EnterStreetAddressView.RenderState.Init -> {

                    with(viewState) {
                        enterStreetAddressStreetNameExtEditTxt.setText(streetName)
                        enterStreetAddressNumberExtEditTxt.setText(streetNumber)
                        enterStreetAddressAdditionalExtEditTxt.setText(streetAdditional)
                        enterStreetAddressNextFab.isEnabled = isButtonEnabled
                    }
                }

                is EnterStreetAddressView.RenderState.SetButtonState -> {

                    updateStateParcel(viewState.renderState.parcelState)

                    if (viewState.isButtonEnabled) {
                        enterStreetAddressNextFab.alpha = 1F
                        /*enterStreetAddressNextFab.backgroundTintList =
                                ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.colorAccent))*/
                    } else {
                        enterStreetAddressNextFab.alpha = 0.3F
                        /*enterStreetAddressNextFab.backgroundTintList =
                                ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.material_grey_300))*/

                    }

                    enterStreetAddressNextFab.isEnabled = viewState.isButtonEnabled
                }

                is EnterStreetAddressView.RenderState.Next -> {

                    MainActivity[requireContext()]
                            .navigateTo(SelectParticipantsKey())
                }

                is EnterStreetAddressView.RenderState.UpdateStateParcel ->
                    updateStateParcel(viewState.renderState.parcelState)

                is EnterStreetAddressView.RenderState.None -> {
                }
            }

    private fun updateStateParcel(parcelState: EnterStreetAddressView.ParcelState) {
        this.parcelState = parcelState
    }


    private fun requestRuntimePermissions() {

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
    }
}