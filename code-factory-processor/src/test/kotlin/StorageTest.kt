import com.code.factory.writer.WriterData
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class StorageTest : StringSpec({

    lateinit var storage: Storage

    beforeTest {
        storage = StorageImp()
    }

    afterTest {
        StorageImp().clean()
    }

    "read default all declarations" {
        val defaultAllDeclarations = storage.getDeclarationCode()

        defaultAllDeclarations shouldBe null
    }

    "read allDeclaration test immediately" {
        val allDeclarations = "Some code."
        storage.saveDeclarationsCode(allDeclarations)

        val allDeclarationsRead = storage.getDeclarationCode()
        allDeclarationsRead shouldBe allDeclarations
    }

    "read test other phase" {
        storage.clean()
        val allDeclarations = "Some code other phase."
        storage.saveDeclarationsCode(allDeclarations)

        val newStorage = StorageImp()
        val allDeclarationsRead = newStorage.getDeclarationCode()
        allDeclarationsRead shouldContain allDeclarations
    }

    "when add first then add second should contaminate" {
        val firstDeclaration = "Some code first."
        storage.saveDeclarationsCode(firstDeclaration)

        val secondDeclaration = "Some code second."
        storage.saveDeclarationsCode(secondDeclaration)

        val allDeclarationsRead = storage.getDeclarationCode()
        allDeclarationsRead shouldContain firstDeclaration
        allDeclarationsRead shouldContain secondDeclaration
    }

    "when clean should return null" {
        val allDeclarationsRead = storage.getDeclarationCode()
        allDeclarationsRead shouldBe null
        val interfaceWithOutImplementation = storage.getInterfaceWithOutImplementation()
        interfaceWithOutImplementation shouldBe null
    }

    "read default interfaceWithOutImplementation should be null" {
        val interfaceWithOutImplementation = storage.getInterfaceWithOutImplementation()

        interfaceWithOutImplementation shouldBe null
    }

    val interfaceWithOutImplementation =
        WriterData(
            code = "Some code interfaceWithOutImplementation.",
            packageName = "packageName",
            name = "name",
        )

    "save then read interfaceWithOutImplementation should return some immediate" {
        storage.setInterfaceWithOutImplementation(interfaceWithOutImplementation)
        val interfaceWithOutImplementationRead = storage.getInterfaceWithOutImplementation()
        interfaceWithOutImplementationRead shouldBe interfaceWithOutImplementation
    }

    "save then read interfaceWithOutImplementation should return some after restart" {
        storage.setInterfaceWithOutImplementation(interfaceWithOutImplementation)

        val interfaceWithOutImplementationRead = StorageImp().getInterfaceWithOutImplementation()
        interfaceWithOutImplementationRead shouldBe interfaceWithOutImplementation
    }

    val declarationNames = listOf("FirstName", "SecondName")

    "save then save again then read interfaceWithOutImplementation should return last" {
        storage.saveDeclarationsNames(declarationNames)
        val declarationNamesRead = storage.getNamesForTestFilter()
        declarationNamesRead shouldBe declarationNames
    }

    "save then save again then read interfaceWithOutImplementation should return last after restart" {
        storage.saveDeclarationsNames(declarationNames)
        val declarationNamesRead = StorageImp().getNamesForTestFilter()
        declarationNamesRead shouldBe declarationNames
    }
})
