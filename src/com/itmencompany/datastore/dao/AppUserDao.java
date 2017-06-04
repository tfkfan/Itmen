package com.itmencompany.datastore.dao;

import com.itmencompany.datastore.entities.AppUser;

public class AppUserDao extends BaseDao<AppUser> {

	public AppUserDao(Class<AppUser> clazz) {
		super(clazz);
	}

	public AppUser getByEmail(String email) {
		return this.getByProperty("email", email);
	}
}
