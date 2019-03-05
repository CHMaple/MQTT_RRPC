package com.panda.mqtt.support;

import java.io.Serializable;

/**
 * Created With MqttClient
 *
 * @author ChenHao
 * @date 2019/3/5
 * Target
 */
public class MqttRrpcMessage implements Serializable {

	private static final long serialVersionUID = 264529015352268746L;

	private Integer messageId;

	private String messageContent;

	public MqttRrpcMessage(Integer messageId, String messageContent) {
		this.messageId = messageId;
		this.messageContent = messageContent;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	@Override
	public String toString() {
		return this.messageId + "_" + this.messageContent;
	}
}
