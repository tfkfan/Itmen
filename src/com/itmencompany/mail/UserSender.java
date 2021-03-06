package com.itmencompany.mail;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringEscapeUtils;

import com.itmencompany.common.ServerUtils;
import com.itmencompany.mvc.datastore.entities.AppUser;
import com.itmencompany.mvc.datastore.entities.IncomingInfo;

public class UserSender extends EmailSender{
	private ServletContext context;
	private static final String theme = "ITMEN | No Reply";
	
	public UserSender(ServletContext context) {
		this.context = context;
	}
	
	public void sendCampaignsAnswerNotification(AppUser appUser, IncomingInfo info) throws MessagingException, IOException{
		sendHtmlMessage(appUser.getEmail(), theme, getInfoMultipart(info, appUser.getUserName()));
	}
	
	public Multipart getInfoMultipart(IncomingInfo info, String userName)
			throws MessagingException, IOException {
		List<String> photos = info.getImages();
		String photos_str = "";
		if (photos != null && !photos.isEmpty()) {
			String photosBufHtml = "<div>";
			for (String photo : photos)
				photosBufHtml += "<img src='" + photo + "' />";
			photosBufHtml += "</div>";
			photos_str = photosBufHtml;
		}

		String phone = " - ";
		if(info.getCampaign_phone() != null)
			phone = info.getCampaign_phone();
		
		String email = " - ";
		if(info.getCampaign_email() != null)
			email = info.getCampaign_email();
		
		String title = " - ";
		if (info.getTitle() != null)
			title = info.getTitle();
		
		String description = " - ";
		if (info.getDescription() != null)
			description =  info.getDescription();
		
		String length = " - ";
		if (info.getLength() != null)
			length = info.getLength();
		
		String height = " - ";
		if (info.getHeight() != null)
			height = info.getHeight();
		
		String material = " - ";
		if (info.getMaterial() != null)
			material = info.getMaterial();
		
		String release_date = " - ";
		if (info.getRelease_date() != null)
			release_date =  info.getRelease_date();
		
		String cost = " - ";
		if (info.getCost() != null)
			cost = info.getCost();
		
		String campaign = " - ";
		if (info.getCampaign_title() != null)
			campaign =  info.getCampaign_title();
		
		String add_info = " - ";
		if (info.getAddInfo() != null)
			add_info  = info.getAddInfo();
	
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy  HH:mm:ss ");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
		
		String date = dateFormat.format(info.getDoModified());
		String serviceName = ServerUtils.SERVICE_NAME;
		String serviceDomain = ServerUtils.SERVICE_DOMAIN;
		String serviceUrl = ServerUtils.SERVICE_URL;
		
		add_info = ServerUtils.insertDiv(add_info);
		description = ServerUtils.insertDiv(description);
		
		GenerateUserEmail gue = new GenerateUserEmail(title, photos_str, length,  height, material, add_info, date, release_date,
				userName, campaign, phone, email, cost, description, serviceName, serviceDomain, serviceUrl);
		String mailStr = gue.generateEmailTemplate(context, ServerUtils.RESOURCES_PATH + "userMessage.mustache");
		mailStr = StringEscapeUtils.unescapeHtml4(mailStr);
		log.info("ok, data has been output");
		
		Multipart mp = new MimeMultipart();
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(mailStr, "text/html; charset=utf-8");
		mp.addBodyPart(htmlPart);

		return mp;
	}
}
