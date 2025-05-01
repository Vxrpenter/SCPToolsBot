package dev.vxrp.bot.regulars

import com.charleskorn.kaml.Yaml
import dev.vxrp.bot.regulars.data.Regulars
import dev.vxrp.bot.regulars.data.RegularsConfig
import dev.vxrp.bot.regulars.data.RegularsConfigRole
import dev.vxrp.bot.regulars.data.RegularsManifest
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation
import dev.vxrp.database.tables.database.RegularsTable
import kotlinx.serialization.json.Json
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.name

class RegularsFileHandler(config: Config, translation: Translation) {
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