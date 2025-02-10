import com.code.factory.writer.WriterData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.kotlin.konan.file.File
import java.nio.file.Files
import kotlin.io.path.Path

interface Storage {
    fun getDeclarationCode(): String?

    fun getNamesForTestFilter(): List<String>

    fun saveDeclarationsCode(declarationsCode: String)

    fun saveDeclarationsNames(names: List<String>)

    fun getInterfaceWithOutImplementation(): WriterData?

    fun setInterfaceWithOutImplementation(writerData: WriterData)

    fun clean()
}

fun storage(): Storage = StorageImp()

private val TmpDir = File("build/tmp/code-factory")
private val allDeclarationsFile = File(TmpDir, "DeclarationsCode.txt")
private val declarationsNamesFile = File(TmpDir, "DeclarationsNames.txt")
private val interfaceWithOutDeclarationFile = File(TmpDir, "InterfaceWithOutImplementation.txt")

internal class StorageImp() : Storage {
    private var allDeclarations: String? = readDeclarationsCode()
    private var interfaceWithOutImplementation: WriterData? = readInterfaceWithOutImplementation()

    init {
        if (!TmpDir.exists) {
            TmpDir.mkdirs()
        }
    }

    override fun getInterfaceWithOutImplementation(): WriterData? {
        return interfaceWithOutImplementation
    }

    override fun getDeclarationCode(): String? {
        return allDeclarations
    }

    override fun getNamesForTestFilter(): List<String> {
        return readDeclarationsNames()
    }

    override fun saveDeclarationsCode(declarationsCode: String) {
        allDeclarations = allDeclarations?.let {
            "$allDeclarations \n$declarationsCode"
        } ?: declarationsCode
        saveAllDeclarationsCode(declarationsCode)
    }

    override fun saveDeclarationsNames(names: List<String>) {
        declarationsNamesFile.writeText(Json.encodeToString(names))
    }

    private fun saveAllDeclarationsCode(allDeclarations: String) {
        allDeclarationsFile.writeText(allDeclarations)
    }

    private fun saveInterfaceWithOutImplementation(writerData: WriterData) {
        interfaceWithOutDeclarationFile.writeText(Json.encodeToString(writerData))
    }

    private fun readInterfaceWithOutImplementation(): WriterData? =
        runCatching {
            Json.decodeFromString<WriterData>(Files.readString(Path(interfaceWithOutDeclarationFile.path)))
        }.getOrNull()

    private fun readDeclarationsCode(): String? =
        runCatching {
            Files.readString(Path(allDeclarationsFile.path))
        }.getOrNull()

    private fun readDeclarationsNames(): List<String> = Json.decodeFromString(Files.readString(Path(declarationsNamesFile.path)))

    override fun setInterfaceWithOutImplementation(writerData: WriterData) {
        interfaceWithOutImplementation = writerData
        saveInterfaceWithOutImplementation(writerData)
    }

    override fun clean() {
        allDeclarations = null
        interfaceWithOutImplementation = null
        runCatching {
            allDeclarationsFile.delete()
        }
        runCatching {
            interfaceWithOutDeclarationFile.delete()
        }
        runCatching {
            declarationsNamesFile.delete()
        }
    }
}
