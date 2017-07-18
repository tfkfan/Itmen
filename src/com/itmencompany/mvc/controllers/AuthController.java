package com.itmencompany.mvc.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.itmencompany.Exceptions.InvalidPrivateInfoException;
import com.itmencompany.Exceptions.UserAlreadyExistsException;
import com.itmencompany.common.AppUserHelper;
import com.itmencompany.datastore.entities.AppUser;

@Controller
public class AuthController {
	final static Logger log = Logger.getLogger(AuthController.class.getName());

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginPage() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String handleUserLogin(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		try {
			AppUser appUser = AppUserHelper.getUserFromRequest(request);
			if (appUser == null) {
				model.addAttribute("message", "Неправильный логин или пароль");
				return "login";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "privateOffice";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String showSignUpPage() {
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void handleUserSignUp(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		AppUser appUser = null;
		try {
			appUser = AppUserHelper.createNewUser(request);

			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			PrintWriter stream = response.getWriter();
			stream.println("Уважаемый(ая) пользователь, пароль для входа будет выслан по электронной почте "
					+ appUser.getEmail());
			stream.println("<a href='/'>Вернуться на главную страницу</a>");
			stream.close();

		} catch (UserAlreadyExistsException | InvalidPrivateInfoException | IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String handleLogOut(HttpServletRequest request, HttpServletResponse response) {
		AppUserHelper.removeUserFromSession(request);
		return "login";
	}

	@RequestMapping(value = "/forgot_password", method = RequestMethod.GET)
	public String showForgotPasswordPage() {
		return "forgotPassword";
	}

	@RequestMapping(value = "/forgot_password", method = RequestMethod.POST)
	public void handleForgotPassword(HttpServletRequest request, HttpServletResponse response) {
		try {
			String user_email = AppUserHelper.getUserEmailFromRequest(request);
			if (user_email == null) {
				AppUser currentUser = AppUserHelper.getUserFromRequest(request);
				user_email = currentUser.getEmail();
			}
			
			AppUserHelper.createNewUserPassword(user_email);
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			PrintWriter stream = response.getWriter();
			stream.println(
					"Уважаемый(ая) пользователь, пароль для входа будет выслан по электронной почте, указанной при регистрации");
			stream.println("<a href='/'>Вернуться на главную страницу</a>");
			stream.close();
			log.info("Fine. Password has been sent");
		} catch (InvalidPrivateInfoException | IOException e) {
			log.info(e.getMessage());
			request.setAttribute("message", e.getMessage());
		}
	}
}