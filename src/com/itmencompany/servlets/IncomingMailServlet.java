package com.itmencompany.servlets;


import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
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

@WebServlet("/ah/mail/*")
public class IncomingMailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
			log.info("Count:" +  mp.getCount());
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
	
	

	public void read(InputStream in) {

		String result = "";
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<HSSFPictureData> lst = wb.getAllPictures();
		log.info("Кол-во изображений в сообщении:" + lst.size());
		for (Iterator<HSSFPictureData> it = lst.iterator(); it.hasNext();) {
			HSSFPictureData pict = it.next();
			String ext = pict.suggestFileExtension();
			//byte[] data = pict.getData();
			if (ext.equals("jpeg")) {
				log.info("Найдено изображение!!");
				
			}
		}

		Sheet sheet = wb.getSheetAt(0);
		Iterator<Row> it = sheet.iterator();
		while (it.hasNext()) {
			Row row = it.next();
			Iterator<Cell> cells = row.iterator();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				CellType type = cell.getCellTypeEnum();
				switch (type) {
				case STRING:
					result += cell.getStringCellValue();
					break;
				case NUMERIC:
					result += cell.getNumericCellValue();
					break;
				default:
					break;

				}
			}
			result += "\n";
		}

		log.info(result);
	}
	
}
