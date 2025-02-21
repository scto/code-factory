import com.code.factory.bridge.Bridge
import com.code.factory.bridge.BridgeMainWork
import com.code.factory.bridge.OpenAiService
import com.code.factory.coderesolver.CodeResolver
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class BridgeTestTest : StringSpec({

    lateinit var bridgeMain: Bridge.BridgeMain
    lateinit var openAi: OpenAiService
    lateinit var codeResolver: CodeResolver

    beforeTest {
        openAi = mockk(relaxed = true)
        coEvery { openAi.getCode(any(), any()) } returns
            """
            ```kotlin
            AiCode
            ```
            """.trimIndent()
        codeResolver = mockk(relaxed = true)
        bridgeMain = BridgeMainWork(codeResolver, openAi)
    }

    "when InterfaceFinder find interfaces bridge should to request code resolver for resolve it" {
        val writeData = bridgeMain.getCode(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        writeData!!.code shouldBe "AiCode"
    }

    "when allDeclarations is empty code should generate" {
        val writeData = bridgeMain.getCode(mockk(relaxed = true), emptySequence(), mockk(relaxed = true))
        writeData!!.code shouldBe "AiCode"
    }

    "when interfaceWithOutImplementation is not null get code should return code" {
        val writeData = bridgeMain.getCode(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        writeData!!.code shouldBe "AiCode"
    }
})
