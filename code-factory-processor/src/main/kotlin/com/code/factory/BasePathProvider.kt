package com.code.factory

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import javax.inject.Inject

interface BasePathProvider {
    fun getBasePath(): String
}

// #62
class BasePathProviderImpl
    @Inject
    constructor(private val codeGenerator: CodeGenerator) : BasePathProvider {
        private val path: String by lazy { getBasePathInternal() }

        override fun getBasePath(): String = path

        private fun getBasePathInternal(): String {
            codeGenerator.createNewFile(Dependencies.ALL_FILES, "", "tempFile") // #61
            val emptyFile = codeGenerator.generatedFile.first()
            val emptyFilePath = emptyFile.path
            return emptyFilePath.split("build").first()
        }
    }
