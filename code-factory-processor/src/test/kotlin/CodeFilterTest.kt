import com.code.factory.AllDeclarationFinder
import com.code.factory.AllDeclarationFinderImpl
import com.code.factory.CodeFilter
import com.code.factory.CodeFilterImpl
import com.code.factory.InterfaceFinder
import com.code.factory.coderesolver.CodeResolver
import com.code.factory.coderesolver.CodeResolverImpl
import com.code.factory.interfaceFinder
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CodeFilterTest : StringSpec({

    lateinit var getAllDeclarations: AllDeclarationFinder
    lateinit var codeResolver: CodeResolver
    lateinit var codeFilter: CodeFilter
    lateinit var interfaceFinder: InterfaceFinder

    beforeTest {
        getAllDeclarations = AllDeclarationFinderImpl()
        codeResolver = CodeResolverImpl()
        codeFilter = CodeFilterImpl(getAllDeclarations, codeResolver)
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
        listOf(simpleClass, interfaceWithOutDeclarations, secretClass) compile { resolver ->
            val interfaceWithOutDeclarations = interfaceFinder.getInterfacesWithOutImplementation(resolver).first()
            val result =
                codeFilter.getFilteredCodeDeclarations(
                    resolver,
                    interfaceWithOutDeclarations,
                ).map { it.qualifiedName!!.asString() }
            result.toList() shouldBe listOf("SimpleClass", "InterfaceWithOutDeclarations")
        }
    }
})
