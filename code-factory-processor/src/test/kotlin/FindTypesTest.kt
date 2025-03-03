import com.code.factory.AllDeclarationFinder
import com.code.factory.AllDeclarationFinderImpl
import com.code.factory.compilation.compilationForAssertations
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.test.assertEquals

private lateinit var allDeclarationFinder: AllDeclarationFinder

class FindTypesTest : StringSpec({

    beforeTest {
        allDeclarationFinder = AllDeclarationFinderImpl()
    }

    "when contain A-type should find it" {
        val typeASource =
            """
            class A {
            }
            """.trimIndent()

        val mainCode =
            """
            
            fun someFun() {
                val classA: A = A()
            }
            
            """.trimIndent()

        compilationForAssertations(typeASource, mainCode) { resolver ->
            assertEquals(listOf("A"), allDeclarationFinder.getAllDeclaration(resolver).map { it.toString() }.toList())
        }
    }

    "when work with simple test should return test class" {
        val testClass =
            """
            import java.io.File
            import kotlin.test.Test
            import kotlin.test.assertEquals

            class GeneratedTest {

                @Test
                fun generatedFileGets() {
                    val generatedFile: File = File("integration-test/build/generated/ksp/main/kotlin/ForGenerate.kt") //#45
                    assertEquals(""${'"'}
                        class GeneratedCode(): ForGenerate {

                        }
                    ""${'"'}.trimIndent(), generatedFile.readText())
                }
            }
            """.trimIndent()
        compilationForAssertations(testClass) { resolver ->
            val declarations = allDeclarationFinder.getAllDeclaration(resolver).map { it.qualifiedName?.asString() }.toList()
            declarations shouldBe listOf("GeneratedTest")
        }
    }

    "interface in single file should bee founded" {
        val singleInterface =
            """
            interface SingleInterface {
                fun myFun()
            }
            """.trimIndent()
        compilationForAssertations(singleInterface) { resolver ->
            val allDeclarations = allDeclarationFinder.getAllDeclaration(resolver)
            listOf("SingleInterface") shouldBe allDeclarations.map { it.toString() }.toList()
        }
    }
})
