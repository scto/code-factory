import com.code.factory.TestCodeFilter
import com.code.factory.TestFilesResolver
import com.code.factory.coderesolver.CodeResolver
import com.code.factory.testCodeFilter
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.io.File

class TestCodeFilterTest : StringSpec({

    lateinit var codeResolver: CodeResolver
    lateinit var testFilesResolver: TestFilesResolver
    lateinit var testCodeFilter: TestCodeFilter

    beforeTest {
        codeResolver = mockk(relaxed = true)
        testFilesResolver = mockk(relaxed = true)
        val testFile = File("testPath")
        every {
            testFilesResolver.getTestFiles(any())
        } returns sequenceOf(testFile)
        testCodeFilter = testCodeFilter(testFilesResolver, codeResolver)
    }

    val testFile =
        """
        class Test {
            @Test
            fun test() {
            val someClass = SomeClass()
            
            }
        }
        """.trimIndent()

    "when have empty declaration names should return nothing" {
        val declarationsNames = emptySequence<String>()
        every {
            codeResolver.getCodeString(file = any())
        } returns testFile
        val result = testCodeFilter.getFilteredTestCode(declarationsNames, "path").toList()
        result shouldBe emptyList()
    }

    val secretTest =
        """
        class SecretTest {
            @Test
            fun test() {

            }
        }
        """.trimIndent()

    "when secret not contain in test code the test should not return" {
        val declarationsNames = sequenceOf("SomeClass")
        every {
            codeResolver.getCodeString(file = any())
        } returns secretTest
        val result = testCodeFilter.getFilteredTestCode(declarationsNames, "path").toList()
        result shouldBe emptyList()
    }

    "when names contains in test should return the test" {
        val declarationsNames = sequenceOf("SomeClass")
        every {
            codeResolver.getCode(any())
        } returns testFile // !
        val result = testCodeFilter.getFilteredTestCode(declarationsNames, "path").toList().first()
        result shouldBe testFile
    }
})
