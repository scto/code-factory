package com.code.factory.ksp

import com.google.devtools.ksp.processing.Resolver
import jakarta.inject.Inject

interface BasePathProvider {
    fun getBasePath(resolver: Resolver): String?
}

// #62
class BasePathProviderImpl
    @Inject
    constructor() : BasePathProvider {
        override fun getBasePath(resolver: Resolver): String? {
            return resolver.getAllFiles().firstOrNull()?.filePath?.let {
                return it.split("src").first()
            }
        }
    }
