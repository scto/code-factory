pluginManagement {

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

@file:Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "code-factory"

include("code-factory-processor")
include("utils")
include("integration-test")
