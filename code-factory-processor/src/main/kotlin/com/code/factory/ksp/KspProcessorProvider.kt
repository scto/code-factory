package com.code.factory.ksp

import com.code.factory.DaggerProcessorComponent
import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

@AutoService(SymbolProcessorProvider::class)
class KspProcessorProvider() : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val component =
            DaggerProcessorComponent
                .builder()
                .logger(environment.logger)
                .codeGenerator(environment.codeGenerator)
                .build()

        val apiKeyResolver = component.getApiKeyResolver()
        return apiKeyResolver.resolve()?.let {
            component.getKSPProcessor()
        } ?: component.getSleepKSPProcessor()
    }
}
