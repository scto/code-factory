import com.code.factory.compilation.compilationForAssertations
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertTrue

class CompilationForAssertationTest {
    val correctCode =
        """
        class Test {
            fun test(): String {
                return "test"
            }
        }
        """.trimIndent()

    @Test
    fun `correct code should compile`() {
        compilationForAssertations(correctCode) {
            assertTrue { true }
        }
    }

    @Test
    fun `incorrect code should not compile`() {
        assertThrows<IllegalStateException> {
            compilationForAssertations(correctCode) {
                error("My error.")
            }
        }
    }
}
