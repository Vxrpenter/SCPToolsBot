
package dev.vxrp.updates.handler

import dev.vxrp.configuration.storage.ConfigPaths
import dev.vxrp.updates.data.Updates
import dev.vxrp.updates.data.UpdatesConfigurationSegment
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
    }

    fun override(dir: String) {
        val currentFile = Path("$dir$file").toFile()

        val content = queryNew()
        val currentContent = queryOld(dir)

        val jsonEncoder = Json { prettyPrint = true }

        val newContent = jsonEncoder.encodeToString<Updates>(Updates(
            content.version,
            updateList(content.configurationUpdate, currentContent.configurationUpdate),
            updateList(content.translationUpdates, currentContent.translationUpdates),
            updateList(content.regularsUpdate, currentContent.regularsUpdate),
            content.additionalInformation
        ))

        currentFile.writeBytes(newContent.toByteArray(Charsets.UTF_8))
    }

    fun setConfigPaths(updates: Updates) {
        val communalList = mutableListOf<UpdatesConfigurationSegment>()
        communalList.addAll(updates.configurationUpdate)
        communalList.addAll(updates.translationUpdates)
        communalList.addAll(updates.regularsUpdate)

        for (config in communalList) {
            if (config.filename == "config.yml") ConfigPaths().configPath = Path(config.location)
            if (config.filename == "ticket-settings.yml") ConfigPaths().ticketPath = Path(config.location)
            if (config.filename == "status-settings.yml") ConfigPaths().statusPath = Path(config.location)
            if (config.filename == "commands.json") ConfigPaths().commandsPath = Path(config.location)
            if (config.filename == "launch-configuration.json") ConfigPaths().launchConfigurationPath = Path(config.location)
            if (config.filename == "en_US.yml") ConfigPaths().enUsPath = Path(config.location)
            if (config.filename == "de_DE.yml") ConfigPaths().deDePath = Path(config.location)
        }
    }

    fun queryNew(): Updates {
        return Json.decodeFromString<Updates>(UpdatesFileHandler::class.java.getResourceAsStream(file.toString())?.readBytes()?.toString(Charsets.UTF_8)!!)
    }

    fun queryOld(dir: String): Updates {
        return Json.decodeFromString<Updates>(Path("$dir$file").toFile().readText())
    }

    private fun updateList(newList: List<UpdatesConfigurationSegment>, oldList: List<UpdatesConfigurationSegment>): List<UpdatesConfigurationSegment> {
        val list = mutableListOf<UpdatesConfigurationSegment>()
        newList.zip(oldList) {config, currentConfig -> list.add(UpdatesConfigurationSegment(config.changed, currentConfig.changed, config.type, config.filename, config.location, config.upstream)) }

        return list
    }
}