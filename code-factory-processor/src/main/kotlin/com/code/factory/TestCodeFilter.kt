package com.code.factory

import Storage
import com.code.factory.coderesolver.CodeResolver
import com.code.factory.coderesolver.codeResolver
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSDeclaration

interface TestCodeFilter {
    fun getFilteredTestDeclarations(resolver: Resolver): Sequence<KSDeclaration>
}

fun testCodeFilter(
    storage: Storage,
    codeResolver: CodeResolver,
): TestCodeFilter = TestCodeFilterImpl(storage, codeResolver)

class TestCodeFilterImpl(
    private val storage: Storage,
    private val codeResolver: CodeResolver,
) : TestCodeFilter {
    override fun getFilteredTestDeclarations(resolver: Resolver): Sequence<KSDeclaration> {
        val declarationsNames = storage.getNamesForTestFilter()
        return resolver.getAllFiles()
            .filter {
                val codeOfTest = codeResolver.getCodeString(it)
                declarationsNames.any { it in codeOfTest }
            }.getAllDeclarations()
    }
}
