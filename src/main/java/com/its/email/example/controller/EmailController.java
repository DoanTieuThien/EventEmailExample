package com.its.email.example.controller;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.its.email.example.model.EmailModel;
import com.its.email.example.model.ResponseModel;

@RestController
@RequestMapping("/email")
@CrossOrigin("*")
public class EmailController {
	@Autowired
	@Qualifier("requestEmailQueue")
	private LinkedBlockingQueue<EmailModel> requestEmailQueue = null;
	@Autowired
	@Qualifier("publicEventEmailQueue")
	private LinkedBlockingQueue<ResponseModel> publicEventEmailQueue = null;
	
	@PostMapping("/send-email")
	public ResponseModel sendEmail(@RequestBody EmailModel emailModel) {
		ResponseModel res = new ResponseModel();

		try {
			String transactionId = UUID.randomUUID().toString();
			emailModel.setTransactionId(transactionId);
			this.requestEmailQueue.put(emailModel);
			res.setCode("API-00000");
			res.setDes("Server accept request successed, please wait event from server via id that i response for you");
			res.setPayload(transactionId);
		} catch (Exception exp) {
			exp.printStackTrace();
			res.setCode("API-99999");
			res.setDes("Error request " + exp.getMessage());
		}
		try {
			this.publicEventEmailQueue.put(res);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return res;
	}
}
