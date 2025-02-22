package com.code.factory.bridge

import com.code.factory.coderesolver.CodeResolver
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import java.io.File
import java.util.Properties

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

fun bridgeFactory(
    codeResolver: CodeResolver,
    logger: KSPLogger,
    path: String,
): BridgeFactory = BridgeFactoryImpl(codeResolver, logger, path)

internal class BridgeFactoryImpl(
    private val codeResolver: CodeResolver,
    private val logger: KSPLogger,
    private val path: String,
) : BridgeFactory {
    @get:JvmName("getApiKeyProperty")
    private val apiKey: String by lazy { getApiKey() }

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

    private fun getApiKey(): String =
        loadLocalProperties(path).getProperty("API_KEY")
            ?: error("API_KEY not found in local.properties")
}

private fun loadLocalProperties(path: String): Properties {
    val properties = Properties()
    val localPropertiesFile = File(path, "local.properties")
    return properties
        .takeIf {
            localPropertiesFile.exists()
        }?.also {
            it.load(localPropertiesFile.inputStream())
        } ?: error("local.properties file not found")
}
