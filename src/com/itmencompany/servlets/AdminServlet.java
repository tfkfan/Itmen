package com.itmencompany.servlets;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.itmencompany.common.AppUserHelper;
import com.itmencompany.datastore.entities.AppUser;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    final static Logger log = Logger.getLogger(AdminServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	public AdminServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AppUser appUser = AppUserHelper.getUserFromRequest(request);
		if(appUser == null || !appUser.getIsAdmin()){
			response.sendRedirect("/login");
			return;
		}
		request.getRequestDispatcher("/admin.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		JSONObject obj = new JSONObject();
		try {
			try {
				AppUser appUser = AppUserHelper.getUserFromRequest(request);
			
	
				String mode = request.getParameter("mode");
			} catch (Exception e) {
				obj.put("error", "true");
				obj.put("message", e.getMessage());
				log.info(e.getMessage());
				e.printStackTrace();
			}
		} catch (JSONException e1) {
			
			e1.printStackTrace();
		}
		response.getWriter().println(obj.toString());
		response.getWriter().close();
	}
}
