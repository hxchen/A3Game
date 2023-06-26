package com.a3fun.core.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamedRunnable implements Runnable {

	private final static Logger log = LoggerFactory.getLogger(NamedRunnable.class);

	private final String newName;

	private final Runnable runnable;

	public NamedRunnable(String newName, Runnable runnable) {
		if (newName == null) {
			throw new NullPointerException("newName");
		}
		if (runnable == null) {
			throw new NullPointerException("runnable");
		}

		this.newName = newName;
		this.runnable = runnable;
	}

	@Override
	public void run() {
		final Thread currentThread = Thread.currentThread();
		final String oldThreadName = currentThread.getName();

		boolean renamed = false;
		if (!oldThreadName.equals(newName)) {
			try {
				currentThread.setName(newName);
				renamed = true;
			} catch (SecurityException e) {
				if (log.isWarnEnabled()) {
					log.warn("Failed to rename a thread due to security restriction.", e);
				}
			}
		}

		try {
			runnable.run();
		} finally {
			if (renamed) {
				currentThread.setName(oldThreadName);
			}
		}
	}

}
