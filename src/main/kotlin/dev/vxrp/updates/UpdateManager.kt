package dev.vxrp.updates

import dev.vxrp.configuration.data.Config
import dev.vxrp.updates.handler.UpdateHandler
import dev.vxrp.updates.handler.UpdatesFileHandler
import dev.vxrp.util.coroutines.Timer
import dev.vxrp.util.coroutines.updatesScope
import kotlin.time.Duration.Companion.hours

class UpdateManager() {

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
            UpdateHandler().checkForUpdatesByTag(config, "https://api.github.com/repos/Vxrpenter/SCPToolsBot/git/refs/tags")
        }
    }
}