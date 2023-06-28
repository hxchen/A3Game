package com.a3fun.core.threads;

import com.a3fun.core.util.JavaUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 单线程任务处理器
 * @param <T>
 */
@Slf4j
public abstract class SingleThreadTaskWorker<T> implements Runnable {
    private ConcurrentLinkedQueue<T> taskQueue = new ConcurrentLinkedQueue<>();
    private ExecutorService exec = Executors.newSingleThreadExecutor();
    private String name;

    private static final int STATE_INIT = 0;

    private static final int STATE_START = 1;

    private static final int STATE_STOP = 2;
    private volatile int state = STATE_INIT;

    private volatile Thread workerThread;

    private AtomicBoolean notified = new AtomicBoolean();
    private int shutdownWaitTime = 5000;
    protected SingleThreadTaskWorker(int shutdownWaitTime) {
        this.name = JavaUtils.getSimpleName(getClass());
        this.shutdownWaitTime = shutdownWaitTime;
    }

    /**
     * run方法是任务处理器的主要执行逻辑，它会在单独的线程中被调用。
     * 首先，它会将当前线程保存在workerThread变量中，并进入一个循环。
     * 在循环中，它会检查任务处理器的状态（state）是否为STATE_START（即已启动状态）以及是否收到任务通知。
     * 如果没有收到任务通知，它会调用LockSupport.park()方法使当前线程阻塞，直到收到新的任务通知或者任务处理器的状态变为非启动状态。
     * 一旦收到任务通知，它会重置通知状态，并依次处理任务队列中的任务。它会不断从任务队列中取出任务，并调用execute方法执行任务。
     * 在执行任务时，如果发生异常，它会捕获异常并记录错误日志，继续处理下一个任务。
     * 当任务处理器的状态变为非启动状态时，循环结束，线程执行结束。
     * 总体来说，run方法是一个不断循环、处理任务队列中任务的过程。它在启动后会等待任务通知，收到通知后会执行任务，直到任务处理器的状态变为非启动状态。
     */
    @Override
    public void run() {
        log.info("SingleThreadTaskWorker start");
        Thread workerThread = Thread.currentThread();
        this.workerThread = workerThread;
        boolean interrupt = false;
        while (this.state == STATE_START) {
            while (!notified.get() && this.state == STATE_START) {
                LockSupport.park();
            }

            try {
                notified.set(false);
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                T task = null;
                while ((task = taskQueue.poll()) != null) {
                    try {
                        execute(task);
                    } catch (Throwable e) {
                        log.error(name + " throws exception", e);
                    }
                }
            } catch (InterruptedException e) {
                interrupt = false;
            } catch (Throwable e) {
                log.error(name + " throws exception", e);
            }
        }

        if (interrupt) {
            Thread.currentThread().interrupt();
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
    protected void addTask(T task){
        taskQueue.add(task);
        if (this.state == STATE_START) {
            if (!notified.get() && notified.compareAndSet(false, true)) {
                signal();
            }
        } else {
            if (taskQueue.remove(task)) {
                throw new RejectedExecutionException("Worker has already been shutdown");
            }
        }
    }

    private void signal() {
        LockSupport.unpark(this.workerThread);
    }

    protected abstract void execute(T task);
}
