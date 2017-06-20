package com.itmencompany.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.itmencompany.common.UserInfo;
import com.itmencompany.datastore.dao.CampaignDao;
import com.itmencompany.datastore.dao.UserOrderDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.datastore.entities.Campaign;
import com.itmencompany.datastore.entities.UserOrder;

public class CampaignsSender extends EmailSender {
	private static final String readmeURL = "https://drive.google.com/open?id=0B9cddZ7KlyxbYm1NWWRqSGh5MFU";
	private static final String xlsURL = "https://drive.google.com/open?id=0B9cddZ7KlyxbNGlSWU1fMHU4Nmc";
	
	private static final String theme = "ITMEN | Заявка";
	private static final String htmlBody = "<div style='text-align:center;'><h2>Уважаемый/ая, @campaign@. Пользователь сервиса @service@ отправил вам заявку.</h2>"
			+ "<hr>@photos@@length@@material@@parlor@@wishes@@height@@addWishes@@download@</div>";

	private static final String photosHTML = "<p><h4>Фотографии/эскизы</h4></p>";
	private static final String lengthHTML = "<p><h4>Длина гарнитуры</h4></p>";
	private static final String materialHTML = "<p><h4>Материал фасадов</h4></p>";
	private static final String parlorHTML = "<p><h4>Необходим ли пристенок</h4></p>";
	private static final String wishesHTML = "<p><h4>Пожелания по фурнитуре</h4></p>";
	private static final String heightHTML = "<p><h4>Высота</h4></p>";
	private static final String addWishesHTML = "<p><h4>Дополнительные пожелания к изделию</h4></p>";
	private static final String downloadHTML = "<p><h4>Файлы для скачивания</h4></p><div><ul style='list-style-type: none;'>"
				+ "<li><a href='@link1@'>Пример файла для заполнения</a></li>"
				+ "<li><a href='@link2@'>Инструкция к заполнению</a></li></ul></div>";
	
	public CampaignsSender() {

	}

	public void sendMessageToCampaigns(AppUser appUser, UserInfo info) throws JSONException, MessagingException {
		Long userId = appUser.getId();

		CampaignDao campaignDao = new CampaignDao(Campaign.class);
		List<Campaign> campaigns = campaignDao.listAll();

		Long date = (new Date()).getTime();

		UserOrderDao userOrderDao = new UserOrderDao(UserOrder.class);
		UserOrder order = new UserOrder(info, date, userId, getCampaignsEmailsList(campaigns));
		userOrderDao.save(order);

		if (campaigns == null)
			return;
	
		for (Campaign campaign : campaigns)
			sendHtmlMessage(campaign.getEmail(), theme, getInfoMultipart(info, campaign.getTitle(), "ITMEN"));
	}

	public List<String> getCampaignsEmailsList(List<Campaign> campaigns) {
		List<String> res = new ArrayList<String>();
		if (campaigns != null)
			for (Campaign campaign : campaigns)
				res.add(campaign.getEmail());
		return res;
	}

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
		
		buf = new String(downloadHTML);
		buf.replace("@link1@", xlsURL);
		buf.replace("@link2@", readmeURL);
		body = body.replace("@download@", buf);
		
		log.info("ok, data has been output");
		
		Multipart mp = new MimeMultipart();
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(body, "text/html; charset=utf-8");
		mp.addBodyPart(htmlPart);

		return mp;
	}
}
