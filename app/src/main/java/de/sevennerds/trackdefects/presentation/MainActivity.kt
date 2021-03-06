package de.sevennerds.trackdefects.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhuinden.simplestack.BackstackDelegate
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.StateChanger
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import de.sevennerds.trackdefects.presentation.base.navigation.FragmentStateChanger
import de.sevennerds.trackdefects.presentation.feature.enter_street_address.navigation.EnterStreetAddressKey
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_multi_contact_picker.*


/**
 * Only responsibility right now is to manage the navigation
 * Single-Activity approach
 * Basically copy-pasted from here:
 * https://github.com/Zhuinden/simple-stack/blob/master/simple-stack-example-kotlin/src/main/kotlin/com/zhuinden/simplestackexamplekotlin/MainActivity.kt
 */
class MainActivity : AppCompatActivity(), StateChanger {


    private lateinit var backstackDelegate: BackstackDelegate
    private lateinit var fragmentStateChanger: FragmentStateChanger


    override fun onCreate(savedInstanceState: Bundle?) {

        backstackDelegate = BackstackDelegate(null)

        backstackDelegate.onCreate(savedInstanceState,
                                   lastCustomNonConfigurationInstance,
                                   History.single(EnterStreetAddressKey()))

        backstackDelegate.registerForLifecycleCallbacks(this);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TrackDefectsApp.get(this).appComponent.inject(this)

        fragmentStateChanger = FragmentStateChanger(supportFragmentManager, R.id.fragment_container)
        backstackDelegate.setStateChanger(this)

        setSupportActionBar(mainToolbar)
    }

    override fun onRetainCustomNonConfigurationInstance(): BackstackDelegate.NonConfigurationInstance =
            backstackDelegate.onRetainCustomNonConfigurationInstance()

    override fun onBackPressed() {

        if (!backstackDelegate.onBackPressed()) {
            super.onBackPressed()
        }
    }

    fun replaceHistory(rootKey: BaseKey) {
        backstackDelegate.backstack.setHistory(History.single(rootKey), StateChange.FORWARD)

    }

    fun replaceTopWith(key: BaseKey){
        backstackDelegate.backstack.replaceTop(key, StateChange.FORWARD)
    }

    fun navigateTo(key: BaseKey) {
        backstackDelegate.backstack.goTo(key)
    }

    override fun handleStateChange(stateChange: StateChange, completionCallback: StateChanger.Callback) {

        if (stateChange.topNewState<Any>() == stateChange.topPreviousState<Any>()) {
            completionCallback.stateChangeComplete()
            return
        }

        fragmentStateChanger.handleStateChange(stateChange)
        completionCallback.stateChangeComplete()
    }

    override fun getSystemService(name: String): Any? =
            when (name) {
                TAG -> this
                else -> super.getSystemService(name)
            }

    companion object {
        private const val TAG = "MainActivity"

        @SuppressLint("WrongConstant")
        operator fun get(context: Context): MainActivity {
            return context.getSystemService(TAG) as MainActivity
        }
    }

}



