package com.code.factory

import com.google.devtools.ksp.processing.Resolver

interface TestSourcePathResolver {
    fun getSourcesPath(mainResolver: Resolver): String
}

fun testSourcePathResolver(): TestSourcePathResolver {
    return GetTestSourcePathResolver()
}

internal class GetTestSourcePathResolver : TestSourcePathResolver {
    override fun getSourcesPath(mainResolver: Resolver): String {
        return "${mainResolver.getAllFiles().first().filePath.split("main").first()}test/"
    }
}
