import com.code.factory.compilation.compilationForAssertations
import com.code.factory.getAllDeclarations
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class GetAllDeclarationsTest : StringSpec({

    val someType =
        """
        class SomeType {
            
        }
        """.trimIndent()

    val someClass =
        """
        interface InterfaceWithOutDeclarations {
            fun doSome(): SomeType
            }
        """.trimIndent()

    "should return all declarations" {
        compilationForAssertations(someType, someClass) { resolver ->
            val result = resolver.getAllFiles().getAllDeclarations().map { it.qualifiedName!!.asString() }.toList()
            result shouldBe listOf("SomeType", "InterfaceWithOutDeclarations")
        }
    }

    "should return all declarations from interface only" {
        compilationForAssertations(someClass) { resolver ->
            val result = resolver.getAllFiles().getAllDeclarations().map { it.qualifiedName!!.asString() }.toList()
            //    result shouldBe listOf("SomeType", "InterfaceWithOutDeclarations")
        }
    }
})
