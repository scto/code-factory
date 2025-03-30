import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.File

class GenerateTest : StringSpec({
    "should generate file via KSP without package" {

        val project = generateProject(File("src/test/kotlin/ForGenerationWithOutPackage.kt"))

        val generated = project.resolve("build/generated/ksp/main/kotlin/ForGenerate.kt")
        generated.exists() shouldBe true
    }

    "should generate file via KSP with package" {

        val project = generateProject(File("src/test/kotlin/ForGenerationWithPackage.kt"))

        val generated = project.resolve("build/generated/ksp/main/kotlin/com/example/ForGenerate.kt")
        generated.exists() shouldBe true
    }
})

private fun generateProject(fileForGeneration: File): TestProject {
    val project =
        createTestProject()
            .withLocalProperties("API_KEY=test")
            .withBuildScript(buildGradleKts)
            .withKotlinSource(
                "com/example/MyClass.kt",
                fileForGeneration,
            )

    val result = project.build("build")
    result.output.contains("BUILD SUCCESSFUL") shouldBe true
    println(result.output)
    return project
}
