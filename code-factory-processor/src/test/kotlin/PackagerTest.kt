import com.code.factory.bridge.PackagerImpl
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PackagerTest : StringSpec({

    "add package to code" {
        val packager = PackagerImpl()
        val codeWithOutPackage =
            """
            |interface ForGenerate {
            |   fun plus(
            |        first: Int,
            |        second: Int,
            |   ): Int
            """.trimMargin()
        val result = packager.addPackage(codeWithOutPackage, "package.name")
        result shouldBe
            """
            |package package.name
            |
            |interface ForGenerate {
            |   fun plus(
            |        first: Int,
            |        second: Int,
            |   ): Int
            """.trimMargin()
    }
})
