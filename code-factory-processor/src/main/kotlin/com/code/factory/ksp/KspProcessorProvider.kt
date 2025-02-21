package com.code.factory.ksp

import com.code.factory.AllDeclarationFinder
import com.code.factory.CompileChecker
import com.code.factory.InterfaceFinder
import com.code.factory.TestFilesResolver
import com.code.factory.allDeclarationFinder
import com.code.factory.basePathProvider
import com.code.factory.bridge.bridgeFactory
import com.code.factory.codeFilter
import com.code.factory.coderesolver.CodeResolver
import com.code.factory.coderesolver.codeResolver
import com.code.factory.compileChecker
import com.code.factory.interfaceFinder
import com.code.factory.testCodeFilter
import com.code.factory.testFileResolver
import com.code.factory.testSourcePathResolver
import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

@AutoService(SymbolProcessorProvider::class)
class KspProcessorProvider(
    private val allDeclarationFinder: AllDeclarationFinder = allDeclarationFinder(),
    private val interfaceFinder: InterfaceFinder = interfaceFinder(),
    private val codeResolver: CodeResolver = codeResolver(),
    private val testFilterResolver: TestFilesResolver = testFileResolver(),
) : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val compileChecker: CompileChecker = compileChecker(environment.logger)
        return KspProcessor(
            logger = environment.logger,
            codeFilter = codeFilter(allDeclarationFinder, codeResolver),
            testCodeFilter = testCodeFilter(testFilterResolver, codeResolver),
            interfaceFinder = interfaceFinder,
            codeResolver = codeResolver,
            bridgeFactory =
                bridgeFactory(
                    path = basePathProvider(environment.codeGenerator).getBasePath(),
                    logger = environment.logger,
                    codeResolver = codeResolver,
                ),
            compileChecker = compileChecker,
            phaseResolver = PhaseResolverImpl(),
            testSourcePathResolver = testSourcePathResolver(),
            codeGenerator = environment.codeGenerator,
        )
    }
}
