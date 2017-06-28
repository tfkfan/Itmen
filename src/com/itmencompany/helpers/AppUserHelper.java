package com.itmencompany.helpers;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.mortbay.log.Log;
import com.itmencompany.Exceptions.InvalidPrivateInfoException;
import com.itmencompany.Exceptions.UserAlreadyExistsException;
import com.itmencompany.common.RandomString;
import com.itmencompany.datastore.dao.AppUserDao;
import com.itmencompany.datastore.entities.AppUser;
import com.itmencompany.mail.EmailSender;

public class AppUserHelper {
	protected static String USER_NAME_PARAM = "user_name";
	protected static String USER_PASSWORD_PARAM = "user_password";
	protected static String USER_EMAIL_PARAM = "user_email";
	protected static String USER_PHONE_PARAM = "user_phone";

	protected static String USER_SESSION_KEY = "loggedInUser";

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
					Log.info("User is not found in db");
				} else {
					if(user_password != null){
						if(!user_password.equals(appUser.getPassword()))
								return null;
					}
					Log.info("You have been logged in");
					updateUserSession(req, appUser);
				}
			}
			else
				Log.info("You have been logged in");
			return appUser;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
