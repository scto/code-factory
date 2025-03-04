package com.code.factory

import com.code.factory.usescases.getClassKind
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import javax.inject.Inject

interface InterfaceFinder {
    fun getInterfacesWithOutImplementation(resolver: Resolver): Sequence<KSClassDeclaration>
}

class InterfaceFinderImpl
    @Inject
    constructor() : InterfaceFinder {
        override fun getInterfacesWithOutImplementation(resolver: Resolver): Sequence<KSClassDeclaration> {
            val interfaces = resolver.getClassKind(ClassKind.INTERFACE)
            val classes = resolver.getClassKind(ClassKind.CLASS)

            return interfaces.filter { interfaceItem ->
                classes.none { classItem ->
                    classItem.superTypes.any { superType ->
                        superType.resolve().declaration.qualifiedName?.asString() ==
                            interfaceItem.qualifiedName?.asString()
                    }
                }
            }
        }
    }

fun interfaceFinder(): InterfaceFinder = InterfaceFinderImpl()
