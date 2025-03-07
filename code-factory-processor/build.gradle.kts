import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.ksp)
    kotlin("jvm")
    id("maven-publish")
    alias(libs.plugins.vanniktech)
    `java-gradle-plugin`
    alias(libs.plugins.ktlint)
    kotlin("kapt")
}

group = "io.github.antonbutov"
version = "0.0.6"

dependencies {
    ksp(libs.autoservice.ksp)

    api(libs.compilation)
    implementation(platform(libs.openai.kotlin.bom))
    implementation(libs.openai.client)
    runtimeOnly(libs.ktor.okhttp)

    implementation(kotlin("stdlib"))
    implementation(libs.autoservice.annotations)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.tschuchortdev.testing.ksp)

    implementation(libs.mockk)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.runner.junit5.jvm)
}

tasks.withType<Test>().configureEach {
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

mavenPublishing {
    coordinates(
        groupId = project.group.toString(),
        artifactId = project.name,
        version = project.version.toString(),
    )

    mavenPublishing {
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()
    }

    pom {
        name.set("Code factory")
        description.set("You wrote tests, then AI generates code.")
        url.set("https://github.com/AntonButov/code-factory")
        scm {
            url.set("https://github.com/AntonButov/code-factory")
        }
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("antonbutov")
                name.set("Anton Butov")
                email.set("butov6101@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/AntonButov/code-factory.git")
            developerConnection.set("scm:git:ssh://git@github.com:AntonButov/code-factory.git")
            url.set("https://github.com/AntonButov/code-factory")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
