package com.itmencompany.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.itmencompany.common.ServerUtils;
import com.itmencompany.common.UserInfo;
import com.itmencompany.mvc.datastore.dao.CampaignDao;
import com.itmencompany.mvc.datastore.dao.UserOrderDao;
import com.itmencompany.mvc.datastore.entities.AppUser;
import com.itmencompany.mvc.datastore.entities.Campaign;
import com.itmencompany.mvc.datastore.entities.UserOrder;

public class CampaignsSender extends EmailSender {
	
	private ServletContext context;
	private static final String theme = "ITMEN | Order";
	
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
			sendHtmlMessage(campaign.getEmail(), theme, getOrderMultipart(order, campaign.getTitle()));
	}

	public List<String> getCampaignsEmailsList(List<Campaign> campaigns) {
		List<String> res = new ArrayList<String>();
		if (campaigns != null)
			for (Campaign campaign : campaigns)
				res.add(campaign.getEmail());
		return res;
	}

	public Multipart getOrderMultipart(UserOrder userOrder, String campaignName)
			throws MessagingException, IOException {
		UserInfo info = userOrder.getInfo();

		List<String> photos = info.getFiles();
		
		String photos_str = "";
		if (photos != null && !photos.isEmpty()) {
			String photosBufHtml = "<div>";
			for (String photo : photos)
				photosBufHtml += "<img src='" + photo + "' />";
			photosBufHtml += "</div><br/>";
			photos_str =  photosBufHtml;
		}else photos_str = " - ";
		
		photos_str = photos_str.replace("\"", "");
		
		String material = " - ";
		if (info.getFasade_material() != null)
			material = info.getFasade_material();

		String length = " - ";
		if (info.getLength() != null)
			length = info.getLength();
	
		String is_parlor = " - ";
		if (info.getIs_parlor() != null)
			is_parlor = (info.getIs_parlor() ? "Необходим" : "Необязателен");
		
		String wishes = " - ";
		if (info.getWishes() != null)
			 wishes = info.getWishes();
	
		String height = " - ";
		if (info.getHeight() != null)
			height = info.getHeight();
		
		String add_wishes = " - ";
		if (info.getAdditional_wishes() != null)
			add_wishes =  info.getAdditional_wishes();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy  HH:mm:ss ");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
		
		String date = dateFormat.format(userOrder.getDate());
		String answersUrl = ServerUtils.ANSWERS_URL;
		String serviceName = ServerUtils.SERVICE_NAME;
		String serviceDomain = ServerUtils.SERVICE_DOMAIN;
		String serviceUrl = ServerUtils.SERVICE_URL;
		
		wishes = ServerUtils.insertDiv(wishes);
		add_wishes = ServerUtils.insertDiv(add_wishes);
		material = ServerUtils.insertDiv(material);
		
		GenerateCampaignEmail gce = new GenerateCampaignEmail(photos_str, length, height, material, is_parlor,
				wishes, add_wishes, date, serviceName, serviceDomain, serviceUrl,
				campaignName, answersUrl);
		String mailStr = gce.generateEmailTemplate(context, ServerUtils.RESOURCES_PATH + "campaignMessage.mustache");
		
		//TODO find another no deprecated version
		mailStr = StringEscapeUtils.unescapeHtml4(mailStr);
		log.info("ok, data has been fetched");
		
		Multipart mp = new MimeMultipart();
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(mailStr, "text/html; charset=utf-8");
		mp.addBodyPart(htmlPart);

		//Attachments
	    MimeBodyPart attachment = new MimeBodyPart();
	    InputStream attachmentDataStream = new ByteArrayInputStream(getRewritedXlsBytes(userOrder.getId()));
	    attachment.setFileName("message.xls");
	    attachment.setContent(attachmentDataStream, "application/vnd.ms-excel");
	    mp.addBodyPart(attachment);
	    
		return mp;
	}
	
	public byte[] getRewritedXlsBytes(Long orderId) throws IOException{
		InputStream xlsStream = context.getResourceAsStream(ServerUtils.RESOURCES_PATH + "Message.xls");
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
}
