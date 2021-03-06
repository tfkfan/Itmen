package com.itmencompany.mvc.controllers;

import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.itmencompany.common.AppUserHelper;
import com.itmencompany.mvc.datastore.dao.CampaignDao;
import com.itmencompany.mvc.datastore.entities.AppUser;
import com.itmencompany.mvc.datastore.entities.Campaign;

@Controller
@RequestMapping(value = "/campaigns")
public class CampaignsController {
	final static Logger log = Logger.getLogger(CampaignsController.class.getName());

	@RequestMapping(method = RequestMethod.GET)
	public String showCampaigns() {
		return "campaigns";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String editOrCreateCampaign(HttpServletRequest request, @RequestParam String title,
			@RequestParam String email, @RequestParam Long id) {
		String res = "";
		try {
			AppUser appUser = AppUserHelper.getUserFromRequest(request);

			CampaignDao campaignDao = new CampaignDao(Campaign.class);

			Campaign campaign = null;
			if (id != null)
				campaign = campaignDao.get(id);
			if (campaign == null)
				campaign = new Campaign(title, email, appUser.getId().toString());
			else {
				campaign.setTitle(title);
				campaign.setEmail(email);
			}

			campaign = campaignDao.saveAndReturn(campaign);

			res = campaign.toJSON();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteCampaign(@RequestParam Long id) {
		String res = "";
		try {

			CampaignDao campaignDao = new CampaignDao(Campaign.class);

			Campaign campaign = null;
			if (id != null) {
				campaign = campaignDao.get(id);
				if (campaign != null) {
					campaignDao.delete(campaign);
				}
			}

			res = campaign.toJSON();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}

}