package dev.vxrp.bot.regulars

import dev.vxrp.bot.regulars.data.Regulars
import dev.vxrp.bot.regulars.data.RegularsConfig
import dev.vxrp.bot.regulars.data.RegularsManifest
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import kotlinx.serialization.json.Json
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.name

class RegularsManager(val config: Config, val translation: Translation) {
    private val workingDirectory = System.getProperty("user.dir")

    init {
        Files.createDirectories(Path("$workingDirectory/SCPToolsBot/regulars/"))

        if (config.settings.regulars.createExample) createExamples()
    }

    private fun createExamples() {
        Files.createDirectories(Path("$workingDirectory/SCPToolsBot/regulars/example/"))

        val configFile = Path("$workingDirectory/SCPToolsBot/regulars/example/config.json").toFile()
        val manifestFile = Path("$workingDirectory/SCPToolsBot/regulars/example/manifest.json").toFile()

        if (!configFile.exists()) {
            configFile.createNewFile()

            val content = RegularsManager::class.java.getResourceAsStream("/SCPToolsBot/regulars/example/config.json")
            configFile.appendBytes(content!!.readBytes())
        }

        if (!manifestFile.exists()) {
            manifestFile.createNewFile()

            val content = RegularsManager::class.java.getResourceAsStream("/SCPToolsBot/regulars/example/manifest.json")
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
        val configFile = Path("$workingDirectory/SCPToolsBot/regulars/$folder/config.json").toFile()
        if (!configFile.exists()) return null

        return Json.decodeFromString(RegularsConfig.serializer(), configFile.readText())
    }

    private fun queryManifest(folder: String): RegularsManifest? {
        val manifestFile = Path("$workingDirectory/SCPToolsBot/regulars/$folder/manifest.json").toFile()
        if (!manifestFile.exists()) return null

        return Json.decodeFromString(RegularsManifest.serializer(), manifestFile.readText())
    }
}