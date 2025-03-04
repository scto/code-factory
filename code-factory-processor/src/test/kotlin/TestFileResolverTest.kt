import com.code.factory.TestFilesResolverImpl
import io.kotest.core.spec.style.StringSpec
import kotlin.io.path.createTempDirectory
import kotlin.test.assertEquals

class TestFileResolverTest : StringSpec({

    "base case" {
        val testFileResolver = TestFilesResolverImpl()
        val tempDir = createTempDirectory().toFile()

        val file1 = java.io.File(tempDir, "Test1.kt")
        val file2 = java.io.File(tempDir, "Test2.kt")
        val subDir = java.io.File(tempDir, "subdir")
        subDir.mkdir()
        val file3 = java.io.File(subDir, "Test3.kt")
        val otherFile = java.io.File(subDir, "Test.txt")

        file1.createNewFile()
        file2.createNewFile()
        file3.createNewFile()
        otherFile.createNewFile()

        val result = testFileResolver.getTestFiles(tempDir.path).toList()

        assertEquals(3, result.size)
        assert(result.containsAll(listOf(file1, file2, file3)))
    }
})
