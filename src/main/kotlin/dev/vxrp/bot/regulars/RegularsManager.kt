package dev.vxrp.bot.regulars
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation

class RegularsManager(val config: Config, val translation: Translation) {


    init {
        RegularsFileHandler(config, translation)
    }

    fun syncRegulars() {

    }

    fun reactivateSync() {

    }

    fun deactivateSync() {

    }

    fun removeSync() {

    }
}