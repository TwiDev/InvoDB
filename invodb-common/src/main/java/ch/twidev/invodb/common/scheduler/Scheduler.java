/*
 * Copyright (c) 2024. PREZIUSO Matteo - All Rights Reserved
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 *
 * Written by PREZIUSO Matteo, prezmatteo@gmail.com
 */

package ch.twidev.invodb.common.scheduler;

import java.util.concurrent.*;

public abstract class Scheduler implements Runnable {

    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void runTask(Runnable run) {
        executor.submit(run);
    }

    public static void cancelTasks() {
        executor.shutdown();
        scheduledExecutor.shutdown();
    }

    public ScheduledFuture scheduleAsyncDelayedTask(long delay, TimeUnit timeUnit) {
        return scheduledExecutor.schedule(this, delay, timeUnit);
    }

    public ScheduledFuture scheduleAsyncRepeatingTask(long start, long period, TimeUnit timeUnit) {
        return scheduledExecutor.scheduleAtFixedRate(this, start, period, timeUnit);
    }

    public void runTask() {
        Scheduler.runTask(this);
    }

}
