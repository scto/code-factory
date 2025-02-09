package com.code.factory.writer

import com.code.factory.MainCodeWriter
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import kotlinx.serialization.Serializable
import java.io.OutputStream

internal class WriterImpl(
    private val storageWriter: StorageWriter,
    private val mainCodeWriter: MainCodeWriter,
    private val codeGenerator: CodeGenerator,
) : Writer {
    override fun setKotlinPath(
        packageName: String,
        name: String,
    ) {
        codeGenerator.createNewFile(Dependencies.ALL_FILES, packageName, name)
        val emptyFile = codeGenerator.generatedFile.first()
        val kotlinPath = emptyFile.parentFile.path
        storageWriter.setKotlinPath(kotlinPath)
    }

    override fun write(writerData: WriterData) {
        mainCodeWriter.write(writerData)
        //
        codeGenerator.createNewFile(Dependencies.ALL_FILES, writerData.packageName, writerData.name).use {
            it.appendText(writerData.code)
        }
    }
}

@Serializable
data class WriterData(
    val packageName: String,
    val name: String,
    val code: String,
)

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}
