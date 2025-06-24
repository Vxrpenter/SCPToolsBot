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

package dev.vxrp.configuration.handler

import com.charleskorn.kaml.Yaml
import dev.vxrp.configuration.data.Translation
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.system.exitProcess

class TranslationFileHandler {
    private val logger = LoggerFactory.getLogger(TranslationFileHandler::class.java)

    fun create(dir: String, files: List<Path>) {
        Files.createDirectories(Path("$dir/SCPToolsBot/lang/"))

        for (file in files) {
            val content = TranslationFileHandler::class.java.getResourceAsStream(file.toString())

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
        val currentFile = Path("$dir/SCPToolsBot/lang/$lang.yml").toFile()

        if (!currentFile.exists()) {
            logger.error("Could not load configuration set with name: {}", lang)
            exitProcess(2)
        }

        logger.debug("Query translation file {}{}", dir, currentFile)
        val result = Yaml.default.decodeFromString(Translation.serializer(), currentFile.readText())
        logger.debug("Query of translation file {}{} completed", dir, currentFile)
        return result
    }
}