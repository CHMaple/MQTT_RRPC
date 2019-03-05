package com.panda.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created With MqttClient
 *
 * @author ChenHao
 * @date 2019/3/4
 * Target
 */
public class SimpleListenerMain {

	private static final String HOST = "tcp://127.0.0.1:1883";

	private static final String CLIENT_ID = "CLIENT2";

	private static String userName = "admin";

	private static String passWord = "admin";

	public static void main(String[] args) {
		try {
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setUserName(userName);
			options.setPassword(passWord.toCharArray());
			options.setConnectionTimeout(10);
			options.setKeepAliveInterval(20);
			final ExecutorService executorService = Executors.newSingleThreadExecutor();
			MqttClient client = new MqttClient(HOST, CLIENT_ID, new MemoryPersistence());

			// 设置回调类
			client.setCallback(new PushCallback());
			// 连接
			client.connect(options);
			client.subscribe("CLIENT/" + CLIENT_ID, (topic, message) -> {
				String messageContent = new String(message.getPayload(), StandardCharsets.UTF_8);
				String id = messageContent.substring(messageContent.lastIndexOf("_")+1);
				System.out.println("receive id : " + id + ",payload : " + messageContent);
				//不能在回调线程中调用publish，会阻塞线程，所以使用线程池
				executorService.submit(() -> {
					try {
						MqttMessage m = new MqttMessage();
						m.setQos(0);
						m.setRetained(true);
						m.setPayload(("client receive : " + id).getBytes(StandardCharsets.UTF_8));
						client.getTopic("RRPC/" + CLIENT_ID + "/" + id).publish(m);
						System.out.println("response,topic:{RRPC/" + CLIENT_ID + "/" + id + "}");
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			});
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
