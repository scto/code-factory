package com.code.factory

import java.io.File
import javax.inject.Inject

interface TestFilesResolver {
    fun getTestFiles(basePath: String): Sequence<File>
}

class TestFilesResolverImpl
    @Inject
    constructor() : TestFilesResolver {
        override fun getTestFiles(basePath: String): Sequence<File> {
            return findKtFiles(File(basePath))
        }

        private fun findKtFiles(directory: File): Sequence<File> {
            return directory.walkTopDown()
                .filter { it.isFile && it.extension == "kt" }
        }
    }
