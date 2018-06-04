package de.sevennerds.trackdefects.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger
import com.zhuinden.simplestack.BackstackDelegate
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.StateChanger
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.common.addFragment
import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import de.sevennerds.trackdefects.presentation.base.navigation.FragmentStateChanger
import de.sevennerds.trackdefects.presentation.select_contacts.SelectContactsFragment
import de.sevennerds.trackdefects.presentation.select_contacts.navigation.SelectContactsKey


class MainActivity : AppCompatActivity(), StateChanger {

    val PICK_CONTACT = 2015

    val REQUEST_CODE_PICK_CONTACT = 1
    val MAX_PICK_CONTACT = 10

    private lateinit var backstackDelegate: BackstackDelegate
    private lateinit var fragmentStateChanger: FragmentStateChanger


    override fun onCreate(savedInstanceState: Bundle?) {

        backstackDelegate = BackstackDelegate(null)
        backstackDelegate.onCreate(savedInstanceState,
                                   lastCustomNonConfigurationInstance,
                                   History.single(SelectContactsKey()))
        backstackDelegate.registerForLifecycleCallbacks(this);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TrackDefectsApp.get(this).component.inject(this)

        fragmentStateChanger = FragmentStateChanger(supportFragmentManager, R.id.fragment_container)
        backstackDelegate.setStateChanger(this)
    }

    override fun onRetainCustomNonConfigurationInstance(): BackstackDelegate.NonConfigurationInstance =
            backstackDelegate.onRetainCustomNonConfigurationInstance()

    override fun onBackPressed() {

        if (!backstackDelegate.onBackPressed()) {
            super.onBackPressed()
        }
    }

    private fun replaceHistory(rootKey: BaseKey) {
        backstackDelegate.backstack.setHistory(History.single(rootKey), StateChange.REPLACE)
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

    // share activity through context
    override fun getSystemService(name: String): Any? = when (name) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /*if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            val contactUri = data!!.data
            val cursor = contentResolver.query(contactUri!!, null, null, null, null)
            cursor!!.moveToFirst()
            val column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            Log.d("phone number", cursor.getString(column))
            cursor.close()
        }*/

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CODE_PICK_CONTACT) {

                val bundle = data!!.getExtras()

                val result = bundle.getString("result")
                val contacts = bundle.getStringArrayList("result")


                Log.d("TAG", "launchMultiplePhonePicker bundle.toString()= " + contacts.toString())

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    // ---------------------------------------------------------------------------------------------
    /*fun testContact() {

        *//*val i = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)

        startActivityForResult(i, PICK_CONTACT)*//*

        RxContacts.fetch(this)
                .toSortedList(Contact::compareTo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ contact -> Log.d("Contact", contact.toString()) })
    }

    fun testRetrofit() {

        clientRepo
                .register(RegistrationData(
                        LoginCredentials("test@test4.de", "qwertzuiop"),
                        Client("Bern", "Berni")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t: Result<RegistrationResponse> -> Log.d("TAG", t.toString()) }


    }*/

    fun imageDraw() {

        /*val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.mangel)
                .copy(Bitmap.Config.ARGB_8888, true)

        val canvas = Canvas(imageBitmap)

        drawImageViewButton.setOnClickListener {

            canvas.drawPath(drawImageView.path, drawImageView.paint)

            drawImageView.setImageBitmap(imageBitmap)

        }*/

/*        val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.mangel)
                .copy(Bitmap.Config.ARGB_8888, true)

        val bitmap = Bitmap.createBitmap(
                imageBitmap.width, // Width
                imageBitmap.height, // Height
                Bitmap.Config.ARGB_8888 // Config
        )

        // Log.d("Tag", imageBitmap.width.toString())



        val canvas = Canvas(imageBitmap)

        val paint = Paint()
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.RED
        paint.strokeWidth = 25F
        paint.isAntiAlias = true

        // canvas.drawText("HELLO", 100F, 10F, paint)

        canvas.drawText("helloaaaaaaaaaaaaaaaaaaaaaaa", 0F, 600F, paint)*/

        // drawImageView.setImageDrawable(getDrawable(R.drawable.mangel))

    }

}



