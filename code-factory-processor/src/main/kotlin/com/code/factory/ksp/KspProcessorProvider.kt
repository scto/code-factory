package com.code.factory.ksp

import com.code.factory.DaggerProcessorComponent
import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

@AutoService(SymbolProcessorProvider::class)
class KspProcessorProvider() : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return DaggerProcessorComponent
            .builder()
            .logger(environment.logger)
            .codeGenerator(environment.codeGenerator)
            .build()
            .getKSPProcessor()
    }
}
