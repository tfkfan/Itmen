package com.itmencompany.mvc.controllers;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.itmencompany.mvc.datastore.dao.IncomingInfoDao;
import com.itmencompany.mvc.datastore.dao.UserOrderDao;
import com.itmencompany.mvc.datastore.entities.AppUser;
import com.itmencompany.mvc.datastore.entities.IncomingInfo;
import com.itmencompany.mvc.datastore.entities.UserOrder;
import com.itmencompany.mvc.model.ResultMessage;
import com.itmencompany.mvc.services.OrderService;
import com.itmencompany.mvc.services.UserService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	final static Logger log = Logger.getLogger(AdminController.class.getName());
	
	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;

	@RequestMapping(method = RequestMethod.GET)
	public String showAdmin() {
		return "admin";
	}

	@RequestMapping(value = "/get_order", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public UserOrder getOrder(@RequestParam Long order_id) {
		return orderService.get(order_id);
	}

	@RequestMapping(value = "/delete_order", method = RequestMethod.POST)
	public void deleteOrder(@RequestParam Long order_id) {
		orderService.delete(order_id);
	}

	@RequestMapping(value = "/get_user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public AppUser getUser(@RequestParam Long user_id) {
		return userService.get(user_id);
	}

	@RequestMapping(value = "/edit_user", method = RequestMethod.POST)
	public void editUser(@RequestParam Long user_id, @RequestParam(required = false) String username,
			@RequestParam(required = false) String phone, @RequestParam(required = false) String email,
			@RequestParam(required = false) Boolean isAdmin) {
		userService.save(user_id, username, phone, email, isAdmin);
	}

	@RequestMapping(value = "/delete_user", method = RequestMethod.POST)
	public void deleteUser(@RequestParam Long user_id) {
		userService.delete(user_id);
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