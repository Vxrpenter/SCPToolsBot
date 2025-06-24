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

package dev.vxrp.bot.regulars.handler

import com.charleskorn.kaml.Yaml
import dev.vxrp.bot.regulars.data.Regulars
import dev.vxrp.bot.regulars.data.RegularsConfig
import dev.vxrp.bot.regulars.data.RegularsConfigRole
import dev.vxrp.bot.regulars.data.RegularsManifest
import dev.vxrp.configuration.data.Config
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.name

class RegularsFileHandler(config: Config) {
    private val workingDirectory = System.getProperty("user.dir")

    init {
        Files.createDirectories(Path("$workingDirectory/SCPToolsBot/regulars/"))

        if (config.settings.regulars.createExample) createExamples()
    }

    private fun createExamples() {
        Files.createDirectories(Path("$workingDirectory/SCPToolsBot/regulars/example/"))

        val configFile = Path("$workingDirectory/SCPToolsBot/regulars/example/config.yml").toFile()
        val manifestFile = Path("$workingDirectory/SCPToolsBot/regulars/example/manifest.yml").toFile()

        if (!configFile.exists()) {
            configFile.createNewFile()

            val content = RegularsFileHandler::class.java.getResourceAsStream("/SCPToolsBot/regulars/example/config.yml")
            configFile.appendBytes(content!!.readBytes())
        }

        if (!manifestFile.exists()) {
            manifestFile.createNewFile()

            val content = RegularsFileHandler::class.java.getResourceAsStream("/SCPToolsBot/regulars/example/manifest.yml")
            manifestFile.appendBytes(content!!.readBytes())
        }
    }

    fun query(): List<Regulars> {
        val regulars = mutableListOf<Regulars>()

        val folders = Files.walk(Path("$workingDirectory/SCPToolsBot/regulars/"))
            .filter(Files::isDirectory)
            .toList()

        for (folder in folders) {
            if (folder.name == "regulars") continue

            val config = queryConfig(folder.name)
            val manifest = queryManifest(folder.name)

            regulars.add(Regulars(folder.name, config!!, manifest!!))
        }

        return regulars
    }

    private fun queryConfig(folder: String): RegularsConfig? {
        val configFile = Path("$workingDirectory/SCPToolsBot/regulars/$folder/config.yml").toFile()
        if (!configFile.exists()) return null

        return Yaml.default.decodeFromString(RegularsConfig.serializer(), configFile.readText())
    }

    private fun queryManifest(folder: String): RegularsManifest? {
        val manifestFile = Path("$workingDirectory/SCPToolsBot/regulars/$folder/manifest.yml").toFile()
        if (!manifestFile.exists()) return null

        return Yaml.default.decodeFromString(RegularsManifest.serializer(), manifestFile.readText())
    }

    fun queryRoleFromConfig(name: String, roleId: String): RegularsConfigRole? {
        for (regular in query()) {
            if (regular.manifest.name != name) continue

            for (role in regular.config.roles) {
                if (role.id != roleId) continue
                return role
            }
            break
        }
        return null
    }
}