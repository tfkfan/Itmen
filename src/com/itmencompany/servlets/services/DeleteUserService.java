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
import com.itmencompany.helpers.AppUserHelper;

@WebServlet("/delete_user")
public class DeleteUserService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USER_ID_KEY = "user_id";

	public DeleteUserService() {
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
		if (user_id != null && appUser.getIsAdmin()) {
			AppUser user = dao.get(Long.parseLong(user_id));
			dao.delete(user);
		}

		pw.println(res);
		pw.close();
	}
}
