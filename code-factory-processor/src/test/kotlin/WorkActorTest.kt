import com.code.factory.AllDeclarationFinder
import com.code.factory.CodeFilter
import com.code.factory.CompileChecker
import com.code.factory.InterfaceFinder
import com.code.factory.TestCodeFilter
import com.code.factory.TestSourcePathResolver
import com.code.factory.bridge.BridgeFactory
import com.code.factory.coderesolver.CodeResolver
import com.code.factory.ksp.PhaseResolver
import com.code.factory.ksp.Phases
import com.code.factory.ksp.WorkActorImpl
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSDeclaration
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class WorkActorTest : StringSpec({

    lateinit var bridgeFactory: BridgeFactory
    lateinit var codeResolver: CodeResolver
    lateinit var allDeclarationFinder: AllDeclarationFinder
    lateinit var interfaceFinder: InterfaceFinder
    lateinit var resolver: Resolver
    lateinit var declaration: KSDeclaration
    lateinit var declarations: Sequence<KSDeclaration>
    lateinit var compileChecker: CompileChecker
    lateinit var phaseResolver: PhaseResolver
    lateinit var codeFilter: CodeFilter
    lateinit var testCodeFilter: TestCodeFilter
    lateinit var testSourcePathResolver: TestSourcePathResolver
    lateinit var codeGenerator: CodeGenerator
    lateinit var workActor: WorkActorImpl

    beforeTest {
        bridgeFactory = mockk(relaxed = true)
        allDeclarationFinder = mockk()
        resolver = mockk(relaxed = true)
        declaration = mockk<KSDeclaration>(relaxed = true)
        declarations = sequenceOf(declaration)
        every {
            allDeclarationFinder.getAllDeclaration(any())
        } returns declarations
        codeResolver = mockk(relaxed = true)
        every {
            codeResolver.getCodeString(declarations)
        } returns sequenceOf("code context")
        interfaceFinder = mockk(relaxed = true)
        compileChecker = mockk(relaxed = true)
        phaseResolver = mockk(relaxed = true)
        codeFilter = mockk(relaxed = true)
        testCodeFilter = mockk(relaxed = true)
        testSourcePathResolver = mockk(relaxed = true)
        codeGenerator = mockk(relaxed = true)
        workActor =
            WorkActorImpl(
                phaseResolver = phaseResolver,
                bridgeFactory = bridgeFactory,
                interfaceFinder = interfaceFinder,
                codeFilter = codeFilter,
                testCodeFilter = testCodeFilter,
                testSourcePathResolver = testSourcePathResolver,
                codeGenerator = codeGenerator,
                logger = mockk(relaxed = true),
                resolver = resolver,
                apiKey = "apiKey",
            )
    }

    "when phase main should work bridge main" {
        every {
            phaseResolver.resolvePhase(any())
        } returns Phases.Main
        workActor.act()

        verify {
            bridgeFactory.createMain(any())
        }
    }

    "when phase test should work bridge test" {
        every {
            phaseResolver.resolvePhase(any())
        } returns Phases.Tests
        workActor.act()

        verify {
            bridgeFactory.createTest()
        }
    }
})
