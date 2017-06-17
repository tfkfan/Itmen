package com.itmencompany.datastore.dao;

import java.util.HashMap;

import com.itmencompany.datastore.entities.AppUser;

public class AppUserDao extends BaseDao<AppUser> {

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
