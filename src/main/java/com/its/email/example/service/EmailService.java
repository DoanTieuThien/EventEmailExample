package com.its.email.example.service;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
	private Session mailsession = null;
	private Transport transport = null;
	private String smtpServerMail = "";
	private int port = 587;
	private String userName = "";
	private String password = "";

	private void reConnect() throws Exception {
		if (!this.transport.isConnected()) {
			open(smtpServerMail, port, userName, password);
		}
	}

	public void open(String smtpServerMail, int port, String userName, String password) throws Exception {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			mailsession = Session.getInstance(props);
			transport = mailsession.getTransport(new URLName("smtp", smtpServerMail, port, null, userName, password));
			transport.connect();
			this.smtpServerMail = smtpServerMail;
			this.port = port;
			this.userName = userName;
			this.password = password;
		} catch (Exception exp) {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				transport = null;
			}
			throw exp;
		}
	}

	public MimeMessage createMessage(String sender, String recipient, String subject, String strContent)
			throws Exception {
		reConnect();
		MimeMessage message = new MimeMessage(mailsession);
		message.setFrom(new InternetAddress(sender));
		message.setSubject(subject);
		message.setContent(strContent, "text/html;charset=\"utf-8\"");
		message.setSentDate(new Date());
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
		return message;
	}

	public void sendMessage(MimeMessage message) throws Exception {
		reConnect();
		transport.sendMessage(message, message.getAllRecipients());
	}

	public void close() {
		if (transport != null) {
			try {
				transport.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			transport = null;
		}
	}
}
