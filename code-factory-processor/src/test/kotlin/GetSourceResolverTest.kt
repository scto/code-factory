import com.code.factory.TestSourcePathResolverImpl
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSFile
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetSourceResolverTest : StringSpec({

    val resolver: Resolver = mockk()
    val file: KSFile = mockk()
    val path = "/Users/antonbutov/StudioProjects/code-factory/integration-test/src/main/kotlin/ForGenerate.kt"

    beforeTest {
        every {
            file.filePath
        } returns path
        every {
            resolver.getAllFiles()
        } returns sequenceOf(file)
    }

    "base case" {
        val testSourcePathResolver = TestSourcePathResolverImpl()

        val result = testSourcePathResolver.getSourcesPath(resolver)
        "/Users/antonbutov/StudioProjects/code-factory/integration-test/src/test/" shouldBe result
    }
})
