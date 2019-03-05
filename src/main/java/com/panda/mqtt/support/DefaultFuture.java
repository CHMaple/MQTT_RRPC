package com.panda.mqtt.support;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created With MqttClient
 *
 * @author ChenHao
 * @date 2019/3/4
 * Target
 */
public class DefaultFuture {

	private final Lock lock = new ReentrantLock();

	private final Condition done = lock.newCondition();

	private volatile byte[] payload;

	private boolean isDone() {
		return payload != null;
	}

	public String get(int timeout) throws TimeoutException {
		if (timeout <= 0) {
			timeout = 1000;
		}
		if (!isDone()) {
			long start = System.currentTimeMillis();
			lock.lock();
			try {
				while (!isDone()) {
					done.await(timeout, TimeUnit.MILLISECONDS);
					if (isDone() || System.currentTimeMillis() - start > timeout) {
						break;
					}
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				lock.unlock();
			}
			if (!isDone()) {
				throw new TimeoutException("time out");
			}
		}
		return new String(payload, StandardCharsets.UTF_8);
	}

	public void doReceived(byte[] res) {
		lock.lock();
		try {
			payload= res;
			done.signal();
		} finally {
			lock.unlock();
		}
	}
}
