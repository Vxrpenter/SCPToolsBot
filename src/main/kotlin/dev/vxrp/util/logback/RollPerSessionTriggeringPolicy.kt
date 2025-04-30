package dev.vxrp.util.logback

import ch.qos.logback.core.rolling.TriggeringPolicyBase
import java.io.File

class RollPerSessionTriggeringPolicy<E> : TriggeringPolicyBase<E>() {
    var doRolling: Boolean = true

    override fun isTriggeringEvent(activeFile: File?, event: E?): Boolean {
        if (doRolling) {
            doRolling = false
            return true
        }
        return false
    }
}