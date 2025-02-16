import com.code.factory.AllDeclarationFinder
import com.code.factory.CodeFilter
import com.code.factory.InterfaceFinder
import com.code.factory.allDeclarationFinder
import com.code.factory.codeFilter
import com.code.factory.coderesolver.CodeResolver
import com.code.factory.coderesolver.codeResolver
import com.code.factory.compilation.compilationForAssertations
import com.code.factory.interfaceFinder
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CodeFilterTest : StringSpec({

    lateinit var getAllDeclarations: AllDeclarationFinder
    lateinit var codeResolver: CodeResolver
    lateinit var codeFilter: CodeFilter
    lateinit var interfaceFinder: InterfaceFinder

    beforeTest {
        getAllDeclarations = allDeclarationFinder()
        codeResolver = codeResolver()
        codeFilter = codeFilter(getAllDeclarations, codeResolver)
        interfaceFinder = interfaceFinder()
    }

    "when have simple class then should be found one without a SecretClass" {
        val simpleClass =
            """
            class SimpleClass {
                
            }
            """.trimIndent()
        val interfaceWithOutDeclarations =
            """
            interface InterfaceWithOutDeclarations {
              fun doSome(): SimpleClass
            }
            """.trimIndent()

        val secretClass =
            """
            class SecretClass {
            }
            """.trimIndent()
        compilationForAssertations(simpleClass, interfaceWithOutDeclarations, secretClass) { resolver ->
            val interfaceWithOutDeclarations = interfaceFinder.getInterfacesWithOutImplementation(resolver)
            val result = codeFilter.getFilteredDeclarations(resolver, interfaceWithOutDeclarations).map { it.qualifiedName!!.asString() }
            result.toList() shouldBe listOf("SimpleClass", "InterfaceWithOutDeclarations")
        }
    }
})
