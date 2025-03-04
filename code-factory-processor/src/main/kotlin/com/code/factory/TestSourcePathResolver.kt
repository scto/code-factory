package com.code.factory

import com.google.devtools.ksp.processing.Resolver
import javax.inject.Inject

interface TestSourcePathResolver {
    fun getSourcesPath(mainResolver: Resolver): String
}

class TestSourcePathResolverImpl
    @Inject
    constructor() : TestSourcePathResolver {
        override fun getSourcesPath(mainResolver: Resolver): String {
            return "${mainResolver.getAllFiles().first().filePath.split("main").first()}test/"
        }
    }
