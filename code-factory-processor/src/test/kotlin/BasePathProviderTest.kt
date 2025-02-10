import com.code.factory.basePathProvider
import com.google.devtools.ksp.processing.CodeGenerator
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.io.File

class BasePathProviderTest : StringSpec({

    "should return base path" {
        val codeGenerator = mockk<CodeGenerator>(relaxed = true)
        every {
            codeGenerator.generatedFile
        } returns listOf(File("some/path/build/path"))
        val basePathProvider = basePathProvider(codeGenerator)
        val result = basePathProvider.getBasePath()

        result shouldBe "some/path/"
    }
})
