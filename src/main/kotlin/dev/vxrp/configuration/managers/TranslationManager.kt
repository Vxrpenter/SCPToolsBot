package dev.vxrp.configuration.managers

import com.charleskorn.kaml.Yaml
import dev.vxrp.configuration.loaders.Translation
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path

class TranslationManager {
    private val logger = LoggerFactory.getLogger(TranslationManager::class.java)

    fun create(dir: String, files: List<Path>) {
        Files.createDirectories(Path("$dir/lang/"))

        for (file in files) {
            val content = TranslationManager::class.java.getResourceAsStream(file.toString())

            val path = Path("$dir$file")

            val currentFile = path.toFile()
            if (!currentFile.exists()) {
                currentFile.createNewFile()
                logger.info("Created translation file $dir$file")

                if (content != null) {
                    currentFile.appendBytes(content.readBytes())
                    logger.info("Wrote contents to $dir$file")
                }
            }
        }
    }

    fun query(dir: String, lang: String): Translation {
        val currentFile = Path("$dir/lang/$lang.yml").toFile()
        logger.debug("Query translation file {}{}", dir, currentFile)
        val result = Yaml.default.decodeFromString(Translation.serializer(), currentFile.readText())
        logger.debug("Query of translation file {}{} completed", dir, currentFile)
        return result
    }
}