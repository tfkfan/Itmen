package com.itmencompany.datastore.entities;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.annotation.Entity;
import com.itmencompany.datastore.entities.interfaces.JSONObj;
import com.googlecode.objectify.annotation.Index;

@Entity
public class AppUser extends DatabaseObject implements JSONObj {

	private static final long serialVersionUID = 1L;

	@Index
	protected Boolean isAdmin;

	@Index
	protected String email;
	@Index
	protected String userName;
	@Index
	protected String phone;

	@Index
	protected String password;

	@Index
	protected Boolean isVerified;
	@Index
	protected String vUniqueKey;

	public AppUser() {

	}

	public AppUser(String email, String userName, String phone) {
		setIsAdmin(false);
		setIsVerified(false);
		setEmail(email);
		setUserName(userName);
		setPhone(phone);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getvUniqueKey() {
		return vUniqueKey;
	}

	public void setvUniqueKey(String vUniqueKey) {
		this.vUniqueKey = vUniqueKey;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toJSON() throws JSONException {

		JSONObject resObj = new JSONObject();

		if (userName != null)
			resObj.put("username", userName);
		if (email != null)
			resObj.put("email", email);
		if (isAdmin != null)
			resObj.put("isAdmin", isAdmin);

		if (phone != null)
			resObj.put("phone", phone);

		return resObj.toString();
	}

}
