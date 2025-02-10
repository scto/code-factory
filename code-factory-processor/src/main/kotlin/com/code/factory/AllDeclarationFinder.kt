package com.code.factory

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSDeclaration

interface AllDeclarationFinder {
    fun getAllDeclaration(resolver: Resolver): Sequence<KSDeclaration>
}

internal class AllDeclarationFinderImpl : AllDeclarationFinder {
    override fun getAllDeclaration(resolver: Resolver): Sequence<KSDeclaration> {
        return resolver.getAllFiles().getAllDeclarations()
    }
}

fun allDeclarationFinder(): AllDeclarationFinder = AllDeclarationFinderImpl()
