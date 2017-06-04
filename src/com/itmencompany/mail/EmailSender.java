package com.itmencompany.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
	final static Logger log = Logger.getLogger(EmailSender.class.getName());
	
	protected final static String senderEmail = "vasiliykolesov13@gmail.com";
	protected final static String senderSubject = "ITMEN No-Reply";

	public EmailSender() {
	}
	
	public void sendTextMessage(String recipientEmail, String subject, String msg) {
		try {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail, senderSubject));
			message.setRecipient(Message.RecipientType.TO,  new InternetAddress(recipientEmail, "Mr. User"));
			message.setSubject(subject);
			message.setText(msg);

			Transport.send(message);

			log.info("Msg to " + recipientEmail + " has been sent");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendHtmlMessage(String recipientEmail, String subject, Multipart multipart) {
		try {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail, senderSubject));
			message.setRecipient(Message.RecipientType.TO,  new InternetAddress(recipientEmail, "Mr. User"));
			message.setSubject(subject);
			message.setContent(multipart);

			Transport.send(message);

			log.info("Msg to " + recipientEmail + " has been sent");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
}
