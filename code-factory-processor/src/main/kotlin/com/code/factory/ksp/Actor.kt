package com.code.factory.ksp

import com.code.factory.CodeFilter
import com.code.factory.InterfaceFinder
import com.code.factory.TestCodeFilter
import com.code.factory.TestSourcePathResolver
import com.code.factory.bridge.BridgeFactory
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import kotlin.text.toByteArray

interface Actor {
    fun act(): List<KSAnnotated>
}

class WorkActorImpl
    @AssistedInject
    constructor(
        private val phaseResolver: PhaseResolver,
        private val bridgeFactory: BridgeFactory,
        private val interfaceFinder: InterfaceFinder,
        private val codeFilter: CodeFilter,
        private val testCodeFilter: TestCodeFilter,
        private val testSourcePathResolver: TestSourcePathResolver,
        private val codeGenerator: CodeGenerator,
        private val logger: KSPLogger,
        @Assisted private val resolver: Resolver,
        @Assisted private val apiKey: String,
    ) : Actor {
        override fun act(): List<KSAnnotated> {
            logger.warn("Work actor act.")
            runBlocking {
                when (phaseResolver.resolvePhase(resolver.getAllFiles().map { it.filePath }.toList())) {
                    Phases.Main -> {
                        // logger.warn("Main")
                        val bridgeMain = bridgeFactory.createMain(apiKey) // need here for test
                        val interfaceWithOutImpl =
                            interfaceFinder
                                .getInterfacesWithOutImplementation(resolver)
                                .firstOrNull() ?: return@runBlocking // todo # 47 only one yet
                        val declarations = codeFilter.getFilteredCodeDeclarations(resolver, interfaceWithOutImpl)
                        if (declarations.toList().isEmpty()) {
                            logger.warn("No declarations found")
                            return@runBlocking
                        }
                        val basePath = testSourcePathResolver.getSourcesPath(resolver)
                        val codeOfTests =
                            testCodeFilter.getFilteredTestCode(declarations.map { it.simpleName.asString() }, basePath)
                        if (codeOfTests.toList().isEmpty()) {
                            logger.warn("No tests found.")
                        }
                        val generatedCode = bridgeMain.getCode(codeOfTests, declarations, interfaceWithOutImpl)
                        generatedCode?.let {
                            codeGenerator.createNewFile(
                                dependencies = Dependencies.ALL_FILES,
                                packageName = it.packageName,
                                fileName = it.name,
                            ).use {
                                    outputStream ->
                                outputStream.write(it.code.toByteArray())
                            }
                        } ?: logger.warn("Code not valid")
                    }

                    Phases.Tests -> {
                        // logger.warn("Test")
                        val bridgeTest = bridgeFactory.createTest()
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

class SleepActor
    @Inject
    constructor(
        private val logger: KSPLogger,
    ) : Actor {
        override fun act(): List<KSAnnotated> {
            logger.warn("Code factory processor sleeps")
            return emptyList()
        }
    }
