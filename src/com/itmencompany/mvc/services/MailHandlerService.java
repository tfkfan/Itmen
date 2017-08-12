package com.itmencompany.mvc.services;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public interface MailHandlerService{
	void handleMail(HttpServletRequest request, ServletContext context);
	void read(InputStream in) throws IOException;
	String sendToBlobStore(String id, String cmd, byte[] imageBytes) throws IOException;
	
}
