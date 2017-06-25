package com.itmencompany.mail;

import java.util.List;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.datastore.entities.IncomingInfo;

public class UserSender extends EmailSender{
	private static final String htmlBody = "<div><h2>Уважаемый/ая, @username@. Вам пришел ответ от компании @campaign@.</h2>"
			+ "<hr>@photos@@title@@description@@length@@height@@material@@date@@cost@@campaignTitle@@addInfo@</div>";
	
	private static final String photosHTML = "<p><h4>Фотографии/эскизы</h4></p>";
	private static final String titleHTML = "<p><h4>Наименование изделия</h4></p>";
	private static final String descriptionHTML = "<p><h4>Описание изделия</h4></p>";
	private static final String dateHTML = "<p><h4>Срок Изготовления</h4></p>";
	private static final String costHTML = "<p><h4>Цена</h4></p>";
	private static final String campaignTitleHTML = "<p><h4>Название компании</h4></p>";
	private static final String lengthHTML = "<p><h4>Длина гарнитуры</h4></p>";
	private static final String materialHTML = "<p><h4>Материал фасадов</h4></p>";
	private static final String heightHTML = "<p><h4>Высота</h4></p>";
	private static final String addInfoHTML = "<p><h4>Дополнительная информация</h4></p>";
	
	private static final String theme = "ITMEN | No Reply";
	
	public UserSender(){
		
	}
	
	public void sendCampaignsAnswerNotification(AppUser appUser, IncomingInfo info) throws MessagingException{
		sendHtmlMessage(appUser.getEmail(), theme, getInfoMultipart(info, appUser.getUserName(), "ITMEN"));
	}
	
	public Multipart getInfoMultipart(IncomingInfo info, String userName, String serviceName)
			throws MessagingException {
		String body = new String(htmlBody);

		body = body.replace("@username@", userName);
		body = body.replace("@service@", serviceName);

		List<String> photos = info.getImages();
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
		if (info.getTitle() != null)
			buf = titleHTML + info.getTitle();
		body = body.replace("@title@", buf);
		
		buf = "";
		if (info.getDescription() != null)
			buf = descriptionHTML + info.getDescription();
		body = body.replace("@description@", buf);
		
		buf = "";
		if (info.getLength() != null)
			buf = lengthHTML + info.getLength();
		body = body.replace("@length@", buf);
		
		buf = "";
		if (info.getHeight() != null)
			buf = heightHTML + info.getHeight();
		body = body.replace("@height@", buf);

		buf = "";
		if (info.getMaterial() != null)
			buf = materialHTML + info.getMaterial();
		body = body.replace("@material@", buf);

		buf = "";
		if (info.getDate() != null)
			buf = dateHTML + info.getDate();
		body = body.replace("@date@", buf);

		buf = "";
		if (info.getCost() != null)
			buf = costHTML + info.getCost();
		body = body.replace("@cost@", buf);

		buf = "";
		if (info.getCompanyTitle() != null)
			buf = campaignTitleHTML + info.getCompanyTitle();
		body = body.replace("@campaignTitle@", buf);

		buf = "";
		if (info.getAddInfo() != null)
			buf = addInfoHTML + info.getAddInfo();
		body = body.replace("@addInfo@", buf);
		
		log.info("ok, data has been output");
		
		Multipart mp = new MimeMultipart();
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(body, "text/html; charset=utf-8");
		mp.addBodyPart(htmlPart);

		return mp;
	}
	
}
