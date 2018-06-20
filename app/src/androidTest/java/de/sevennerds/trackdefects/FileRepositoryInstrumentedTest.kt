package de.sevennerds.trackdefects

import android.graphics.Bitmap
import androidx.test.runner.AndroidJUnit4
import de.sevennerds.trackdefects.common.Constants.Database.FILES_PATH
import de.sevennerds.trackdefects.data.file.FileRepository
import de.sevennerds.trackdefects.data.file.GenericFile
import de.sevennerds.trackdefects.data.response.Error
import de.sevennerds.trackdefects.data.response.Result
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class FileRepositoryInstrumentedTest {




    val fileRepository: FileRepository = FileRepository()
    val file = File(FILES_PATH)
    val bitmap: Bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    val testFile = File(FILES_PATH, "example_file.jpg")

    @Before
    fun setup() {
        if (!file.exists()) {
            file.mkdir()
        }
    }

    @After
    fun tearDown() {
        if (testFile.exists()) {
            testFile.delete()
        }
    }

    @Test
    fun save_file() {
        fileRepository.save(GenericFile(name = "example_file", data = bitmap))
                .test()
                .assertResult(Result.success(R.string.file_saved.toString()))

        fileRepository.delete(File("example_file"))
                .test()
                .assertResult(Result.success(R.string.file_deleted.toString()))
    }


    /*
    @Test
    fun save_file_conflict() {

        fileRepository.save(GenericFile(name = "example_file", data = bitmap))
                .test()
                .assertResult(Result.success(R.string.file_saved.toString()))

        fileRepository.save(GenericFile(name = "example_file", data = bitmap))
                .test()

        //duplicate.assertResult(Result.failure(Error.DuplicateFileError(R.string.duplicate_file.toString())))

    }
    */


    /*
    @Test
    fun save_files() {

    }

    @Test
    fun save_files_conflict() {

    }

    @Test
    fun loading_file() {

    }

    @Test
    fun deleting_file() {

    }
    */

    @Test
    fun deleting_file_not_found() {
        fileRepository.delete(File("non_existant_example_file"))
                .test()
                .assertResult(Result.failure(Error.FileNotFoundError(R.string.file_not_found.toString())))

        /**
         * deleting something that doesnt exist will always succeed it seems.
         */
    }

}