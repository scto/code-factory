import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
    alias(libs.plugins.vanniktech)
    `java-gradle-plugin`
    kotlin("kapt")
}

group = "io.github.antonbutov"
version = "0.0.7"

dependencies {
    ksp(libs.autoservice.ksp)
    // ksp(libs.code.factory)

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

private val buildDirectory = project.rootProject.layout.buildDirectory.asFile.get()

 publishing {
    repositories {
        maven {
            name = "localBuild"
            url = buildDirectory.resolve("localMaven").toURI()
        }
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
        if (project.findProperty("signing") == "true") {
            signAllPublications()
        }
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

private val buildConfig = "${project.layout.buildDirectory.asFile.get().path}/generated/buildConfig"

sourceSets["main"].kotlin.srcDir(buildConfig)

tasks.register("generateBuildConfig") {
    doLast {
        val file = file("$buildConfig/BuildConfig.kt")
        file.parentFile.mkdirs()
        file.writeText(
            """
            object BuildConfig {
                const val KOTLIN_VERSION = "${libs.versions.kotlin.get()}"
                const val KSP_VERSION = "${libs.versions.ksp.get()}"
                const val PROCESSOR_VERSION = "$version"
            }
            """.trimIndent(),
        )
    }
}

tasks.named("build") {
    dependsOn("generateBuildConfig")
}
tasks.named("test") {
    dependsOn("generateBuildConfig")
}
 tasks.named("build") {
    dependsOn(":code-factory-processor:publishToMavenLocal")
 }
 tasks.named("test") {
    dependsOn(":code-factory-processor:publishToMavenLocal")
 }
tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
