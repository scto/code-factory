package com.code.factory

import com.code.factory.coderesolver.CodeResolver
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration

interface CodeFilter {
    fun getFilteredDeclarations(
        resolver: Resolver,
        interfaceWithOutDeclaration: Sequence<KSClassDeclaration>,
    ): Sequence<KSDeclaration>
}

fun codeFilter(
    getAllDeclarationFinder: AllDeclarationFinder,
    codeResolver: CodeResolver,
): CodeFilter = CodeFilterImpl(getAllDeclarationFinder, codeResolver)

internal class CodeFilterImpl(
    private val getAllDeclarationFinder: AllDeclarationFinder,
    private val codeResolver: CodeResolver,
) : CodeFilter {
    override fun getFilteredDeclarations(
        resolver: Resolver,
        interfaceWithOutDeclaration: Sequence<KSClassDeclaration>,
    ): Sequence<KSDeclaration> {
        val codeInterfaceWithOutDeclaration = codeResolver.getCodeString(interfaceWithOutDeclaration).joinToString("\n") // #81
        return getAllDeclarationFinder.getAllDeclaration(resolver)
            .filter {
                it.qualifiedName!!.asString() in codeInterfaceWithOutDeclaration
            }
    }
}
