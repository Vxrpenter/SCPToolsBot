package dev.vxrp.updates

import dev.vxrp.configuration.data.Config
import dev.vxrp.updates.handler.UpdateHandler
import dev.vxrp.updates.handler.UpdatesFileHandler
import dev.vxrp.util.coroutines.Timer
import dev.vxrp.util.coroutines.updatesScope
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.hours

class UpdateManager() {
    private val logger = LoggerFactory.getLogger(UpdateManager::class.java)

    fun checkUpdated() {
        val dir = System.getProperty("user.dir")
        UpdatesFileHandler().create(dir)
        UpdateHandler().checkUpdated(UpdatesFileHandler().queryOld(dir), UpdatesFileHandler().queryNew(), false)
    }

    fun spinUpChecker(config: Config) {
        Timer().runWithTimer(
            1.hours,
            updatesScope
        ) {
            try {
                UpdateHandler().checkForUpdatesByTag(config, "https://api.github.com/repos/Vxrpenter/SCPToolsBot/git/refs/tags")
            } catch (_: Exception) {
                logger.error("Could not proceed with update check correctly")
            }
        }
    }
}