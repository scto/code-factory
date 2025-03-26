package com.code.factory.ksp

import com.code.factory.ApiKeyResolver
import com.code.factory.ProcessorBindModule
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import javax.inject.Inject

interface WorkKspProvider : SymbolProcessor

class KspProcessor
    @Inject
    constructor(
        private val workActorFactory: ProcessorBindModule.WorkActorFactory,
        private val sleepKspProcessor: SleepKspProcessor,
        private val apiKeyResolver: ApiKeyResolver,
    ) : WorkKspProvider {
        override fun process(resolver: Resolver): List<KSAnnotated> {
            val apiKey = apiKeyResolver.resolve(resolver)
            return apiKey?.let {
                workActorFactory
                    .create(resolver, apiKey)
                    .act()
            } ?: sleepKspProcessor.process(resolver)
        }
    }
