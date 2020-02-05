package com.its.email.example.service;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.its.email.example.model.EmailModel;
import com.its.email.example.model.ResponseModel;

public class EmailServiceThread extends ITSServiceThreadBase {
	@Autowired
	@Qualifier("requestEmailQueue")
	private LinkedBlockingQueue<EmailModel> requestEmailQueue = null;
	@Autowired
	@Qualifier("publicEventEmailQueue")
	private LinkedBlockingQueue<ResponseModel> publicEventEmailQueue = null;
	

	private EmailService emailService = null;

	@Value("${com.its.app.server-mail}")
	private String smtpServerMail = "";
	@Value("${com.its.app.mail-port}")
	private String port = "587";
	@Value("${com.its.app.userName}")
	private String userName = "";
	@Value("${com.its.app.password}")
	private String password = "";

	@Override
	protected void init() throws Exception {
		if (this.smtpServerMail.equals("") || this.requestEmailQueue == null) {
			throw new Exception("Starting init");
		}
		this.emailService = new EmailService();
		this.emailService.open(smtpServerMail, Integer.parseInt(port), userName, password);
	}

	@Override
	protected void process() throws Exception {
		while (this.miThreadState != 0) {
			try {
				EmailModel emailModel = this.requestEmailQueue != null ? this.requestEmailQueue.poll() : null;

				if (emailModel == null) {
					Thread.sleep(100);
					continue;
				}
				ResponseModel responseModel = new ResponseModel();
				try {
					responseModel.setCode("SEM-00001");
					responseModel.setDes("Starting send email " + emailModel.getTransactionId());
					this.publicEventEmailQueue.put(responseModel);
					
					responseModel.setPayload(emailModel.getTransactionId());
					this.emailService.sendMessage(this.emailService.createMessage(userName, emailModel.getTo(),
							emailModel.getSubject(), emailModel.getContent()));
					responseModel.setCode("SEM-00000");
					responseModel.setDes("Send email " + emailModel.getTransactionId() + " successed");
				} catch (Exception exp) {
					responseModel.setCode("SEM-99999");
					responseModel.setDes("Send email error process " + exp.getMessage());
				}
				this.publicEventEmailQueue.put(responseModel);
			} catch (Exception exp) {
				exp.printStackTrace();
			}
			Thread.sleep(100);
		}
	}

	@Override
	protected void end() throws Exception {
		if (this.emailService != null) {
			this.emailService.close();
			this.emailService = null;
		}
	}

}
