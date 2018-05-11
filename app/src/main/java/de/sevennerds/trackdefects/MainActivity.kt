package de.sevennerds.trackdefects

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Moshi
import de.sevennerds.trackdefects.data.AppJsonAdapterFactory
import de.sevennerds.trackdefects.data.client.local.ClientLocalDataSource
import de.sevennerds.trackdefects.data.defect_list.DefectList
import de.sevennerds.trackdefects.data.defect_list.DefectListLocalDataSource
import de.sevennerds.trackdefects.data.floor.FloorLocalDataSource
import de.sevennerds.trackdefects.data.response.Response
import de.sevennerds.trackdefects.data.street_address.StreetAddressLocalDataSource
import de.sevennerds.trackdefects.data.test.TestLocalDataSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var clientLocalDataSource: ClientLocalDataSource;

    @Inject
    lateinit var defectListLocal: DefectListLocalDataSource;

    @Inject
    lateinit var streetAddressLocal: StreetAddressLocalDataSource;

    @Inject
    lateinit var floorLocal: FloorLocalDataSource;

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TrackDefectsApp.get(this).component.inject(this);

        val sub = retrofit.create(TestLocalDataSource::class.java).test()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response -> Log.d("RESPONSE", response.body().toString()) }

        Single.fromCallable {
            /*defectListLocal.insert(DefectList(0, 0, "wqdwqdwqd", "30.10.1111"))
            streetAddressLocal.insert(StreetAddress(0, 0, 1, "ewqewqe", 1212, 1, "a", "21323"))*/
            // floorLocal.insert(Floor(0, 0, 1, "EG"))

        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

        Single.just(DefectList(0, 0, "wqdwqdwqd", "30.10.1111"))
                .map { list -> defectListLocal.insert(list) }
                .subscribe { id -> Log.d("ID", id.toString()) }


        test()

        val r = defectListLocal
                .getDefectListWithRelations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    list.forEach {
                        run {
                            Log.d("TAG", it.defectList.toString())
                            it.streetAddressList.forEach {
                                run {
                                    Log.d("TAG", it.streetAddress.toString())
                                    it.floorList.forEach {
                                        run {
                                            Log.d("TAG", it.toString())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

    }

    fun test(): Int {
        val response: Response<String> = Response.Failure("TADA")


        return when (response) {
            is Response.Success<String> -> Log.d("Success", response.data)
            is Response.Failure -> Log.d("Failure", response.error)
        }
    }
}
