package com.itmencompany.mvc.controllers;

import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.itmencompany.datastore.dao.AppUserDao;
import com.itmencompany.datastore.dao.IncomingInfoDao;
import com.itmencompany.datastore.dao.UserOrderDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.datastore.entities.IncomingInfo;
import com.itmencompany.datastore.entities.UserOrder;
import com.itmencompany.mvc.model.ResultMessage;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	final static Logger log = Logger.getLogger(AdminController.class.getName());

	@RequestMapping(method = RequestMethod.GET)
	public String showAdmin() {
		return "admin";
	}

	@RequestMapping(value = "/get_order", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public UserOrder getOrder(@RequestParam Long order_id) {
		UserOrderDao dao = new UserOrderDao(UserOrder.class);
		if (order_id != null)
			return dao.get(order_id);
		return null;
	}

	@RequestMapping(value = "/delete_order", method = RequestMethod.POST)
	public void deleteOrder(@RequestParam Long order_id) {
		UserOrderDao dao = new UserOrderDao(UserOrder.class);
		if (order_id != null)
			dao.delete(order_id);
	}

	@RequestMapping(value = "/get_user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public AppUser getUser(@RequestParam Long user_id) {
		AppUserDao dao = new AppUserDao(AppUser.class);
		if (user_id != null)
			return dao.get(user_id);
		return null;
	}

	@RequestMapping(value = "/edit_user", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage editUser(@RequestParam Long user_id, @RequestParam(required = false) String username,
			@RequestParam(required = false) String phone, @RequestParam(required = false) String email,
			@RequestParam(required = false) Boolean isAdmin) {
		ResultMessage res = new ResultMessage();

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
		}
		return res;
	}

	@RequestMapping(value = "/delete_user", method = RequestMethod.POST)
	public void deleteUser(@RequestParam Long user_id) {
		AppUserDao dao = new AppUserDao(AppUser.class);
		if (user_id != null) {
			AppUser user = dao.get(user_id);
			dao.delete(user);
		}
	}

	@RequestMapping(value = "/delete_answer", method = RequestMethod.POST)
	public ResultMessage deleteAnswer(@RequestParam Long answer_id) {
		ResultMessage res = new ResultMessage();
		IncomingInfoDao dao = new IncomingInfoDao();
		try {
			if (answer_id != null) {
				IncomingInfo info = dao.get(answer_id);
				dao.delete(info);
				res.setMessage("ok");
			}
		} catch (Exception e) {
			res.setMessage("Some error occured during deleting answer");
			res.setError(true);
		}
		return res;
	}
}