package com.its.email.example.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.socket.CloseStatus;

import com.its.email.example.model.ChannelModel;

public class ManagerConnectionThread extends ITSServiceThreadBase {
	/*
	 * log4j
	 */
	private static final Logger log = LoggerFactory.getLogger(ManagerConnectionThread.class);

	@Autowired
	@Qualifier("queueNewConnection")
	public LinkedBlockingQueue<ChannelModel> queueNewConnection = null;
	@Autowired
	@Qualifier("sessionManagerMap")
	public ConcurrentHashMap<String, ChannelModel> sessionManagerMap = null;

	@Override
	public void init() throws Exception {
		log.warn("Thread " + this.getThreadId() + ": is started");
	}

	@Override
	public void process() throws Exception {
		while (this.miThreadState != 0) {
			try {
				ChannelModel channelModel = this.queueNewConnection == null || this.queueNewConnection.isEmpty() ? null
						: this.queueNewConnection.poll();

				if (channelModel != null) {
					CloseStatus closeStatus = channelModel.getCloseStatus();

					if (closeStatus == null && channelModel.isOpen()) {
						// day la ket noi moi
						this.sessionManagerMap.put(channelModel.getChannelId(), channelModel);
						log.warn("Thread " + this.getThreadId() + ": have new connection with id "
								+ channelModel.getChannelId() + " from source "
								+ channelModel.getSession().getUri().getPath());
					} else {
						this.sessionManagerMap.remove(channelModel.getChannelId());
						log.warn("Thread " + this.getThreadId() + ": is closed with id " + channelModel.getChannelId()
								+ " from source " + channelModel.getSession().getUri().getPath());
					}
					Thread.sleep(10);
					continue;
				}
			} catch (Exception exp) {
				log.error("Thread " + this.getThreadId() + " process error", exp);
			}
			Thread.sleep(100);
		}
	}

	@Override
	public void end() throws Exception {
		log.warn("Thread " + this.getThreadId() + ": is stopped");
	}

}
