package com.itmencompany.mail;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import com.itmencompany.common.UserInfo;
import com.itmencompany.datastore.entities.AppUser;

public class UserSender extends EmailSender{
	private static final String htmlBody = "<div><h2>Уважаемый/ая, @campaign@. Пользователь сервиса @service@ отправил вам заявку.</h2>"
			+ "<hr>@photos@@length@@material@@parlor@@wishes@@height@@addWishes@</div>";
	
	public UserSender(){
		
	}
	
	public void sendCampaignsAnswerNotification(AppUser appUser){
		
	}
	/*
	public Multipart getInfoMultipart(UserInfo info, String campaignName, String serviceName)
			throws MessagingException {
		String body = new String(htmlBody);

		body = body.replace("@campaign@", campaignName);
		body = body.replace("@service@", serviceName);

		List<String> photos = info.getFiles();
		String buf = "";
		if (photos != null && !photos.isEmpty()) {
			String photosBufHtml = "<div>";
			for (String photo : photos)
				photosBufHtml += "<img src='" + photo + "' />";
			photosBufHtml += "</div>";
			buf = photosHTML + photosBufHtml;
		}
		body = body.replace("@photos@", buf);

		buf = "";
		if (info.getFasade_material() != null)
			buf = materialHTML + info.getFasade_material();
		body = body.replace("@material@", buf);

		buf = "";
		if (info.getLength() != null)
			buf = lengthHTML + info.getLength();
		body = body.replace("@length@", buf);

		buf = "";
		if (info.getIs_parlor() != null)
			buf = parlorHTML + (info.getIs_parlor() ? "Необходим" : "Необязателен");
		body = body.replace("@parlor@", buf);

		buf = "";
		if (info.getWishes() != null)
			buf = wishesHTML + info.getWishes();
		body = body.replace("@wishes@", buf);

		buf = "";
		if (info.getHeight() != null)
			buf = heightHTML + info.getHeight();
		body = body.replace("@height@", buf);

		buf = "";
		if (info.getAdditional_wishes() != null)
			buf = addWishesHTML + info.getAdditional_wishes();
		body = body.replace("@addWishes@", buf);
		
		
		log.info("ok, data has been output");
		
		Multipart mp = new MimeMultipart();
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(body, "text/html; charset=utf-8");
		mp.addBodyPart(htmlPart);

		return mp;
	}
	*/
}
