package com.itmencompany.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.itmencompany.common.ServerUtils;
import com.itmencompany.datastore.dao.AppUserDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.datastore.dao.IncomingInfoDao;
import com.itmencompany.datastore.dao.UserOrderDao;
import com.itmencompany.datastore.entities.IncomingInfo;
import com.itmencompany.datastore.entities.UserOrder;
import com.itmencompany.mail.EmailSender;
import com.itmencompany.mail.UserSender;

@WebServlet("/ah/mail/*")
public class IncomingMailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String URL_PREFIX = "http://itmen-1261.appspot.com";

	private static final Logger log = Logger.getLogger(IncomingMailServlet.class.getName());

	public IncomingMailServlet() {
		super();
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {
			MimeMessage message = new MimeMessage(session, req.getInputStream());
			// debug option
			log.info("Received mail message. ");
			MimeMultipart mp = (MimeMultipart) message.getContent();
			log.info("Count:" + mp.getCount());
			for (int index = 0; index < mp.getCount(); index++) {
				try {
					MimeBodyPart attachment = (MimeBodyPart) mp.getBodyPart(index);
					log.info(attachment.getContentType());
					if (attachment.getContentType().contains("application/vnd.ms-excel")) {

						String filename = attachment.getFileName();
						log.info("Found an excel file!" + filename);
						InputStream is = attachment.getInputStream();
						read(is);
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Some error occured during parsing incoming message ");
					// do nothing and try again
				}
			}

			log.info("Message parsed");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private static Random random = new Random();

	public void read(InputStream in) throws IOException {
		IncomingInfo iinfo = new IncomingInfo();
		String result = "";
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<HSSFPictureData> lst = wb.getAllPictures();
		log.info("Кол-во изображений в сообщении:" + lst.size());
		List<String> images = new ArrayList<String>();
		try {
			for (Iterator<HSSFPictureData> it = lst.iterator(); it.hasNext();) {
				HSSFPictureData pict = it.next();
				// String ext = pict.suggestFileExtension();
				byte[] data = pict.getData();
				images.add(sendToBlobStore(String.valueOf(random.nextLong()), "save", data));
			}
		} catch (Exception e) {
			log.info("Error during images saving");
		}
		UserOrderDao orderDao = new UserOrderDao(UserOrder.class);

		// images
		iinfo.setImages(images);
		Sheet sheet = wb.getSheetAt(0);

		// Title
		String title = getCellValue(sheet, 0, 1);
		iinfo.setTitle(title);

		// Description
		String description = getCellValue(sheet, 1, 1);
		iinfo.setDescription(description);

		// Height
		String height = getCellValue(sheet, 2, 1);
		iinfo.setHeight(height);

		// Length
		String length = getCellValue(sheet, 3, 1);
		iinfo.setLength(length);

		// Material
		String material = getCellValue(sheet, 4, 1);
		iinfo.setMaterial(material);

		// Release date
		String releaseDate = getCellValue(sheet, 5, 1);
		iinfo.setDate(releaseDate);
		// Cost
		String cost = getCellValue(sheet, 6, 1);
		iinfo.setCost(cost);

		// Additional info
		String addInfo = getCellValue(sheet, 7, 1);
		iinfo.setAddInfo(addInfo);

		// Order ID
		String orderIDStr = getCellValue(sheet, 8, 1);
		Long orderID = Long.parseLong(orderIDStr);
		iinfo.setOrderID(orderID);

		// Phone
		String phone = getCellValue(sheet, 9, 1);
		iinfo.setContactPhone(phone);

		// Email
		String email = getCellValue(sheet, 10, 1);
		iinfo.setCampaignEmail(email);

		// company name
		String companyTitle = getCellValue(sheet, 11, 1);
		iinfo.setCompanyTitle(companyTitle);

		UserOrder order = orderDao.get(orderID);
		if (order != null) {
			iinfo.setUserId(order.getUserId());
		} else{
			log.info("CANNOT FIND THE ORDER BY ORDER ID [" + orderID + "]");
			return;
		}
		IncomingInfoDao dao = new IncomingInfoDao();
		dao.save(iinfo);
		log.info("INCOMING INFO HAS BEEN SAVED");
		
		AppUserDao userDao = new AppUserDao(AppUser.class);
		AppUser appUser = userDao.get(order.getUserId());
		if(appUser.getIsNtfsEnabled()){
			UserSender sender = new UserSender();
			try {
				sender.sendCampaignsAnswerNotification(appUser, iinfo);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	private static String getCellValue(Sheet sheet, int r, int c) {

		String result = "";
		try {
			CellReference cellReference = new CellReference(r, c);
			Row row = sheet.getRow(cellReference.getRow());
			Cell cell = row.getCell(cellReference.getCol());

			if (cell != null) {
				if (cell.getCellTypeEnum().equals(CellType.STRING))
					result = cell.getStringCellValue();
				else if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
					result = "" + Math.round(cell.getNumericCellValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String randomString() {
		return Long.toString(random.nextLong(), 36);
	}

	private String makeBoundary() {
		return "---------------------------" + randomString() + randomString() + randomString();
	}

	private void write(OutputStream os, String s) throws IOException {
		os.write(s.getBytes());
	}

	private void writeParameter(OutputStream os, String name, String value) throws IOException {
		write(os, "Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n" + value + "\r\n");
	}

	private void writeImage(OutputStream os, String name, byte[] bs) throws IOException {
		write(os, "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"image.jpg\"\r\n");
		write(os, "Content-Type: image/jpeg\r\n\r\n");
		os.write(bs);
		write(os, "\r\n");
	}

	private String sendToBlobStore(String id, String cmd, byte[] imageBytes) throws IOException {
		String urlStr = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl(ServerUtils.UPLOAD_PATH);
		URLFetchService urlFetch = URLFetchServiceFactory.getURLFetchService();
		HTTPRequest req = new HTTPRequest(new URL(urlStr), HTTPMethod.POST, FetchOptions.Builder.withDeadline(10.0));

		String boundary = makeBoundary();

		req.setHeader(new HTTPHeader("Content-Type", "multipart/form-data; boundary=" + boundary));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		write(baos, "--" + boundary + "\r\n");
		writeParameter(baos, "id", id);
		write(baos, "--" + boundary + "\r\n");
		writeImage(baos, cmd, imageBytes);
		write(baos, "--" + boundary + "--\r\n");

		req.setPayload(baos.toByteArray());
		try {
			HTTPResponse resp = urlFetch.fetch(req);
			if (resp.getResponseCode() == 200){
				   String result = new String(resp.getContent(), "UTF-8");
					log.info("!IMG UPLOADED: " + result);
					return result;
			}

			return "";
		} catch (IOException e) {
			// Need a better way of handling Timeout exceptions here - 10 second
			// deadline
			log.info("Error: " + e.getMessage());
		}

		return null;
	}

}
