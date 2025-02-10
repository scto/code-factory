package com.code.factory.coderesolver

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import java.nio.file.Files
import kotlin.io.path.Path

internal class CodeResolverImpl : CodeResolver {
    @Throws(AssertionError::class)
    override fun getCodeString(declaration: Sequence<KSDeclaration>): Sequence<String> {
        assert(declaration.toList().isNotEmpty())
        return declaration.map {
            it.containingFile?.let { getCodeString(it) } ?: "" // todo #46
        }
    }

    override fun getCodeString(file: KSFile): String {
        return fileCode(file.filePath)
    }

    private fun fileCode(fileName: String): String {
        val path = Path(fileName)
        return Files.readString(path)
    }
}
