package com.code.factory.bridge

import com.code.factory.coderesolver.CodeResolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import removeChatComment

internal class BridgeMainWork(
    private val codeResolver: CodeResolver,
    private val openAi: OpenAiService,
) : Bridge.BridgeMain {
    override suspend fun getCode(
        testCode: Sequence<String>,
        declarations: Sequence<KSDeclaration>,
        interfaceWithOutImpl: KSClassDeclaration,
    ): WriterData? {
        val allDeclarationsCode = codeResolver.getCodeString(declarations).joinToString("\n")
        val context = "$allDeclarationsCode\n${testCode.toList().joinToString("\n")}"
        val generatedCode = openAi.getCode(context, interfaceWithOutImpl.toString()).removeChatComment()
        return WriterData(
            code = generatedCode,
            packageName = interfaceWithOutImpl.packageName.asString(),
            name = interfaceWithOutImpl.simpleName.asString(),
        )
    }
}

internal class BridgeTestWork() : Bridge.BridgeTest

internal class BridgeGeneratedWork : Bridge.BridgeGenerated
