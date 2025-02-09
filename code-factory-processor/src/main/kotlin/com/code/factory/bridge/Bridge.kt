package com.code.factory.bridge

import Storage
import com.code.factory.CompileChecker
import com.code.factory.coderesolver.CodeResolver
import com.code.factory.writer.WriterData
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
        fun saveAllDeclarations(allDeclarations: List<KSDeclaration>)

        fun saveInterFaceWithOutDeclaration(interfaceWithOutImpl: KSClassDeclaration)
    }

    interface BridgeTest : Bridge {
        suspend fun getCode(testDeclarations: List<KSDeclaration>): WriterData?
    }

    interface BridgeGenerated : Bridge
}

fun bridgeFactory(
    storage: Storage,
    compileChecker: CompileChecker,
    codeResolver: CodeResolver,
    logger: KSPLogger,
    path: String,
): BridgeFactory = BridgeFactoryImpl(storage, compileChecker, codeResolver, logger, path)

internal class BridgeFactoryImpl(
    private val storage: Storage,
    private val compileChecker: CompileChecker,
    private val codeResolver: CodeResolver,
    private val logger: KSPLogger,
    private val path: String,
) : BridgeFactory {
    @get:JvmName("getApiKeyProperty")
    private val apiKey: String by lazy { getApiKey() }

    override fun createMain(): Bridge.BridgeMain =
        keyLogic(
            mock = BridgeMainWork(storage, codeResolver),
            work = BridgeMainWork(storage, codeResolver),
        )

    override fun createTest(): Bridge.BridgeTest =
        keyLogic(
            mock = BridgeTestMock(storage),
            work = BridgeTestWork(codeResolver, storage, openAiService(apiKey, logger), compileChecker),
        )

    override fun createGenerated(): Bridge.BridgeGenerated =
        keyLogic(
            mock = BridgeGeneratedMock(),
            work = BridgeGeneratedWork(),
        )

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
