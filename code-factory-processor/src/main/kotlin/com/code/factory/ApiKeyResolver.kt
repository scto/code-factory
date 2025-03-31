package com.code.factory

import com.code.factory.ksp.BasePathProvider
import com.google.devtools.ksp.processing.Resolver
import java.io.File
import java.util.Properties
import javax.inject.Inject

interface ApiKeyResolver {
    fun resolve(resolver: Resolver): String?
}

class ApiKeyResolverImpl
    @Inject
    constructor(
        private val basePathProvider: BasePathProvider,
    ) : ApiKeyResolver {
        override fun resolve(resolver: Resolver): String? =
            basePathProvider.getBasePath(resolver)?.let {
                loadLocalProperties(it)?.getProperty("API_KEY")
            }

        private fun loadLocalProperties(path: String): Properties? {
            val localPropertiesFile = File(path, "local.properties")
            if (localPropertiesFile.exists()) {
                val properties = Properties()
                properties.load(localPropertiesFile.inputStream())
                return properties
            }
            return null
        }
    }
