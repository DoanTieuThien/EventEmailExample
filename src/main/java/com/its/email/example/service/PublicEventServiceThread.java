package com.its.email.example.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.its.email.example.model.ChannelModel;
import com.its.email.example.model.ResponseModel;

public class PublicEventServiceThread extends ITSServiceThreadBase {
	@Autowired
	@Qualifier("sessionManagerMap")
	public ConcurrentHashMap<String, ChannelModel> sessionManagerMap = null;
	@Autowired
	@Qualifier("publicEventEmailQueue")
	private LinkedBlockingQueue<ResponseModel> publicEventEmailQueue = null;
	private ObjectMapper objectMapper = null;

	@Override
	protected void init() throws Exception {
		this.objectMapper = new ObjectMapper();
	}

	@Override
	protected void process() throws Exception {
		while (this.miThreadState != 0) {
			try {
				ResponseModel responseModel = this.publicEventEmailQueue != null ? this.publicEventEmailQueue.poll()
						: null;

				if (responseModel == null) {
					Thread.sleep(100);
					continue;
				}
				if (this.sessionManagerMap == null || this.sessionManagerMap.size() == 0) {
					Thread.sleep(100);
					continue;
				}
				Enumeration<String> keys = this.sessionManagerMap.keys();
				List<String> removeKey = new ArrayList<String>();
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					ChannelModel channelModel = this.sessionManagerMap.get(key);
					if (!channelModel.sendData(this.objectMapper.writeValueAsString(responseModel))) {
						removeKey.add(key);
					}
				}
				for (String k : removeKey) {
					this.sessionManagerMap.remove(k);
				}
			} catch (Exception exp) {
				exp.printStackTrace();
			}
			Thread.sleep(100);
		}
	}

	@Override
	protected void end() throws Exception {
		this.objectMapper = null;
	}

}
