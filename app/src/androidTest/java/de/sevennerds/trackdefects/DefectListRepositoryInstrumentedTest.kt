package de.sevennerds.trackdefects

import android.app.Application
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import de.sevennerds.trackdefects.common.Constants.Database.FILES_PATH
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
                name = "Himmelstrasse 1",
                additional = "this address man, it lacks informationS",
                creationDate = "2017-10-03T03:20:11.687+02:00",
                number = 135,
                city = "Peenemünde",
                postalCode = 10512
        )

        val viewParticipantEntity = ViewParticipantEntity(
                forename = "MUDEMANN",
                surname = "SUP",
                companyName = "NO AG",
                email = "me@de.de",
                phoneNumber = 123
        )

        val floorPlanEntity = FloorPlanEntity(
                fileName = "roman.jpg",
                path = FILES_PATH
        )

        val defectListEntity = DefectListEntity(
                name = "kaput fenster",
                creationDate = "2017-10-03T03:20:11.687+02:00",
                floorPlanEntity = floorPlanEntity,
                streetAddressEntity = streetAddressEntity,
                viewParticipantEntity = viewParticipantEntity
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