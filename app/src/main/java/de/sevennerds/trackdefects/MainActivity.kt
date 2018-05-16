package de.sevennerds.trackdefects

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import de.sevennerds.trackdefects.data.defect.DefectInsert
import de.sevennerds.trackdefects.data.defect.DefectRepo
import de.sevennerds.trackdefects.data.defect_list.DefectListRepo
import de.sevennerds.trackdefects.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var defectListRepo: DefectListRepo

    @Inject
    lateinit var defectRepo: DefectRepo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TrackDefectsApp.get(this).component.inject(this);


        deleteDatabase("trackdefects.db")

        val r = defectListRepo.insertBasic(defectList)
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response -> Log.d("RESPONSE1", response.toString()) }


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
