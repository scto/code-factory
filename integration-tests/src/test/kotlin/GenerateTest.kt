import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.BuildResult
import java.io.File

class GenerateTest : StringSpec({
    "when files is empty processor should sleep" {
        val project =
            createTestProject()
                .withLocalProperties("API_KEY=test")
                .withBuildScript(buildGradleKts)

        val result = project.build("build")
        result.output.contains("BUILD SUCCESSFUL") shouldBe true
    }

    "when api key have not should sleep" {
        val result =
            generateProject(fileForGeneration = File("src/test/kotlin/ForGenerationWithOutPackage.kt"), apiKey = "OTHER_API_KEY=test")
        result.output shouldContain "Sleep"
    }

    "should generate file via KSP without package" {

        generateProject(File("src/test/kotlin/ForGenerationWithOutPackage.kt")) {
            val generated = resolve("build/generated/ksp/main/kotlin/ForGenerate.kt")
            generated.exists() shouldBe true
        }
    }

    "should generate file via KSP with package" {

        generateProject(File("src/test/kotlin/ForGenerationWithPackage.kt")) {
            val generated = resolve("build/generated/ksp/main/kotlin/com/example/ForGenerate.kt")
            generated.exists() shouldBe true
        }
    }
})

private fun generateProject(
    fileForGeneration: File,
    apiKey: String = "API_KEY=test",
    checkAction: TestProject.() -> Unit = {},
): BuildResult {
    val project =
        createTestProject()
            .withLocalProperties(apiKey)
            .withBuildScript(buildGradleKts)
            .withKotlinSource(
                "com/example/MyClass.kt",
                fileForGeneration,
            )

    val result = project.build("build")
    result.output.contains("BUILD SUCCESSFUL") shouldBe true
    println(result.output)
    checkAction(project)
    return result
}
