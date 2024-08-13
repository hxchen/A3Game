package com.a3fun.core.world;

import com.a3fun.core.threads.MainWorker;
import com.a3fun.core.threads.SingleThreadWorker;
import com.a3fun.pudding.Application;
import com.a3fun.pudding.service.WorldService;
import lombok.extern.slf4j.Slf4j;

/**
 * 调度器 每1秒执行一次
 */
@Slf4j
public class WorldScheduler extends SingleThreadWorker {
    private static final int TICK_PERIOD = 1000;
    public static final int SHUTDOWN_WAIT_TIME = 30000;
    private volatile boolean busyNotice;
    private MainWorker mainWorker = Application.getBean(MainWorker.class);
    private WorldWorker worldWorker = new WorldWorker(Application.getBean(WorldService.class));
    public void setBusyNotice(boolean busyNotice) {
        this.busyNotice = busyNotice;
    }
    public WorldScheduler() {
        super(SHUTDOWN_WAIT_TIME);
    }
    @Override
    protected void execute() throws InterruptedException {
        if (worldWorker.canSchedule()){
            worldWorker.beforeSchedule();
            mainWorker.putWorldWorker(worldWorker);
            worldWorker.afterSchedule();
        }
        Thread.sleep(TICK_PERIOD * 2 / 3);
        if (!worldWorker.isDone() && busyNotice ) {
            log.error("World is busy. state=" + worldWorker.getState());
        }

        Thread.sleep(TICK_PERIOD / 3);
    }
}
