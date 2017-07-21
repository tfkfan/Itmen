package com.itmencompany.mvc.services;

import java.util.List;

import com.itmencompany.mvc.datastore.entities.AppUser;

public interface UserService {
	public void save(AppUser user);
	public void save(Long id, String username, String phone, String email, Boolean isAdmin);
	public void delete(AppUser user);
	public void delete(Long id);
	public AppUser get(Long id);
	public List<AppUser> getAll();
}
