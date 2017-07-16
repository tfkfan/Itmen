package com.itmencompany.mvc.application;

import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.itmencompany.common.UserInfo;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.mail.CampaignsSender;
import com.itmencompany.mvc.model.ResultMessage;

@Controller
@RequestMapping(value = "/private")
public class PrivateOfficeController {
	final static Logger log = Logger.getLogger(PrivateOfficeController.class.getName());

	@Autowired
	ServletContext context;

	@RequestMapping(method = RequestMethod.GET)
	public String showPrivateOffice() {
		return "privateOffice";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage handleEdit(HttpServletRequest request) {
		ResultMessage obj = new ResultMessage();
		try {
			AppUser appUser = AppUserHelper.getUserFromRequest(request);
			if (appUser == null)
				appUser = AppUserHelper.createNewUser(request);

			obj.setValue(AppUserHelper.editPrivateUserInfo(request, appUser));
			obj.setMessage("Профиль обновлен");
		} catch (Exception e) {
			obj.setError(true);
			obj.setMessage(e.getMessage());
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage handleSend(HttpServletRequest request) {
		ResultMessage obj = new ResultMessage();
		try {
			AppUser appUser = AppUserHelper.getUserFromRequest(request);
			if (appUser == null)
				appUser = AppUserHelper.createNewUser(request);

			UserInfo info = AppUserHelper.getUserInfo(request);
			CampaignsSender sender = new CampaignsSender(context);
			sender.sendMessageToCampaigns(appUser, info);
			obj.setMessage("Сообщение отправлено компаниям");
		} catch (Exception e) {
			obj.setError(true);
			obj.setMessage(e.getMessage());
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}
}