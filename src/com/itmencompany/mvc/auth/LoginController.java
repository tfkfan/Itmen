package com.itmencompany.mvc.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.helpers.AppUserHelper;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

	@RequestMapping(method = RequestMethod.GET)
	public String showLoginPage() {
		return "login";
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView handleUserLogin(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();

		try {
			AppUser appUser = AppUserHelper.getUserFromRequest(request);
			if (appUser != null){
				mv.setViewName("privateOffice");
			}
			else {
				mv.addObject("message", "Неправильный логин или пароль");
				mv.setViewName("login");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}

}