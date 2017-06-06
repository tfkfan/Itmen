package com.itmencompany.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
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
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.itmencompany.datastore.dao.IncomingInfoDao;
import com.itmencompany.datastore.dao.UserOrderDao;
import com.itmencompany.datastore.entities.IncomingInfo;
import com.itmencompany.datastore.entities.UserOrder;

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
					log.info("Some error occured during parsing incoming message");
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
		try{
			for (Iterator<HSSFPictureData> it = lst.iterator(); it.hasNext();) {
				HSSFPictureData pict = it.next();
				//String ext = pict.suggestFileExtension();
				byte[] data = pict.getData();
				images.add(sendToBlobStore(String.valueOf(random.nextLong()), "save", data));
			}
		}catch(Exception e){
			log.info("Error during images saving");
		}
		UserOrderDao orderDao = new UserOrderDao(UserOrder.class);

		//images
		iinfo.setImages(images);
		Sheet sheet = wb.getSheetAt(0);

		//Title
		CellReference cellReference = new CellReference(1, 2);
		Row row = sheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());

		String title = cell.getStringCellValue();
		iinfo.setTitle(title != null ? cell.getStringCellValue() : "");
		
		//Description
		cellReference = new CellReference(2, 2);
		row = sheet.getRow(cellReference.getRow());
		cell = row.getCell(cellReference.getCol());
		
		String description = cell.getStringCellValue();
		iinfo.setDescription(description);

		//Height
		cellReference = new CellReference(3, 2);
		row = sheet.getRow(cellReference.getRow());
		cell = row.getCell(cellReference.getCol());
		
		String height = cell.getStringCellValue();
		iinfo.setHeight(height);
		
		//Length
		cellReference = new CellReference(4, 2);
		row = sheet.getRow(cellReference.getRow());
		cell = row.getCell(cellReference.getCol());
		
		String length = cell.getStringCellValue();
		iinfo.setLength(length);
		
		//Material
		cellReference = new CellReference(5, 2);
		row = sheet.getRow(cellReference.getRow());
		cell = row.getCell(cellReference.getCol());
		
		String material = cell.getStringCellValue();
		iinfo.setMaterial(material);

		//Release date
		cellReference = new CellReference(6, 2);
		row = sheet.getRow(cellReference.getRow());
		cell = row.getCell(cellReference.getCol());
		
		String releaseDate = cell.getStringCellValue();
		iinfo.setDate(releaseDate);

		//Cost
		cellReference = new CellReference(7, 2);
		row = sheet.getRow(cellReference.getRow());
		cell = row.getCell(cellReference.getCol());
		
		String cost = cell.getStringCellValue();
		iinfo.setCost(cost);
		//Additional info
		
		cellReference = new CellReference(8, 2);
		row = sheet.getRow(cellReference.getRow());
		cell = row.getCell(cellReference.getCol());
		
		String addInfo = cell.getStringCellValue();
		iinfo.setAddInfo(addInfo);
		
		//Order ID
		cellReference = new CellReference(9, 2);
		row = sheet.getRow(cellReference.getRow());
		cell = row.getCell(cellReference.getCol());
		
		String orderIDStr = cell.getStringCellValue();
		Long orderID = Long.parseLong(orderIDStr);
		iinfo.setOrderID(orderID);

		//Phone
		cellReference = new CellReference(10, 2);
		row = sheet.getRow(cellReference.getRow());
		cell = row.getCell(cellReference.getCol());
		
		String phone = cell.getStringCellValue();
		iinfo.setContactPhone(phone);


		//Email
		cellReference = new CellReference(11, 2);
		row = sheet.getRow(cellReference.getRow());
		cell = row.getCell(cellReference.getCol());
		
		String email = cell.getStringCellValue();
		iinfo.setCampaignEmail(email);
		
		//company name
		cellReference = new CellReference(12, 2);
		row = sheet.getRow(cellReference.getRow());
		cell = row.getCell(cellReference.getCol());
		
		String companyTitle = cell.getStringCellValue();
		iinfo.setCompanyTitle(companyTitle);

		UserOrder order = orderDao.get(orderID);
		if(order!= null){
			iinfo.setUserId(order.getUserId());
		}else
			log.info("CANNOT FIND THE ORDER BY ORDER ID [" + orderID + "]");
		IncomingInfoDao dao = new IncomingInfoDao();
		dao.save(iinfo);
		log.info("INCOMING INFO HAS BEEN SAVED");
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
		String urlStr = URL_PREFIX + BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/blobimage");
		log.info(urlStr + " IMG UPLOADED");
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
			urlFetch.fetch(req);
			
			return urlStr;
		} catch (IOException e) {
			// Need a better way of handling Timeout exceptions here - 10 second
			// deadline
			log.info("Error: " + e.getMessage());
		}
		
		return null;
	}

}
