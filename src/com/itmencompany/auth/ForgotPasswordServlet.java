package com.itmencompany.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itmencompany.Exceptions.InvalidPrivateInfoException;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.helpers.AppUserHelper;

@WebServlet("/forgot_password")
public class ForgotPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Logger log = Logger.getLogger(ForgotPasswordServlet.class.getName());

	public ForgotPasswordServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/forgotPassword.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try{
			String user_email = AppUserHelper.getUserEmailFromRequest(request);
			if(user_email == null){
				AppUser currentUser = AppUserHelper.getUserFromRequest(request);
				user_email = currentUser.getEmail();
			}
			AppUserHelper.createNewUserPassword(user_email);
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			PrintWriter stream = response.getWriter();
			stream.println("Уважаемый(ая) пользователь, пароль для входа будет выслан по электронной почте, указанной при регистрации");
			stream.println("<a href='/'>Вернуться на главную страницу</a>");
			stream.close();
			log.info("Fine. Password has been sent");
		}catch(InvalidPrivateInfoException e){
			log.info(e.getMessage());
			request.setAttribute("message", e.getMessage());
			request.getRequestDispatcher("/forgotPassword.jsp").forward(request, response);
		}
	}

}
