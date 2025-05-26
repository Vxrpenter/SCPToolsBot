
package dev.vxrp.updates.handler

import dev.vxrp.updates.data.Updates
import kotlinx.serialization.json.Json
import java.nio.file.Files
import kotlin.io.path.Path

class UpdatesFileHandler {
    private val file = Path("/SCPToolsBot/configs/extra/updates.json")

    fun create(dir: String) {
        Files.createDirectories(Path("$dir/SCPToolsBot/configs/extra/"))

        val content = UpdatesFileHandler::class.java.getResourceAsStream(file.toString())
        val currentFile = Path("$dir$file").toFile()

        if (currentFile.exists()) return
        currentFile.createNewFile()

        if (content != null) {
            currentFile.appendBytes(content.readBytes())
        }
        UpdateHandler().checkUpdated(UpdatesFileHandler().queryOld(dir), UpdatesFileHandler().queryNew(), true)
    }

    fun override(dir: String) {
        val currentFile = Path("$dir$file").toFile()

        val content = queryNew()
        val currentContent = queryOld(dir)

        val jsonEncoder = Json {
            prettyPrint = true
        }

        val newContent = jsonEncoder.encodeToString<Updates>(Updates(
            content.version,
            currentContent.settings,
            content.configurationUpdate,
            content.translationUpdates,
            content.regularsUpdate,
            content.additionalInformation
        ))

        currentFile.writeBytes(newContent.toByteArray(Charsets.UTF_8))
    }

    fun queryNew(): Updates {
        return Json.decodeFromString<Updates>(UpdatesFileHandler::class.java.getResourceAsStream(file.toString())?.readBytes()?.toString(Charsets.UTF_8)!!)
    }

    fun queryOld(dir: String): Updates {
        return Json.decodeFromString<Updates>(Path("$dir$file").toFile().readText())
    }
}