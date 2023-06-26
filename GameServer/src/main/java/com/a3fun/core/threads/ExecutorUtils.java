package com.a3fun.core.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 
 * @author zheng.sun
 */
public class ExecutorUtils {

	private ExecutorUtils() {
	}

	public static void terminate(int awaitTime, TimeUnit timeunit, ExecutorService exec) {
		terminate(awaitTime, timeunit, false, exec);
	}
	public static void terminate(int awaitTime, TimeUnit timeunit, ExecutorService... execs) {
		terminate(awaitTime, timeunit, true, execs);
	}
	public static void terminate(int awaitTime, TimeUnit timeunit, boolean shutdownNow, ExecutorService... execs) {
		long awaitNano = timeunit.toNanos(awaitTime);
		long lastTime = System.nanoTime();
		boolean interrupted = false;

		for (ExecutorService exec : execs) {
			if (shutdownNow) {
				exec.shutdownNow();
			} else {
				exec.shutdown();
			}

			boolean terminated = false;
			while (awaitNano > 0 && !terminated) {
				try {
					if (exec.awaitTermination(awaitTime, timeunit)) {
						terminated = true;
					}
				} catch (InterruptedException e) {
					interrupted = true;
				}
				long now = System.nanoTime();
				awaitNano = awaitNano - (now - lastTime);
				lastTime = now;
			}
		}

		if (interrupted) {
			Thread.currentThread().interrupt();
		}
	}


}
