package de.sevennerds.trackdefects.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.common.addFragment
import de.sevennerds.trackdefects.data.client.Client
import de.sevennerds.trackdefects.data.client.ClientRepo
import de.sevennerds.trackdefects.data.client.LoginCredentials
import de.sevennerds.trackdefects.data.client.RegistrationData
import de.sevennerds.trackdefects.data.defect.DefectRepo
import de.sevennerds.trackdefects.data.defect_list.DefectListRepo
import de.sevennerds.trackdefects.data.floor.FloorRepo
import de.sevennerds.trackdefects.data.response.RegistrationResponse
import de.sevennerds.trackdefects.data.response.Result
import de.sevennerds.trackdefects.presentation.create_defect_list.CreateDefectListFragment
import de.sevennerds.trackdefects.presentation.select_contacts.SelectContactsFragment
import de.sevennerds.trackdefects.presentation.show_defect_list.ShowDefectListFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ir.mirrajabi.rxcontacts.Contact
import ir.mirrajabi.rxcontacts.RxContacts
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    val PICK_CONTACT = 2015

    val REQUEST_CODE_PICK_CONTACT = 1
    val MAX_PICK_CONTACT = 10

    @Inject
    lateinit var clientRepo: ClientRepo

    @Inject
    lateinit var defectListRepo: DefectListRepo

    @Inject
    lateinit var defectRepo: DefectRepo

    @Inject
    lateinit var floorRepo: FloorRepo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        addFragment(SelectContactsFragment.newInstance(),
                    R.id.fragment_container)



        TrackDefectsApp.get(this).component.inject(this)

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

        // testContact()
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

    fun testContact() {

        /*val i = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)

        startActivityForResult(i, PICK_CONTACT)*/

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


    }

}

// deleteDatabase("trackdefects.db")

/*        val r = defectListRepo.insertBasic(defectListEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response -> Log.d("RESPONSE", response.toString()) }

        val e = defectRepo.insert(DefectInsert(
                1,
                floorEntity,
                livingUnitEntity,
                roomEntity,
                defectInfoEntity,
                defectImageEntityList))
                .flatMap { defectResponse ->
                    when (defectResponse) {
                        is Result.Success -> defectRepo
                                .insertOrUpdate(defectResponse.data.floorEntity.copy(id = 0, name = "New"))
                        is Result.Failure -> Single.just(defectResponse)
                    }
                }.flatMap { floorRepo.insert(floorEntity.copy(id = 1)) }*/
/*.map { defectResponse ->
    when (defectResponse) {
        is Result.Success -> defectRepo
                .insertOrUpdate(defectResponse.data.floorEntity.copy(name = "CHANGED")).toString()
        is Result.Failure -> defectResponse.error
    }
}*/

/*        floorRepo.insert(floorEntity.copy(id = 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t1, t2 -> Log.d("RESPONSE1", t2?.toString() ?: "") }*/


// }



