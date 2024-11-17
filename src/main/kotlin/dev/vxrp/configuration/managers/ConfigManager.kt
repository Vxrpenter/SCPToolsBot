package dev.vxrp.configuration.managers

import com.charleskorn.kaml.Yaml
import dev.vxrp.configuration.loaders.Config
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path

class ConfigManager {
    private val logger = LoggerFactory.getLogger(ConfigManager::class.java)

    fun create(dir: String, files: List<Path>){
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

    fun query(dir: String, file: Path): Config {
        val currentFile = Path("$dir$file").toFile();
        logger.debug("Query configuration file {}{}", dir, file)
        val result = Yaml.default.decodeFromString(Config.serializer(), currentFile.readText())
        logger.debug("Query of configuration file {}{} completed", dir, file)
        return result
    }
}