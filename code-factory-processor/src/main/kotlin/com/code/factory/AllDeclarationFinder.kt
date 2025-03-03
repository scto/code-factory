package com.code.factory

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSDeclaration
import javax.inject.Inject

interface AllDeclarationFinder {
    fun getAllDeclaration(resolver: Resolver): Sequence<KSDeclaration>
}

class AllDeclarationFinderImpl
    @Inject
    constructor() : AllDeclarationFinder {
        override fun getAllDeclaration(resolver: Resolver): Sequence<KSDeclaration> {
            return resolver.getAllFiles().getAllDeclarations()
        }
    }
