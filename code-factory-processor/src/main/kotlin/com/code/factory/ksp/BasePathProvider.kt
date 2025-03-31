package com.code.factory.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import jakarta.inject.Inject

interface BasePathProvider {
    fun getBasePath(resolver: Resolver): String?
}

class BasePathProviderImpl
    @Inject
    constructor(
        private val logger: KSPLogger,
    ) : BasePathProvider {
        override fun getBasePath(resolver: Resolver): String? {
            val allFiles = resolver.getAllFiles()
            if (allFiles.toList().isEmpty()) { // for diagnostic that case https://github.com/AntonButov/code-factory/issues/149
                logger.warn("No files found")
            }
            return allFiles.firstOrNull()?.filePath?.split("src")?.first()
        }
    }
