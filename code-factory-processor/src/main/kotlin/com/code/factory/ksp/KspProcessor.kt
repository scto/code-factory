package com.code.factory.ksp

import com.code.factory.CodeFilter
import com.code.factory.CompileChecker
import com.code.factory.InterfaceFinder
import com.code.factory.TestCodeFilter
import com.code.factory.bridge.BridgeFactory
import com.code.factory.coderesolver.CodeResolver
import com.code.factory.writer.Writer
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import kotlinx.coroutines.runBlocking

class KspProcessor(
    private val logger: KSPLogger,
    private val writer: Writer,
    private val codeFilter: CodeFilter,
    private val testCodeFilter: TestCodeFilter,
    private val interfaceFinder: InterfaceFinder,
    private val codeResolver: CodeResolver,
    private val compileChecker: CompileChecker,
    private val phaseResolver: PhaseResolver,
    private val bridgeFactory: BridgeFactory,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        runBlocking {
            when (phaseResolver.resolvePhase(resolver.getAllFiles().map { it.filePath }.toList())) {
                Phases.Main -> {
                    // logger.warn("Main")
                    val bridgeMain = bridgeFactory.createMain() // need here for test
                    val interfaceWithOutImpl =
                        interfaceFinder
                            .getInterfacesWithOutImplementation(resolver)
                            .firstOrNull() ?: return@runBlocking // todo # 47 only one yet
                    val declarations = codeFilter.getFilteredDeclarations(resolver, sequenceOf(interfaceWithOutImpl))
                    bridgeMain.saveDeclarations(declarations)
                    bridgeMain.saveInterFaceWithOutDeclaration(interfaceWithOutImpl)
                    writer.setKotlinPath(
                        interfaceWithOutImpl.packageName.asString(),
                        interfaceWithOutImpl.simpleName.asString(),
                    )
                }

                Phases.Tests -> {
                    // logger.warn("Test")
                    val bridgeTest = bridgeFactory.createTest()
                    val declarationTest = testCodeFilter.getFilteredTestDeclarations(resolver)
                    val generatedCode = bridgeTest.getCode(declarationTest)
                    generatedCode?.let {
                        writer.write(it)
                    } ?: logger.warn("Code not valid")
                }

                Phases.Generated -> {
                    // logger.warn("Generated")
                    // for other interfaces with out implementation
                }
            }
        }
        return emptyList()
    }
}
