package com.code.factory.bridge

import com.code.factory.coderesolver.CodeResolver
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import jakarta.inject.Inject

interface BridgeFactory {
    fun createMain(apiKey: String): Bridge.BridgeMain

    fun createTest(): Bridge.BridgeTest

    fun createGenerated(): Bridge.BridgeGenerated
}

sealed interface Bridge {
    interface BridgeMain : Bridge {
        suspend fun getCode(
            testCode: Sequence<String>,
            declarations: Sequence<KSDeclaration>,
            interfaceWithOutImpl: KSClassDeclaration,
        ): WriterData?
    }

    interface BridgeTest : Bridge

    interface BridgeGenerated : Bridge
}

class BridgeFactoryImpl
    @Inject
    constructor(
        private val codeResolver: CodeResolver,
        private val logger: KSPLogger,
    ) : BridgeFactory {
        override fun createMain(apiKey: String): Bridge.BridgeMain {
            return when (apiKey) {
                "test" -> BridgeMainWork(codeResolver, openAiServiceMock(logger))
                else -> BridgeMainWork(codeResolver, openAiService(apiKey, logger))
            }
        }

        override fun createTest(): Bridge.BridgeTest = BridgeTestWork()

        override fun createGenerated(): Bridge.BridgeGenerated = BridgeGeneratedWork()
    }
