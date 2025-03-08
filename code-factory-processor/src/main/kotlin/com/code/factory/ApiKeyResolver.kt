package com.code.factory

import java.io.File
import java.util.Properties
import javax.inject.Inject

interface ApiKeyResolver {
    fun resolve(): String?
}

class ApiKeyResolverImpl
    @Inject
    constructor(
        private val basePathProvider: BasePathProvider,
    ) : ApiKeyResolver {
        override fun resolve(): String? = loadLocalProperties(basePathProvider.getBasePath())?.getProperty("API_KEY")

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
