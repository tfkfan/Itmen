package com.itmencompany.mvc.controllers;

import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.itmencompany.datastore.dao.AppUserDao;
import com.itmencompany.datastore.dao.UserOrderDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.datastore.entities.UserOrder;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	final static Logger log = Logger.getLogger(AdminController.class.getName());

	@RequestMapping(method = RequestMethod.GET)
	public String showAdmin() {
		return "admin";
	}

	@RequestMapping(value = "/get_order", method = RequestMethod.POST)
	@ResponseBody
	public String getOrder(@RequestParam Long order_id) {
		String res = "";
		try {
			UserOrderDao dao = new UserOrderDao(UserOrder.class);
			if (order_id != null) {
				UserOrder userOrder = dao.get(order_id);
				res = userOrder.toJSON();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}

	@RequestMapping(value = "/delete_order", method = RequestMethod.POST)
	@ResponseBody
	public String deleteOrder(@RequestParam Long order_id) {
		String res = "";
		try {
			UserOrderDao dao = new UserOrderDao(UserOrder.class);

			if (order_id != null) {
				UserOrder userOrder = dao.get(order_id);
				dao.delete(userOrder);
				res = userOrder.toJSON();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}

	@RequestMapping(value = "/get_user", method = RequestMethod.POST)
	@ResponseBody
	public String getUser(@RequestParam Long user_id) {
		String res = "";
		try {
			AppUserDao dao = new AppUserDao(AppUser.class);

			if (user_id != null) {
				AppUser user = dao.get(user_id);
				res = user.toJSON();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}

	@RequestMapping(value = "/edit_user", method = RequestMethod.POST)
	@ResponseBody
	public String editUser(@RequestParam Long user_id,
			@RequestParam(required = false) String username, @RequestParam(required = false) String phone,
			@RequestParam(required = false) String email, @RequestParam(required = false) Boolean isAdmin) {
		String res = "";
		try {
			AppUserDao dao = new AppUserDao(AppUser.class);

			if (user_id != null) {
				AppUser user = dao.get(user_id);

				if (phone != null && !username.equals(""))
					user.setUserName(username);
				if (phone != null && !phone.equals(""))
					user.setPhone(phone);
				if (email != null && !email.equals(""))
					user.setEmail(email);
				if (isAdmin != null && !isAdmin)
					user.setIsAdmin(isAdmin);
				user = dao.saveAndReturn(user);
				res = user.toJSON();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}

}