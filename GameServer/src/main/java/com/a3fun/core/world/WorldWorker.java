package com.a3fun.core.world;

import com.a3fun.pudding.service.WorldService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
public class WorldWorker implements Runnable {
    private WorldService worldService;

    public static final int INIT = 0;
    public static final int RUNNABLE = 1;
    public static final int RUNNING = 2;
    public static final int FINISHED = 3;

    private AtomicInteger state = new AtomicInteger(INIT);

    public WorldWorker(WorldService worldService){
        this.worldService = worldService;
    }
    @Override
    public void run() {
        log.info("World worker is running ...");
        state.set(RUNNING);
        try{
            worldService.getWorld().tick(System.currentTimeMillis());
        }catch (Throwable e) {
            log.error("World tick throws exception", e);
        }finally {
            state.set(FINISHED);
        }
    }

    public boolean canSchedule(){
        int s = state.get();
        return s == FINISHED || s == INIT;
    }

    public void beforeSchedule() {
        state.set(INIT);
    }

    public void afterSchedule() {
        state.compareAndSet(INIT, RUNNABLE);
    }

    public boolean isDone() {
        return state.get() == FINISHED;
    }

    public int getState() {
        return state.get();
    }
}
