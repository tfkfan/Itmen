package com.itmencompany.servlets;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.itmencompany.mail.EmailSender;


@WebServlet("/test")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 final static Logger log = Logger.getLogger(TestServlet.class.getName());

    public TestServlet() {
        super();
       
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			InputStream xlsStream = this.getServletContext().getResourceAsStream("/WEB-INF/resources/Message.xls");
			HSSFWorkbook wb = new HSSFWorkbook(xlsStream);
			Sheet sheet = wb.getSheetAt(0);
			Row row = (Row) sheet.getRow(8);
			Cell cell = row.getCell(1);
			cell.setCellValue("57672810113269761111111");
			
			wb.close();
			
			EmailSender s = new EmailSender();
			
			Multipart mp = new MimeMultipart();
		

			//Attachments
		    MimeBodyPart attachment = new MimeBodyPart();
		    InputStream attachmentDataStream = new ByteArrayInputStream(wb.getBytes());
		    attachment.setFileName("message.xls");
		    attachment.setContent(attachmentDataStream, "application/vnd.ms-excel");
		    mp.addBodyPart(attachment);
			s.sendHtmlMessage("sylar131@live.ru", "s", mp);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

}
