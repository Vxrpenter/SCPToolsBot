package dev.vxrp.updates

import dev.vxrp.updates.handler.UpdateHandler
import dev.vxrp.updates.handler.UpdatesFileHandler

class UpdateManager() {

    fun checkUpdated() {
        val dir = System.getProperty("user.dir")
        UpdatesFileHandler().create(dir)
        UpdateHandler().checkUpdated(UpdatesFileHandler().queryOld(dir), UpdatesFileHandler().queryNew(), false)
    }
}