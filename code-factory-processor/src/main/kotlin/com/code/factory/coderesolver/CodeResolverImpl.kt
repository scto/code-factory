package com.code.factory.coderesolver

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import java.nio.file.Files
import javax.inject.Inject
import kotlin.io.path.Path

class CodeResolverImpl
    @Inject
    constructor() : CodeResolver {
        override fun getCodeString(declaration: KSDeclaration): String {
            return declaration.containingFile?.let { getCodeString(it) } ?: "" // todo #46
        }

        @Throws(AssertionError::class)
        override fun getCodeString(declaration: Sequence<KSDeclaration>): Sequence<String> {
            assert(declaration.toList().isNotEmpty())
            return declaration.map(::getCodeString)
        }

        override fun getCodeString(file: KSFile): String {
            return getCode(file.filePath)
        }

        override fun getCode(filePath: String): String {
            val path = Path(filePath)
            return Files.readString(path)
        }
    }
