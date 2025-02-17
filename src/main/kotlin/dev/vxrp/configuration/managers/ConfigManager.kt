package dev.vxrp.configuration.managers

import com.charleskorn.kaml.Yaml
import dev.vxrp.bot.status.data.Status
import dev.vxrp.bot.ticket.data.Ticket
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Settings
import dev.vxrp.database.tables.ApplicationTypeTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path

class ConfigManager {
    private val logger = LoggerFactory.getLogger(ConfigManager::class.java)

    fun create(dir: String, files: List<Path>) {
        Files.createDirectories(Path("$dir/configs/"))

        for (file in files) {
            val content = ConfigManager::class.java.getResourceAsStream(file.toString())

            val path = Path("$dir$file")

            val currentFile = path.toFile()
            if (!currentFile.exists()) {
                currentFile.createNewFile()
                logger.info("Created configuration file $dir$file")

                if (content != null) {
                    currentFile.appendBytes(content.readBytes())
                    logger.info("Wrote contents to $dir$file")
                }
            }
        }
    }

    fun query(configLocation: Path, statusLocation: Path, ticketLocation: Path): Config {
        val configFile = configLocation.toFile()
        val statusFile = statusLocation.toFile()
        val ticketFile = ticketLocation.toFile()

        val settings = Yaml.default.decodeFromString(Settings.serializer(), configFile.readText())
        val status = Yaml.default.decodeFromString(Status.serializer(), statusFile.readText())
        val ticket = Yaml.default.decodeFromString(Ticket.serializer(), ticketFile.readText())

        return Config(settings, status, ticket)
    }

    fun databaseManagement(configLocation: Path, statusLocation: Path, ticketLocation: Path) {
        val configs = query(configLocation, statusLocation, ticketLocation)

        val idList = mutableListOf<String>()

        for (type in configs.ticket.applicationTypes) {
            if (ApplicationTypeTable().exists(type.roleID)) {
                ApplicationTypeTable().addToDatabase(type.roleID, false, null,null)
            }

            idList.add(type.roleID)
        }

        ApplicationTypeTable().deleteRedundantValues(idList)
    }
}