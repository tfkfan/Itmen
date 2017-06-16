package com.itmencompany.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.log.Log;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.itmencompany.common.UserInfo;
import com.itmencompany.datastore.dao.AppUserDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.helpers.AppUserHelper;
import com.itmencompany.mail.CampaignsSender;

@WebServlet("/private")
public class PrivateOfficeServlet extends HttpServlet {
	final static Logger log = Logger.getLogger(PrivateOfficeServlet.class.getName());

	private static final long serialVersionUID = 1L;
	public static final String EDIT_PRIVATE_INFO = "edit_private_info";
	public static final String EDIT_USER_INFO = "edit_user_info";
	public static final String POST_USER_INFO = "post_user_info";

	public PrivateOfficeServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/privateOffice.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		JSONObject obj = new JSONObject();
		try {
			try {
				AppUser appUser = AppUserHelper.getUserFromRequest(request);
				if (appUser == null)
					appUser = AppUserHelper.createNewUser(request);

				String mode = request.getParameter("mode");

				switch (mode) {
				case EDIT_PRIVATE_INFO:
					editPrivateUserInfo(request, appUser);
					obj.put("message", "Профиль обновлен");
					break;
				case POST_USER_INFO:
					UserInfo info = getUserInfo(request);
					CampaignsSender sender = new CampaignsSender();
					sender.sendMessageToCampaigns(appUser, info);
					obj.put("message", "Сообщение отправлено компаниям");
					break;
				}
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

	public void editPrivateUserInfo(HttpServletRequest request, AppUser appUser) throws Exception {
		if (appUser == null || request == null)
			throw new NullPointerException("App user is null or request is forbidden");
		String property = request.getParameter("property");
		String value = request.getParameter("value");
		AppUserDao dao = new AppUserDao(AppUser.class);
		switch (property) {
		case "user_name":
			appUser.setUserName(value);
			break;
		case "user_phone":
			appUser.setPhone(value);
			break;
		case "user_email":
			appUser.setEmail(value);
			break;
		}
		dao.save(appUser);
	}

	public UserInfo getUserInfo(HttpServletRequest request){
		String[] files = request.getParameterValues("images[]");
		List<String> images = new ArrayList<String>();
	
		if(files != null)
		for(String img : files)
			images.add(img);

		String length = request.getParameter("length");
		String fasade_material = request.getParameter("fasade_material");
		Boolean is_parlor = Boolean.parseBoolean(request.getParameter("is_parlor"));
		String wishes = request.getParameter("wishes");
		String height = request.getParameter("height");
		String additional_wishes = request.getParameter("additional_wishes");

		UserInfo info = new UserInfo(images, length, fasade_material, is_parlor, wishes, height, additional_wishes);
		log.info(info.toString());
		return info;
	}
}
