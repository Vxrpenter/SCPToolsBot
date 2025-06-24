/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */


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

    fun delete(dir: String) {
        val currentFile = Path("$dir$file").toFile()
        currentFile.delete()
    }

    fun override(dir: String) {
        val currentFile = Path("$dir$file").toFile()

        val content = queryNew()
        val currentContent = queryOld(dir)

        val jsonEncoder = Json { prettyPrint = true }

        val newContent = jsonEncoder.encodeToString<Updates>(Updates(
            content.version,
            updateList(content.configurationUpdate.toMutableList(), currentContent.configurationUpdate.toMutableList()),
            updateList(content.translationUpdates.toMutableList(), currentContent.translationUpdates.toMutableList()),
            updateList(content.regularsUpdate.toMutableList(), currentContent.regularsUpdate.toMutableList()),
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

    private fun updateList(newList: MutableList<UpdatesConfigurationSegment>, oldList: MutableList<UpdatesConfigurationSegment>): List<UpdatesConfigurationSegment> {
        val list = mutableListOf<UpdatesConfigurationSegment>()

        val extraElements = mutableListOf<UpdatesConfigurationSegment>()
        val currentExtraElements = mutableListOf<UpdatesConfigurationSegment>()

        if (newList.size > oldList.size) newList.forEach { if (!oldList.contains(it)) extraElements.add(it) }
        if (newList.size < oldList.size) oldList.forEach { if (!oldList.contains(it)) oldList.toMutableList().remove(it) }

        extraElements.forEach { newList.remove(it) }
        currentExtraElements.forEach { oldList.remove(it) }

        newList.zip(oldList) {config, currentConfig ->
            list.add(UpdatesConfigurationSegment(config.changed, currentConfig.regenerate, config.type, config.filename, config.location, config.upstream))
        }

        list.addAll(extraElements)
        return list
    }
}