package de.sevennerds.trackdefects

import android.app.Application
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import de.sevennerds.trackdefects.core.di.AppModule
import de.sevennerds.trackdefects.core.di.DaggerAppComponent
import de.sevennerds.trackdefects.data.defect_list.DefectListEntity
import de.sevennerds.trackdefects.data.floor_plan.FloorPlanEntity
import de.sevennerds.trackdefects.data.street_address.StreetAddressEntity
import de.sevennerds.trackdefects.data.view_participant.ViewParticipantEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DefectListRepositoryInstrumentedTest {

    private val application = Application()

    private val cmp = DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .build()

    @Before
    fun setup() {

    }

    @After
    fun tearDown() {

    }

    @Test
    @Throws(Exception::class)
    fun saveDefectListEntityListToDb() {

        val streetAddressEntity = StreetAddressEntity(
                -1L,
                -1L,
                -1L,
                name = "Himmelstrasse 1",
                additional = "this address man, it lacks informationS",
                creationDate = "2017-10-03T03:20:11.687+02:00",
                number = 135,
                city = "Peenemünde",
                postalCode = 10512
        )

        val viewParticipantEntity = ViewParticipantEntity(
                -1L,
                -1L,
                -1L,
                -1L,
                "MUDEMANN",
                "SUP",
                123,
                "me@de.de",
                "NO AG"
        )

        val floorPlanEntity = FloorPlanEntity(
                -1L,
                "",
                "",
                -1L
        )

        val defectListEntity = DefectListEntity(
                -1L,
                -1L,
                "kaput fenster",
                "2017-10-03T03:20:11.687+02:00",
                floorPlanEntity,
                streetAddressEntity,
                viewParticipantEntity
        )

        cmp.defectRepository().insert(defectListEntity).test().assertNoErrors()
    }

    @Test
    @Throws(Exception::class)
    fun loadDefectListEntityListFromDb() {

    }

    @Test
    @Throws(Exception::class)
    fun deleteDefectListEntityListFromDb() {

    }

}