package com.code.factory.bridge

import com.code.factory.ApiKeyResolver
import com.code.factory.coderesolver.CodeResolver
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import javax.inject.Inject

interface BridgeFactory {
    fun createMain(): Bridge.BridgeMain

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
        private val apiKeyResolveProvider: ApiKeyResolver,
    ) : BridgeFactory {
        @get:JvmName("getApiKeyProperty")
        private val apiKey: String by lazy { apiKeyResolveProvider.resolve() ?: error("ApiKey not found.") }

        override fun createMain(): Bridge.BridgeMain =
            keyLogic(
                mock = BridgeMainWork(codeResolver, openAiServiceMock(logger)),
                work = BridgeMainWork(codeResolver, openAiService(apiKey, logger)),
            )

        override fun createTest(): Bridge.BridgeTest = BridgeTestWork()

        override fun createGenerated(): Bridge.BridgeGenerated = BridgeGeneratedWork()

        private fun <B : Bridge> keyLogic(
            mock: B,
            work: B,
        ): B {
            return when (apiKey) {
                "test" -> mock
                else -> work
            }
        }
    }
