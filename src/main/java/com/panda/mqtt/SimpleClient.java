package com.panda.mqtt;

import com.panda.mqtt.support.DefaultFuture;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created With MqttClient
 *
 * @author ChenHao
 * @date 2019/3/4
 * Target
 */
public class SimpleClient {

	public SimpleClient(MqttClient client) {
		this.client = client;
	}

	private volatile AtomicInteger MESSAGE_ID = new AtomicInteger(1);

	private MqttClient client;

	private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

	private Integer getNextMessageId() {
		return MESSAGE_ID.getAndAdd(1);
	}

	public String doSend(String topic, String messageContent) {
		MqttTopic pubTopic = client.getTopic("CLIENT/" + topic);
		if (null == pubTopic) {
			throw new RuntimeException("topic 不存在");
		}
		Integer messageId = getNextMessageId();
		MqttMessage message = new MqttMessage();
		message.setQos(0);
		message.setPayload((messageContent + "_" + messageId).getBytes(StandardCharsets.UTF_8));
		try {
			pubTopic.publish(message);

			DefaultFuture result = new DefaultFuture();
			EXECUTOR_SERVICE.submit(() -> {
				try {
					System.out.println("subscribe topic:{RRPC/" + topic + "/" + messageId + "}");
					client.subscribe("RRPC/" + topic + "/" + messageId, (topic1, message1) -> {
						result.doReceived(message1.getPayload());
					});
				} catch (MqttException e) {
					e.printStackTrace();
				}
			});

			return result.get(2000);
		} catch (MqttException | TimeoutException e) {
			e.printStackTrace();
			return "time out";
		} finally {
			try {
				client.unsubscribe("RRPC/" + topic + "/M" + message.getId());
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}
}
