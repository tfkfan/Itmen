package com.itmencompany.auth;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itmencompany.Exceptions.InvalidPrivateInfoException;
import com.itmencompany.Exceptions.UserAlreadyExistsException;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.helpers.AppUserHelper;

@WebServlet("/logup")
public class LogUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LogUpServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/logup.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AppUser appUser = null;
		try {
			appUser = AppUserHelper.createNewUser(request);
		} catch (UserAlreadyExistsException | InvalidPrivateInfoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter stream = response.getWriter();
		stream.println("Уважаемый(ая) пользователь, пароль для входа будет выслан по электронной почте " + appUser.getEmail());
		stream.println("<a href='/'>Вернуться на главную страницу</a>");
		stream.close();
	}
}
