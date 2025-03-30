plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":code-factory-processor"))

    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.runner.junit5.jvm)
    testImplementation(gradleTestKit())
}

tasks.named("test") {
    dependsOn("ktlintFormat")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Test>().configureEach {
    testLogging {
        showStandardStreams = true
        events("passed", "failed", "skipped")
    }
}
