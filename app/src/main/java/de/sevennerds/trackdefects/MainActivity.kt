package de.sevennerds.trackdefects

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import de.sevennerds.trackdefects.data.defect_list.DefectList
import de.sevennerds.trackdefects.data.defect_list.DefectListRepo
import de.sevennerds.trackdefects.data.street_address.StreetAddress
import de.sevennerds.trackdefects.data.view_participant.ViewParticipant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repo: DefectListRepo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TrackDefectsApp.get(this).component.inject(this);

        val viewParticipantList = listOf(ViewParticipant(0,
                0,
                0,
                "Bern",
                "Trem",
                1324141,
                "a@a.de",
                "myco"))

        val streetAddress = StreetAddress(0,
                0,
                0,
                "street",
                1,
                1,
                "addd",
                "d21",
                viewParticipantList = viewParticipantList)

        val defectList = DefectList(0,
                0,
                "name",
                "wqdwqd", streetAddress)


        deleteDatabase("trackdefects.db")

        val r = repo.insertBasic(defectList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response -> Log.d("RESPONSE", response.toString()) }
    }

/*    fun testRetrofit(): Int {

        val moshi = Moshi
                .Builder()
                .build()

        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                        MoshiConverterFactory.create(moshi))
                .baseUrl("http://10.0.2.2:3000/")
                .build()

        val sub = retrofit.create(TestNetDataSource::class.java).test()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response -> Log.d("RESPONSE", response.body().toString()) }

        val response: Response<String> = Response.Failure("TADA")


        return when (response) {
            is Response.Success<String> -> Log.d("Success", response.data)
            is Response.Failure -> Log.d("Failure", response.error)
        }
    }*/
}
