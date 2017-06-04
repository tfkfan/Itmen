package com.itmencompany.datastore.entities;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.itmencompany.datastore.entities.interfaces.JSONObj;

@Entity
@Index
public class Campaign extends DatabaseObject implements JSONObj{
	private static final long serialVersionUID = 1L;
	@Index
	private String title;
	@Index
	private String email;
	@Index
	private String createdBy;

	public Campaign() {

	}

	public Campaign(String title, String email, String createdBy) {
		setTitle(title);
		setEmail(email);
		setCreatedBy(createdBy);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String toJSON() throws JSONException {
		JSONObject res = new JSONObject();
		res.put("title", title);
		res.put("email", email);
		res.put("id", getId());
		return res.toString();
	}
}
