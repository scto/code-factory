package com.code.factory.ksp

import javax.inject.Inject

interface PhaseResolver {
    fun resolvePhase(paths: List<String>): Phases
}

class PhaseResolverImpl
    @Inject
    constructor() : PhaseResolver {
        override fun resolvePhase(paths: List<String>): Phases =
            when {
                paths.any { it.contains("generated/ksp") } -> Phases.Generated
                paths.any { it.contains("src/test") } -> Phases.Tests
                else -> Phases.Main
            }
    }

sealed class Phases {
    data object Generated : Phases()

    data object Tests : Phases()

    data object Main : Phases()
}
