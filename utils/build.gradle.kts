plugins {
    kotlin("jvm")
    alias(libs.plugins.ktlint)
}

group = "com.code.factory"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.tschuchortdev.testing.ksp)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.runner.junit5.jvm)
}

tasks.test {
    useJUnitPlatform()
}
tasks.named("build") {
    dependsOn("ktlintFormat")
}

tasks.named("test") {
    dependsOn("ktlintFormat")
}

ktlint {
    debug.set(false)
    android.set(false)
    ignoreFailures.set(true)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
    }
}
kotlin {
    jvmToolchain(17)
}
