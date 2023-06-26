package com.a3fun.core.threads;

import com.a3fun.core.util.JavaUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
@Slf4j
public abstract class SingleThreadWorker implements Runnable{
    private ExecutorService exec = Executors.newSingleThreadExecutor();

    private static final int STATE_INIT = 0;

    private static final int STATE_START = 1;

    private static final int STATE_STOP = 2;
    private volatile int state = STATE_INIT;

    private int shutdownWaitTime = 5000;
    // 记录当前线程
    private volatile Thread workerThread;
    private String name;

    protected SingleThreadWorker(int shutdownWaitTime) {
        this.name = JavaUtils.getSimpleName(getClass());
        this.shutdownWaitTime = shutdownWaitTime;
    }

    /**
     * 线程执行方法: 1.执行execute方法 2.捕获异常
     */
    @Override
    public void run() {
        this.workerThread = Thread.currentThread();
        while (this.state == STATE_START) {
            try {
                execute();
            } catch (InterruptedException ie) {
            } catch (Throwable e) {
                log.error(name + " throws exception", e);
            }
        }
    }

    /**
     * 启动线程
     */
    public void start(){
        if (state != STATE_INIT) {
            throw new IllegalStateException(name + " state is illegal. expected=" + STATE_INIT + ", actual=" + state);
        }

        this.state = STATE_START;

        exec.execute(new NamedRunnable(name, this));
    }

    /**
     * 停止线程
     */
    public void stop() {
        if (this.state != STATE_START) {
            return;
        }
        this.state = STATE_STOP;

        try {
            ExecutorUtils.terminate(shutdownWaitTime, TimeUnit.MILLISECONDS, exec);
        } catch (Exception e) {
            log.error("Failed to stop {}", name, e);
        }

        if (log.isInfoEnabled()) {
            log.info(name + " is stopped");
        }
    }

    /**
     * 子类实现需要执行的任务
     * @throws InterruptedException
     */
    protected abstract void execute() throws InterruptedException;
}
