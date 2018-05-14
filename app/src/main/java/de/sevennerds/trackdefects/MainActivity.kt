package de.sevennerds.trackdefects

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import de.sevennerds.trackdefects.data.defect_list.DefectList
import de.sevennerds.trackdefects.data.defect_list.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.street_address.StreetAddressLocalDataSource
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantLocalDataSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var defectListLocal: DefectListLocalDataSource;

    @Inject
    lateinit var streetAddressLocal: StreetAddressLocalDataSource;

    @Inject
    lateinit var viewParticipantLocal: ViewParticipantLocalDataSource


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TrackDefectsApp.get(this).component.inject(this);

        val dl = DefectList(0, 0, "name", "wqdwqd"
                /*StreetAddress(0, 0, 0, "street", 1, 1, "addd", "d21")*/)



        deleteDatabase("trackdefects.db")

        val r = Single.fromCallable {
            val id = defectListLocal.insert(dl)
            dl.streetAddress?.copy(defectListId = id)
        }
                .map { streetAddress -> streetAddressLocal.insert(streetAddress) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _, t2 -> Log.d("TAG", t2.toString()) }


    }

/*    fun testRetrofit(): Int {

    val moshi = Moshi.Builder()
            .add(AppJsonAdapterFactory.INSTANCE)
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
