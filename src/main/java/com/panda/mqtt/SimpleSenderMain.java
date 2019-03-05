package com.panda.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created With MqttClient
 *
 * @author ChenHao
 * @date 2019/3/4
 * Target
 */
public class SimpleSenderMain {

	private static final String HOST = "tcp://127.0.0.1:1883";

	private static final String CLIENT_ID = "CLIENT1";

	private static String userName = "admin";

	private static String passWord = "admin";

	public static void main(String[] args) throws InterruptedException {
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

			SimpleClient simpleClient = new SimpleClient(client);

			while (true) {
				System.out.println("         "+simpleClient.doSend("CLIENT2", "client heart:" + CLIENT_ID));
				Thread.sleep(10000);
			}

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
