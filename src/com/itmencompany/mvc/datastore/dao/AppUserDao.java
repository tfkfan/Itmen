package com.itmencompany.mvc.datastore.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.itmencompany.mvc.datastore.entities.AppUser;

@Repository("userDao")
public class AppUserDao extends BaseDao<AppUser> {

	public AppUserDao(){
		super(AppUser.class);
	}
	
	public AppUserDao(Class<AppUser> clazz) {
		super(clazz);
	}

	public AppUser getByEmail(String email) {
		return this.getByProperty("email", email);
	}
	
	public AppUser getByEmailAndPassword(String email, String password) {
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("email", email);
		properties.put("password", password);
		
		return this.getByProperties(properties);
	}
}
