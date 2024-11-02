package dev.vxrp.util.general;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RepeatTask {
    public static void repeatWithScheduledExecutorService(Runnable runnable, int delay, TimeUnit unit) {
        ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();
        threadPool.scheduleWithFixedDelay(runnable, 0, delay, unit);
    }
}
