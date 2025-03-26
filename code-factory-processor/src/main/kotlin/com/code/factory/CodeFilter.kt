package com.code.factory

import com.code.factory.coderesolver.CodeResolver
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import javax.inject.Inject

interface CodeFilter {
    fun getFilteredCodeDeclarations(
        resolver: Resolver,
        interfaceWithOutDeclaration: KSClassDeclaration,
    ): Sequence<KSDeclaration>
}

class CodeFilterImpl
    @Inject
    constructor(
        private val getAllDeclarationFinder: AllDeclarationFinder,
        private val codeResolver: CodeResolver,
    ) : CodeFilter {
        override fun getFilteredCodeDeclarations(
            resolver: Resolver,
            interfaceWithOutDeclaration: KSClassDeclaration,
        ): Sequence<KSDeclaration> {
            val codeInterfaceWithOutDeclarationCode = codeResolver.getCodeString(interfaceWithOutDeclaration)
            return getAllDeclarationFinder.getAllDeclaration(resolver)
                .filter {
                    it.simpleName.getShortName() in codeInterfaceWithOutDeclarationCode
                }
        }
    }
