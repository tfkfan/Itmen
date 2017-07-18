package com.itmencompany.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.mortbay.log.Log;
import com.itmencompany.Exceptions.InvalidPrivateInfoException;
import com.itmencompany.Exceptions.UserAlreadyExistsException;
import com.itmencompany.common.RandomString;
import com.itmencompany.common.UserInfo;
import com.itmencompany.datastore.dao.AppUserDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.mail.EmailSender;

public class AppUserHelper {
	final static Logger log = Logger.getLogger(AppUserHelper.class.getName());
	
	public final static String USER_NAME_PARAM = "user_name";
	public final static String USER_PASSWORD_PARAM = "user_password";
	public final static String USER_EMAIL_PARAM = "user_email";
	public final static String USER_PHONE_PARAM = "user_phone";

	public final static String USER_SESSION_KEY = "appUser";

	public static AppUser getUserFromRequest(HttpServletRequest req)
			throws IOException {
		try {
			HttpSession session = req.getSession();
			AppUser appUser = (AppUser) session.getAttribute(USER_SESSION_KEY);
			if (appUser == null) {
				// redirect to authorize
				String user_email = req.getParameter(USER_EMAIL_PARAM);
				String user_password = req.getParameter(USER_PASSWORD_PARAM);

				if (user_email == null || user_password == null) {
					return null;
				}

				appUser = getUserFromDB(user_email, user_password);

				if (appUser == null) {
					log.info("User is not found in db");
				} else {
					if(user_password != null){
						if(!user_password.equals(appUser.getPassword()))
								return null;
					}
					log.info("You have been logged in");
					updateUserSession(req, appUser);
				}
			}
			else
				log.info("You have been logged in");
			return appUser;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String editPrivateUserInfo(HttpServletRequest request, AppUser appUser) throws Exception {
		if (appUser == null || request == null)
			throw new NullPointerException("App user is null or request is forbidden");
		String property = request.getParameter("property");
		String value = request.getParameter("value");
		AppUserDao dao = new AppUserDao(AppUser.class);
		String res = value;
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
			case "user_notifications":
				Boolean isEnabled = !appUser.getIsNtfsEnabled();
				log.info(isEnabled.toString());
				res = isEnabled.toString();
				appUser.setIsNtfsEnabled(isEnabled);
				break;
			default:
				log.info("Uknown user's property");
				break;
		}
		dao.save(appUser);
		AppUserHelper.updateUserSession(request, appUser);
		return res;
	}
	
	public static UserInfo getUserInfo(HttpServletRequest request){
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
	
	public static AppUser getUserFromDB(String user_email, String user_password) {
		try {
			AppUserDao dao = new AppUserDao(AppUser.class);
			AppUser appUser = user_password == null ? dao.getByEmail(user_email) : dao.getByEmailAndPassword(user_email, user_password);
			if (appUser != null) {
				String userEmail = appUser.getEmail();

				Log.info("User: " + userEmail + " is using services");
				return appUser;
			} else {
				Log.info("There's no such user in db.");
				return null;
			}

		} catch (Exception e) {
			Log.info("Singed in error:" + e.toString());
			Log.info(Arrays.toString(e.getStackTrace()));
			e.printStackTrace();
		}

		return null;
	}

	public static void updateUserSession(HttpServletRequest req, AppUser appUser) {
		HttpSession session = req.getSession();
		session.setAttribute(USER_SESSION_KEY, appUser);
	}
	
	public static void removeUserFromSession(HttpServletRequest req){
		HttpSession session = req.getSession();
		session.removeAttribute(USER_SESSION_KEY);
	}
	
	public static String createRandomPassword(){
		return  (new RandomString(8)).nextString();
	}

	public static AppUser createNewUser(HttpServletRequest req) throws UserAlreadyExistsException, InvalidPrivateInfoException {
		String user_name = req.getParameter(USER_NAME_PARAM);
		String user_email = req.getParameter(USER_EMAIL_PARAM);
		String user_phone = req.getParameter(USER_PHONE_PARAM);
		
		if(user_name == null || user_email == null || user_phone == null
				|| user_name.isEmpty() || user_email.isEmpty() || user_phone.isEmpty())
			throw new InvalidPrivateInfoException("Вы ввели некорректные поля пользователя");
		
		String password = createRandomPassword();
		AppUserDao dao = new AppUserDao(AppUser.class);
		AppUser appUser = dao.getByEmail(user_email);
		
		if(appUser != null)
			throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
		
		appUser = new AppUser(user_email, user_name, user_phone);
		if(user_email.equals("sylar131@live.ru"))
			appUser.setIsAdmin(true);
		appUser.setPassword(password);
		dao.save(appUser);
	
		sendPasswordWithEmail(user_email, password);
		updateUserSession(req, appUser);
		
		return appUser;
	}
	
	public static String getUserEmailFromRequest(HttpServletRequest request){
		return request.getParameter(USER_EMAIL_PARAM);
	}
	
	public static void createNewUserPassword(String user_email) throws InvalidPrivateInfoException{
		AppUser appUser = getUserFromDB(user_email, null);
		if (appUser != null){
			String newPassword = AppUserHelper.createRandomPassword();
			
			AppUserDao dao = new AppUserDao(AppUser.class);
			appUser.setPassword(newPassword);
			dao.save(appUser);
			
			AppUserHelper.sendPasswordWithEmail(appUser.getEmail(), newPassword);
		}
		else	
			throw new InvalidPrivateInfoException("Такой пользователь не существует");
	
	}
	
	protected static void sendPasswordWithEmail(String user_email, String password){
		EmailSender sender = new EmailSender();
		sender.sendTextMessage(user_email, "ITMENCOM Пароль для доступа к сервису", "Ваш пароль для входа: " + password);
	}
}
