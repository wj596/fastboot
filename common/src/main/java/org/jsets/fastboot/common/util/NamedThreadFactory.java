package org.jsets.fastboot.common.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * 可命名的线程工厂
 * @author wj596
 *
 */
public class NamedThreadFactory implements ThreadFactory {

	private final String prefix;
	private final AtomicInteger count = new AtomicInteger(1);

	public NamedThreadFactory(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName(prefix + "_" + count.getAndIncrement());
		return thread;
	}

}
