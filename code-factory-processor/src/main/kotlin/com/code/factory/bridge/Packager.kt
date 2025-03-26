package com.code.factory.bridge

import jakarta.inject.Inject

interface Packager {
    fun addPackage(
        code: String,
        packageName: String,
    ): String
}

class PackagerImpl
    @Inject
    constructor() : Packager {
        override fun addPackage(
            code: String,
            packageName: String,
        ): String {
            val trimmedCode = code.trimIndent().trim()
            val hasPackage = trimmedCode.lines().firstOrNull()?.startsWith("package ") == true

            return if (hasPackage) {
                code
            } else {
                buildString {
                    appendLine("package $packageName")
                    appendLine()
                    append(trimmedCode)
                }
            }
        }
    }
