package com.code.factory

import com.code.factory.usescases.visitors.Visitor
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSNode

fun <T : KSNode> Sequence<T>.getAllDeclarations(): Sequence<KSDeclaration> =
    buildSet<KSDeclaration> {
        this@getAllDeclarations.forEach {
            it.accept(
                visitor =
                    Visitor {
                        add(it)
                    },
                data = "",
            )
        }
    }.filter {
        it.qualifiedName?.asString() != "kotlin.Any"
    }.filter {
        it.qualifiedName?.asString() != "kotlin.Unit"
    }.asSequence()
