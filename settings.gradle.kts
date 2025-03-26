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
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "code-factory"

include("code-factory-processor")
include("sample")
include("integration-tests")
