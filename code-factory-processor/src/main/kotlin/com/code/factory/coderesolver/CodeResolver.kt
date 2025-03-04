package com.code.factory.coderesolver

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile

interface CodeResolver {
    fun getCodeString(declaration: KSDeclaration): String

    fun getCodeString(declaration: Sequence<KSDeclaration>): Sequence<String>

    fun getCodeString(file: KSFile): String

    fun getCode(filePath: String): String
}
