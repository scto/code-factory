import com.code.factory.ksp.BasePathProviderImpl
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSFile
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class BasePathProviderTest : StringSpec({

    "should return base path" {
        val resolver = mockk<Resolver>()
        val ksFile = mockk<KSFile>()
        every {
            ksFile.filePath
        } returns "/Users/antonbutov/StudioProjects/code-factory/integration-test/src/main/kotlin/ForGenerate.kt"
        every {
            resolver.getAllFiles()
        } returns sequenceOf(ksFile)
        val basePathProvider = BasePathProviderImpl(mockk(relaxed = true))
        val result = basePathProvider.getBasePath(resolver)

        result shouldBe "/Users/antonbutov/StudioProjects/code-factory/integration-test/"
    }
})
