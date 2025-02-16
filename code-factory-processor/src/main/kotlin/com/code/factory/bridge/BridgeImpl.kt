package com.code.factory.bridge

import Storage
import com.code.factory.CompileChecker
import com.code.factory.coderesolver.CodeResolver
import com.code.factory.writer.WriterData
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import removeChatComment

internal class BridgeMainWork(
    private val storage: Storage,
    private val codeResolver: CodeResolver,
) : Bridge.BridgeMain {
    override fun saveDeclarations(declarations: Sequence<KSDeclaration>) {
        val allDeclarationsCode = codeResolver.getCodeString(declarations).joinToString("\n")
        storage.saveDeclarationsCode(allDeclarationsCode)
        val declarationsNames = declarations.map { it.simpleName.asString() }.toList()
        storage.saveDeclarationsNames(declarationsNames)
    }

    override fun saveInterFaceWithOutDeclaration(interfaceWithOutImpl: KSClassDeclaration) {
        val writerData =
            WriterData(
                code = interfaceWithOutImpl.toString(),
                packageName = interfaceWithOutImpl.packageName.asString(),
                name = interfaceWithOutImpl.simpleName.asString(),
            )
        storage.setInterfaceWithOutImplementation(writerData)
    }
}

internal class BridgeTestWork(
    private val codeResolver: CodeResolver,
    private val storage: Storage,
    private val openAi: OpenAiService,
    private val compileChecker: CompileChecker,
) : Bridge.BridgeTest {
    override suspend fun getCode(testDeclarations: Sequence<KSDeclaration>): WriterData? {
        val writeData = storage.getInterfaceWithOutImplementation() ?: return null
        val testDeclarations = codeResolver.getCodeString(testDeclarations).joinToString("\n")
        storage.saveDeclarationsCode(testDeclarations)
        val context: String = storage.getDeclarationCode() ?: ""
        val code = openAi.getCode(context, writeData.code).removeChatComment()
        if (compileChecker.checkCompile(context, writeData.code)) {
            return null // #54
        }
        storage.clean()
        return WriterData(
            code = code,
            packageName = writeData.packageName,
            name = writeData.name,
        )
    }
}

internal class BridgeGeneratedWork : Bridge.BridgeGenerated

internal class BridgeGeneratedMock() : Bridge.BridgeGenerated
