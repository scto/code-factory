package com.code.factory

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies

interface BasePathProvider {
    fun getBasePath(): String
}

fun basePathProvider(codeGenerator: CodeGenerator): BasePathProvider = BasePathProviderImpl(codeGenerator)
// #62
class BasePathProviderImpl(private val codeGenerator: CodeGenerator): BasePathProvider {
    override fun getBasePath(): String {
        codeGenerator.createNewFile(Dependencies.ALL_FILES, "", "tempFile") // #61
        val emptyFile = codeGenerator.generatedFile.first()
        val emptyFilePath = emptyFile.path
        return emptyFilePath.split("build").first()
    }
}