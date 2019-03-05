package com.panda.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created With ems-parent
 *
 * @author ChenHao
 * @date 2019/1/23
 * Target
 */

public class PushCallback implements MqttCallback {

	@Override
	public void connectionLost(Throwable cause) {
		// 连接丢失后，一般在这里面进行重连
		System.out.println("连接断开，可以做重连");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			System.out.println("deliveryComplete---------" + token.getMessage().getId() + token.isComplete());
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// subscribe后得到的消息会执行到这里面
		System.out.println("                接收消息主题 : " + topic);
		System.out.println("                接收消息Qos : " + message.getQos());
		System.out.println("                接收消息内容 : " + new String(message.getPayload()));
	}
}
