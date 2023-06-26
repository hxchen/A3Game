package com.a3fun.core.threads;

import com.a3fun.core.util.JavaUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Slf4j
public abstract class SingleThreadTaskWorker<T> implements Runnable {
    private ConcurrentLinkedQueue<T> taskQueue = new ConcurrentLinkedQueue<>();
    private ExecutorService exec = Executors.newSingleThreadExecutor();
    private String name;
    private volatile Thread workerThread;
    private int shutdownWaitTime = 5000;
    protected SingleThreadTaskWorker(int shutdownWaitTime) {
        this.name = JavaUtils.getSimpleName(getClass());
        this.shutdownWaitTime = shutdownWaitTime;
    }
    @Override
    public void run() {
        log.info("SingleThreadTaskWorker start");
        Thread workerThread = Thread.currentThread();
        this.workerThread = workerThread;
        try{
            if (Thread.interrupted()){
                throw new InterruptedException();
            }
            T task;
            while ((task = taskQueue.poll()) != null){
                try{
                    execute(task);
                }catch (Throwable e){
                    log.error("throws exception", e);
                }
            }
        }catch (InterruptedException e){
            log.error("InterruptedException exception", e);
        }catch (Throwable e){
            log.error("throws exception", e);
        }
    }
    protected void addTask(T task){
        taskQueue.add(task);
    }

    protected abstract void execute(T task);
}
