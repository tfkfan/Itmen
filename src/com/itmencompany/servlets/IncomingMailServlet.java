package com.itmencompany.servlets;

import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itmencompany.mvc.services.MailHandlerService;
import com.itmencompany.mvc.services.impl.MailHandlerServiceImpl;

@WebServlet("/_ah/mail/*")
public class IncomingMailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(IncomingMailServlet.class.getName());

	public IncomingMailServlet() {
		super();
	}

	private MailHandlerService mailHandlerService = new MailHandlerServiceImpl();
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		mailHandlerService.handleMail(req, this.getServletContext());
	}
}
