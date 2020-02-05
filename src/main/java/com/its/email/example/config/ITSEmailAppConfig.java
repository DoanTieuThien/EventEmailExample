package com.its.email.example.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.its.email.example.model.ChannelModel;
import com.its.email.example.model.EmailModel;
import com.its.email.example.model.ResponseModel;
import com.its.email.example.service.EmailServiceThread;
import com.its.email.example.service.ManagerConnectionThread;
import com.its.email.example.service.PublicEventServiceThread;

@Configuration
public class ITSEmailAppConfig {
	@Bean("emailServiceThread")
	public EmailServiceThread emailServiceThread() {
		EmailServiceThread emailServiceThread = new EmailServiceThread();
		emailServiceThread.start();
		return emailServiceThread;
	}

	@Bean("managerConnectionThread")
	public ManagerConnectionThread managerConnectionThread() {
		ManagerConnectionThread managerConnectionThread = new ManagerConnectionThread();
		managerConnectionThread.start();
		return managerConnectionThread;
	}

	@Bean("publicEventServiceThread")
	public PublicEventServiceThread publicEventServiceThread() {
		PublicEventServiceThread publicEventServiceThread = new PublicEventServiceThread();
		publicEventServiceThread.start();
		return publicEventServiceThread;
	}

	@Bean("requestEmailQueue")
	public LinkedBlockingQueue<EmailModel> requestEmailQueue() {
		return new LinkedBlockingQueue<EmailModel>(500000);
	}

	@Bean("publicEventEmailQueue")
	public LinkedBlockingQueue<ResponseModel> publicEventEmailQueue() {
		return new LinkedBlockingQueue<ResponseModel>(500000);
	}

	@Bean("queueNewConnection")
	public LinkedBlockingQueue<ChannelModel> queueNewConnection() {
		return new LinkedBlockingQueue<ChannelModel>(500000);
	}

	@Bean("sessionManagerMap")
	public ConcurrentHashMap<String, ChannelModel> sessionManagerMap() {
		return new ConcurrentHashMap<String, ChannelModel>();
	}
}
