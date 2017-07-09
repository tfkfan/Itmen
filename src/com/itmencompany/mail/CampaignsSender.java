package com.itmencompany.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.itmencompany.common.UserInfo;
import com.itmencompany.datastore.dao.CampaignDao;
import com.itmencompany.datastore.dao.UserOrderDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.datastore.entities.Campaign;
import com.itmencompany.datastore.entities.UserOrder;

public class CampaignsSender extends EmailSender {
	private ServletContext context;
	private static final String theme = "ITMEN | Order";
	private static final String htmlBody = "<div><h2 style='text-align:center'>Уважаемый/ая, @campaign@. Пользователь сервиса @service@ отправил вам заявку.</h2>"
			+ "<hr>@photos@@length@@material@@parlor@@wishes@@height@@addWishes@</div>";

	private static final String photosHTML = "<p><h4>Фотографии/эскизы</h4></p>";
	private static final String lengthHTML = "<p><h4>Длина гарнитуры</h4></p>";
	private static final String materialHTML = "<p><h4>Материал фасадов</h4></p>";
	private static final String parlorHTML = "<p><h4>Необходим ли пристенок</h4></p>";
	private static final String wishesHTML = "<p><h4>Пожелания по фурнитуре</h4></p>";
	private static final String heightHTML = "<p><h4>Высота</h4></p>";
	private static final String addWishesHTML = "<p><h4>Дополнительные пожелания к изделию</h4></p>";
	
	public CampaignsSender(ServletContext context) {
		this.context = context;
	}

	public void sendMessageToCampaigns(AppUser appUser, UserInfo info) throws JSONException, MessagingException, IOException {
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
			sendHtmlMessage(campaign.getEmail(), theme, getOrderMultipart(order, campaign.getTitle(), "ITMEN"));
	}

	public List<String> getCampaignsEmailsList(List<Campaign> campaigns) {
		List<String> res = new ArrayList<String>();
		if (campaigns != null)
			for (Campaign campaign : campaigns)
				res.add(campaign.getEmail());
		return res;
	}

	public Multipart getOrderMultipart(UserOrder userOrder, String campaignName, String serviceName)
			throws MessagingException, IOException {
		UserInfo info = userOrder.getInfo();
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


		//Attachments
	    MimeBodyPart attachment = new MimeBodyPart();
	    InputStream attachmentDataStream = new ByteArrayInputStream(getRewritedXlsBytes(userOrder.getId()));
	    attachment.setFileName("message.xls");
	    attachment.setContent(attachmentDataStream, "application/vnd.ms-excel");
	    mp.addBodyPart(attachment);
	    
	    MimeBodyPart attachment2 = new MimeBodyPart();
	    InputStream attachmentDataStream2 = new ByteArrayInputStream(getTxtBytes());
	    attachment2.setFileName("readme.txt");
	    attachment2.setContent(attachmentDataStream2, "text/plain");
	    mp.addBodyPart(attachment2);
	    
		return mp;
	}
	
	public byte[] getRewritedXlsBytes(Long orderId) throws IOException{
		InputStream xlsStream = context.getResourceAsStream("/WEB-INF/resources/Message.xls");
		HSSFWorkbook wb = new HSSFWorkbook(xlsStream);
		Sheet sheet = wb.getSheetAt(0);
		Row row = (Row) sheet.getRow(8);
		Cell cell = row.getCell(1);
		cell.setCellValue(orderId.toString());
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		wb.write(bos);
		wb.close();
		bos.close();
		return bos.toByteArray();
	}
	
	public byte[] getTxtBytes() throws IOException{
		InputStream inStream = context.getResourceAsStream("/WEB-INF/resources/readme.txt");
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = inStream.read(data, 0, data.length)) != -1)
		  buffer.write(data, 0, nRead);

		buffer.flush();
		
		return data;
	}
}
