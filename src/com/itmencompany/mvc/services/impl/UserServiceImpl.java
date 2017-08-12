package com.itmencompany.mvc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itmencompany.mvc.datastore.dao.AppUserDao;
import com.itmencompany.mvc.datastore.entities.AppUser;
import com.itmencompany.mvc.services.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	AppUserDao userDao;

	public UserServiceImpl() {

	}

	@Override
	public void save(AppUser user) {
		if (user != null)
			userDao.save(user);
	}

	@Override
	public void delete(AppUser user) {
		if (user != null)
			userDao.delete(user);
	}

	@Override
	public AppUser get(Long id) {
		if (id != null)
			return userDao.get(id);
		return null;
	}

	@Override
	public List<AppUser> getAll() {
		return userDao.listAll();
	}

	@Override
	public void delete(Long id) {
		if (id != null)
			userDao.delete(id);
	}

	@Override
	public void save(Long id, String username, String phone, String email, Boolean isAdmin) {
		if(id == null)
			return;
		AppUser user = userDao.get(id);
		if (username != null && !username.equals(""))
			user.setUserName(username);
		if (phone != null && !phone.equals(""))
			user.setPhone(phone);
		if (email != null && !email.equals(""))
			user.setEmail(email);
		if (isAdmin != null && !isAdmin)
			user.setIsAdmin(isAdmin);
		userDao.save(user);
	}
}
