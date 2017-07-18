package com.itmencompany.servlets.services;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itmencompany.datastore.dao.AppUserDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.common.AppUserHelper;

@WebServlet("/save_user")
public class SaveUserService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USER_ID_KEY = "user_id";

	public SaveUserService() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AppUser appUser = AppUserHelper.getUserFromRequest(request);
		if(appUser == null){
			response.sendRedirect("/login");
			return;
		}
		String user_id = request.getParameter(USER_ID_KEY);

		response.setCharacterEncoding("utf-8");
		response.setContentType("text");
		PrintWriter pw = response.getWriter();
		AppUserDao dao = new AppUserDao(AppUser.class);
		String res = "ok";
		try {
			if (user_id != null && appUser.getIsAdmin()) {
				AppUser user = dao.get(Long.parseLong(user_id));
				String username = request.getParameter("username");
				String phone = request.getParameter("phone");
				String email = request.getParameter("email");
				String isAdmin = request.getParameter("isAdmin");

				if (phone != null && !username.equals(""))
					user.setUserName(username);
				if (phone != null && !phone.equals(""))
					user.setPhone(phone);
				if (email != null && !email.equals(""))
					user.setEmail(email);
				if (isAdmin != null && !isAdmin.equals(""))
					user.setIsAdmin(new Boolean(isAdmin));
				dao.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pw.println(res);
		pw.close();
	}
}
