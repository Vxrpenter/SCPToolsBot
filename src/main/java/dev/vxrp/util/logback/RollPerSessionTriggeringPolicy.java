package dev.vxrp.util.logback;

import ch.qos.logback.core.rolling.TriggeringPolicyBase;

import java.io.File;

public class RollPerSessionTriggeringPolicy<E> extends TriggeringPolicyBase<E> {
    private static boolean doRolling = true;

    @Override
    public boolean isTriggeringEvent(File activeFile, E event) {
        if (doRolling) {
            doRolling = false;
            return true;
        }
        return false;
    }
}
