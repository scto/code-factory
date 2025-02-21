package com.code.factory

import com.code.factory.coderesolver.CodeResolver

interface TestCodeFilter {
    fun getFilteredTestCode(
        declarations: Sequence<String>,
        basePath: String,
    ): Sequence<String>
}

fun testCodeFilter(
    testFilesResolver: TestFilesResolver,
    codeResolver: CodeResolver,
): TestCodeFilter = TestCodeFilterImpl(testFilesResolver, codeResolver)

class TestCodeFilterImpl(
    private val testFilesResolver: TestFilesResolver,
    private val codeResolver: CodeResolver,
) : TestCodeFilter {
    override fun getFilteredTestCode(
        declarationNames: Sequence<String>,
        basePath: String,
    ): Sequence<String> {
        val testFiles = testFilesResolver.getTestFiles(basePath)
        return testFiles
            .map {
                codeResolver.getCode(it.path)
            }
            .filter { codeOfTest ->
                declarationNames.any { it in codeOfTest }
            }
    }
}
