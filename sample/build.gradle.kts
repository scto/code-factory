plugins {
    alias(libs.plugins.ksp)
    kotlin("jvm")
}
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib"))
    ksp(project(":code-factory-processor"))

    testImplementation(kotlin("test"))
}
