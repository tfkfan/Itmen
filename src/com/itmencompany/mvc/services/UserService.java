package com.itmencompany.mvc.services;

import com.itmencompany.mvc.datastore.entities.AppUser;

public interface UserService extends Service<AppUser> {
	public void save(Long id, String username, String phone, String email, Boolean isAdmin);
}
