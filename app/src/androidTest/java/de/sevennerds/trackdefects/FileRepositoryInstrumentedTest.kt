package de.sevennerds.trackdefects

import android.graphics.Bitmap
import androidx.test.runner.AndroidJUnit4
import de.sevennerds.trackdefects.common.Constants.Database.DELETION_FAILED
import de.sevennerds.trackdefects.common.Constants.Database.DUPLICATE_FILE
import de.sevennerds.trackdefects.common.Constants.Database.FILES_DELETED
import de.sevennerds.trackdefects.common.Constants.Database.FILES_PATH
import de.sevennerds.trackdefects.common.Constants.Database.FILE_DELETED
import de.sevennerds.trackdefects.common.Constants.Database.FILE_NOT_FOUND
import de.sevennerds.trackdefects.data.file.FileRepository
import de.sevennerds.trackdefects.data.file.FileEntity
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

    val bitmap1: Bitmap = Bitmap.createBitmap(10, 100, Bitmap.Config.ARGB_8888)
    val bitmap2: Bitmap = Bitmap.createBitmap(100, 10, Bitmap.Config.ARGB_8888)
    val bitmap3: Bitmap = Bitmap.createBitmap(20, 100, Bitmap.Config.ARGB_8888)
    val bitmap4: Bitmap = Bitmap.createBitmap(100, 20, Bitmap.Config.ARGB_8888)

    val testFile = File(FILES_PATH, "example_file.jpg")

    // For saving list of files
    val testFileEntityList = listOf(
            FileEntity("example_file1", bitmap1),
            FileEntity("example_file2", bitmap2),
            FileEntity("example_file3", bitmap3),
            FileEntity("example_file4", bitmap4)
    )

    // For deleting list of files
    val testFileList = listOf(
            File(FILES_PATH, "example_file1.jpg"),
            File(FILES_PATH, "example_file2.jpg"),
            File(FILES_PATH, "example_file3.jpg"),
            File(FILES_PATH, "example_file4.jpg")
    )

    // For loading list of files
    val testFileStringList = listOf(
            "example_file1.jpg",
            "example_file2.jpg",
            "example_file3.jpg",
            "example_file4.jpg"
    )

    @Before
    fun setup() {
        if (!file.exists()) file.mkdir()
        if (testFile.exists()) testFile.delete()
    }

    @After
    fun tearDown() {
        if (testFile.exists()) testFile.delete()
    }

    @Test
    fun delete_list_of_files_that_does_not_exist() {
        fileRepository.deleteAll(listOf(File("asdf"), File("asd2"), File("asfg2")))
                .test()
                .assertResult(Result.failure(Error.DeletionFailed(DELETION_FAILED)))
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun save_list_of_files_then_delete() {
        fileRepository.saveAll(testFileEntityList)
                .test()
                .assertResult(Result.success(Unit) as Result<String>)

        fileRepository.loadAll(testFileStringList)
                .test()
                .assertNoErrors()
                .assertComplete()

        fileRepository.deleteAll(testFileList)
                .test()
                .assertResult(Result.success(FILES_DELETED))
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun save_file() {
        fileRepository.save(FileEntity(name = "example_file", data = bitmap1))
                .test()
                .assertResult(Result.success(Unit) as Result<String>)
        fileRepository.delete(testFile)
               .test()
               .assertResult(Result.success(FILE_DELETED))
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun save_file_that_already_exists() {
        fileRepository.save(FileEntity(name = "example_file", data = bitmap1))
                .test()
                .assertResult(Result.success(Unit) as Result<String>)
        fileRepository.save(FileEntity(name = "example_file", data = bitmap1))
                .test()
                .assertResult(Result.failure(Error.DuplicateFileError(DUPLICATE_FILE)) as Result<String>)
        fileRepository.delete(testFile)
                .test()
                .assertResult(Result.success(FILE_DELETED))
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun load_file() {
        fileRepository.save(FileEntity(name = "example_file", data = bitmap1))
                .test()
                .assertResult(Result.success(Unit) as Result<String>)
        fileRepository.load("example_file.jpg")
                .test()
                .assertNoErrors()
                .assertComplete()
    }

    @Test
    fun delete_file_that_does_not_exist() {
        fileRepository.delete(File(FILES_PATH, "non_existent_example_file.jpg"))
                .test()
                .assertResult(
                        Result.failure(Error.FileNotFoundError(FILE_NOT_FOUND))
                )
    }
}