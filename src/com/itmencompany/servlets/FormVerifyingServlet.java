package com.itmencompany.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.itmencompany.datastore.dao.AppUserDao;
import com.itmencompany.datastore.entities.AppUser;

@WebServlet("/verify")
public class FormVerifyingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FormVerifyingServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		PrintWriter pw = response.getWriter();

		String user_email = request.getParameter("user_email");
		AppUserDao dao = new AppUserDao(AppUser.class);
		AppUser appUser = dao.getByEmail(user_email);
		JSONObject res = new JSONObject();
		try {
			if (appUser != null) 
				res.put("message", "ѕользователь с таким email уже существует");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.println(res.toString());
		pw.close();
	}

}
