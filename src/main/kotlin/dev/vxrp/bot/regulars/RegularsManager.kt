package dev.vxrp.bot.regulars
import dev.vxrp.configuration.loaders.Config
import dev.vxrp.configuration.loaders.Translation

class RegularsManager(val config: Config, val translation: Translation) {


    init {
        RegularsFileHandler(config, translation)
    }

    fun syncRegulars(userId: String) {

    }

    fun reactivateSync(userId: String) {

    }

    fun deactivateSync(userId: String) {

    }

    fun removeSync(userId: String) {

    }
}