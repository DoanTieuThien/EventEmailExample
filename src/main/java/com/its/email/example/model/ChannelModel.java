package com.its.email.example.model;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ChannelModel implements Serializable {
	private String channelId;
	private Date startDateTime;
	private WebSocketSession session;
	private CloseStatus closeStatus;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public CloseStatus getCloseStatus() {
		return closeStatus;
	}

	public void setCloseStatus(CloseStatus closeStatus) {
		this.closeStatus = closeStatus;
	}

	public void close() {
		if (this.session == null) {
			return;
		}
		try {
			this.session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.session = null;
	}

	public boolean isOpen() {
		return this.session != null && this.session.isOpen();
	}

	public boolean sendData(String data) {
		try {
			if (this.session == null) {
				return false;
			}
			TextMessage textMessage = new TextMessage(data.getBytes(Charset.forName("utf-8")));
			this.session.sendMessage(textMessage);
			return true;
		} catch (Exception exp) {
			exp.printStackTrace();
			return false;
		}
	}

	public boolean sendData(TextMessage data) {
		try {
			if (this.session == null) {
				return false;
			}
			this.session.sendMessage(data);
			return true;
		} catch (Exception exp) {
			exp.printStackTrace();
			return false;
		}
	}
}
