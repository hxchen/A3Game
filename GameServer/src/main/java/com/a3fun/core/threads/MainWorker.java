package com.a3fun.core.threads;

import com.a3fun.core.world.WorldWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 主线程 由Application启动
 */
@Slf4j
@Component
public class MainWorker extends SingleThreadTaskWorker<Object>{
    public static final int SHUTDOWN_WAIT_TIME = 5000;
    public MainWorker() {
        super(SHUTDOWN_WAIT_TIME);
    }
    public void putWorldWorker(WorldWorker worldWorker){
        addTask(worldWorker);
    }
    @Override
    protected void execute(Object task) {
        if (task instanceof WorldWorker){
            WorldWorker worker = (WorldWorker) task;
            worker.run();
        }else{
            log.info("Task:{task}" + task);
        }
    }

}
