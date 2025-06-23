/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 *  may obtain the license at
 *
 *  https://mit-license.org/
 *
 *  This software may be used commercially if the usage is license compliant. The software
 *  is provided without any sort of WARRANTY, and the authors cannot be held liable for
 *  any form of claim, damages or other liabilities.
 *
 *  Note: This is no legal advice, please read the license conditions
 */

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