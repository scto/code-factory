package com.code.factory.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import javax.inject.Inject

interface SleepKspProcessor : SymbolProcessor

class SleepKspProcessorImpl
    @Inject
    constructor() : SleepKspProcessor {
        override fun process(resolver: Resolver): List<KSAnnotated> {
            return emptyList()
        }
    }
