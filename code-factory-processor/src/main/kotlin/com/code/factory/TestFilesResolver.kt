package com.code.factory

import java.io.File

interface TestFilesResolver {
    fun getTestFiles(basePath: String): Sequence<File>
}

fun testFileResolver(): TestFilesResolver {
    return TestFilesResolverImpl()
}

internal class TestFilesResolverImpl : TestFilesResolver {
    override fun getTestFiles(basePath: String): Sequence<File> {
        return findKtFiles(File(basePath))
    }

    private fun findKtFiles(directory: File): Sequence<File> {
        return directory.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
    }
}
