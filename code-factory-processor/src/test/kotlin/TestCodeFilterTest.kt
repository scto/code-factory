import com.code.factory.TestCodeFilter
import com.code.factory.coderesolver.CodeResolver
import com.code.factory.compilation.compilationForAssertations
import com.code.factory.testCodeFilter
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class TestCodeFilterTest : StringSpec({

    lateinit var storage: Storage
    lateinit var codeResolver: CodeResolver
    lateinit var testCodeFilter: TestCodeFilter

    beforeTest {
        storage = mockk(relaxed = true)
        codeResolver = mockk(relaxed = true)
        testCodeFilter = testCodeFilter(storage, codeResolver)
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

    "when have empty declaration names should all return" {
        val declarationsNames = listOf<String>()
        every {
            storage.getNamesForTestFilter()
        } returns declarationsNames
        compilationForAssertations(testFile) { resolver ->
            val result = testCodeFilter.getFilteredTestDeclarations(resolver).toList()
            result shouldBe emptyList()
        }
    }

    val secretTest =
        """
        class SecretTest {
            @Test
            fun test() {

            }
        }
        """.trimIndent()

    "when have a declaration should return the test" {
        val declarationsNames = listOf("SomeClass")
        every {
            storage.getNamesForTestFilter()
        } returns declarationsNames
        every {
            codeResolver.getCodeString(file = any())
        } returns secretTest
        compilationForAssertations(secretTest) { resolver ->
            val result = testCodeFilter.getFilteredTestDeclarations(resolver).map { it.simpleName.asString() }.toList()
            result shouldBe emptyList()
        }
    }
})
