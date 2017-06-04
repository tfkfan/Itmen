package com.itmencompany.auth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.helpers.AppUserHelper;

@WebServlet("/login")
public class LogInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LogInServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AppUser appUser = AppUserHelper.getUserFromRequest(request);
		if (appUser != null)
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		else{
			request.setAttribute("message", "Неправильный логин или пароль");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}

}
