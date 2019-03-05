package com.panda.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created With MqttClient
 *
 * @author ChenHao
 * @date 2019/3/5
 * Target
 */
public class MqttMessageClient {

	public MqttMessageClient(MqttClient client) {
		this.client = client;
	}

	private MqttClient client;

	/**
	 * 用于分辨消息属于哪个客户端
	 */
	private volatile AtomicInteger messageId = new AtomicInteger(1);

	private Integer getNextMessageId() {
		return messageId.getAndAdd(1);
	}

	private final static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(0, 10, 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {

		final AtomicLong count = new AtomicLong(1);

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setName("mqtt-message-" + count.getAndIncrement());
			return thread;
		}
	});

}
