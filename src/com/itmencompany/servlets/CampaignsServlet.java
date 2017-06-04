package com.itmencompany.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.itmencompany.datastore.dao.CampaignDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.datastore.entities.Campaign;
import com.itmencompany.helpers.AppUserHelper;

@WebServlet(name = "CampaignsServlet", urlPatterns = { "/campaigns" })
public class CampaignsServlet extends HttpServlet {
	final static Logger log = Logger.getLogger(CampaignsServlet.class.getName());
	private static final long serialVersionUID = 1L;

	public static final String TO_DELETE_LABEL = "to_delete";
	public static final String TO_CHANGE_LABEL = "to_save";

	public CampaignsServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/campaigns.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AppUser appUser = AppUserHelper.getUserFromRequest(request);
		if (appUser == null) {
			response.sendRedirect("/login");
			return;
		}
		try {
			String mode = request.getParameter("mode");
			String title = request.getParameter("title");
			String email = request.getParameter("email");

			String strId = request.getParameter("id");
			Long id = strId != null && !strId.equals("") ? Long.parseLong(strId) : null;
			CampaignDao campaignDao = new CampaignDao(Campaign.class);
			if (mode != null) {
				if (mode.equals(TO_CHANGE_LABEL)) {
					Campaign campaign = null;
					if (id != null)
						campaign = campaignDao.get(id);
					if (campaign == null)
						campaign = new Campaign(title, email, appUser.getId().toString());
					else {
						campaign.setTitle(title);
						campaign.setEmail(email);
					}

					campaignDao.save(campaign);
					printCampaignJSON(campaign, response);
				} else if (mode.equals(TO_DELETE_LABEL)) {
					if (id != null) {
						Campaign campaign = campaignDao.get(id);
						if (campaign != null) {
							campaignDao.delete(campaign);
							printCampaignJSON(campaign, response);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printCampaignJSON(Campaign campaign, HttpServletResponse response) throws IOException, JSONException {
		if (campaign == null)
			return;
		String json = campaign.toJSON();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.print(json);
		pw.close();

	}
}
