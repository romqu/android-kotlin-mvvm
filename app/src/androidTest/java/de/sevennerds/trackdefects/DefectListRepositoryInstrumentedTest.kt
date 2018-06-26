package de.sevennerds.trackdefects

import android.app.Application
import androidx.test.InstrumentationRegistry
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import de.sevennerds.trackdefects.core.di.AppModule
import de.sevennerds.trackdefects.core.di.DaggerAppComponent
import de.sevennerds.trackdefects.core.di.DatabaseModule
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
//            .databaseModule(DatabaseModule(InstrumentationRegistry.getTargetContext()))
            .build()

    @Before
    fun setup() {
        cmp.streetAddressRepository()
        cmp.
    }

    @After
    fun tearDown() {

    }

    @Test
    @Throws(Exception::class)
    fun saveDefectListEntityListToDb() {
        cmp.
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