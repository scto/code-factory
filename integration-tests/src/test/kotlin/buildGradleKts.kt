val buildGradleKts =
    """
    plugins {
        kotlin("jvm") version "${BuildConfig.KOTLIN_VERSION}"
        id("com.google.devtools.ksp") version "${BuildConfig.KSP_VERSION}"
    }
    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        implementation(kotlin("stdlib"))
        ksp("io.github.antonbutov:code-factory-processor:${BuildConfig.PROCESSOR_VERSION}")
    }
    """.trimIndent()
