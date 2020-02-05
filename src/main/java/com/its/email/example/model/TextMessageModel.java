package com.its.email.example.model;

import java.io.Serializable;

public class TextMessageModel implements Serializable {
	private String channelId;
	private String payload;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
}
