package de.sevennerds.trackdefects.presentation

import android.graphics.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.TrackDefectsApp
import de.sevennerds.trackdefects.data.client.Client
import de.sevennerds.trackdefects.data.client.ClientRepo
import de.sevennerds.trackdefects.data.defect.DefectRepo
import de.sevennerds.trackdefects.data.defect_list.DefectListRepo
import de.sevennerds.trackdefects.data.floor.FloorRepo
import de.sevennerds.trackdefects.data.client.LoginCredentials
import de.sevennerds.trackdefects.data.client.RegistrationData
import de.sevennerds.trackdefects.data.response.LoginResponse
import de.sevennerds.trackdefects.data.response.RegistrationResponse
import de.sevennerds.trackdefects.data.response.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.lay.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.lay)

        TrackDefectsApp.get(this).component.inject(this)

        val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.mangel)
                .copy(Bitmap.Config.ARGB_8888, true)

        val canvas = Canvas(imageBitmap)

        drawImageViewButton.setOnClickListener {

            canvas.drawPath(drawImageView.path, drawImageView.paint)

            drawImageView.setImageBitmap(imageBitmap)

        }

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

        drawImageView.setImageDrawable(getDrawable(R.drawable.mangel))

        testRetrofit()
    }

    fun testRetrofit() {

        clientRepo
                .register(RegistrationData(
                        LoginCredentials("test@test4.de", "qwertzuiop"),
                        Client("Bern", "Berni")) )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t: Result<RegistrationResponse> -> Log.d("TAG", t.toString()) }


    }

}

// deleteDatabase("trackdefects.db")

/*        val r = defectListRepo.insertBasic(defectList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response -> Log.d("RESPONSE", response.toString()) }

        val e = defectRepo.insert(DefectInsert(
                1,
                floor,
                livingUnit,
                room,
                defectInfo,
                defectImageList))
                .flatMap { defectResponse ->
                    when (defectResponse) {
                        is Result.Success -> defectRepo
                                .insertOrUpdate(defectResponse.data.floor.copy(id = 0, name = "New"))
                        is Result.Failure -> Single.just(defectResponse)
                    }
                }.flatMap { floorRepo.insert(floor.copy(id = 1)) }*/
/*.map { defectResponse ->
    when (defectResponse) {
        is Result.Success -> defectRepo
                .insertOrUpdate(defectResponse.data.floor.copy(name = "CHANGED")).toString()
        is Result.Failure -> defectResponse.error
    }
}*/

/*        floorRepo.insert(floor.copy(id = 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t1, t2 -> Log.d("RESPONSE1", t2?.toString() ?: "") }*/


// }



