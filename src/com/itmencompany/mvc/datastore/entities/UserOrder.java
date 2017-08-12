package com.itmencompany.mvc.datastore.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.itmencompany.common.UserInfo;
import com.itmencompany.datastore.entities.interfaces.JSONObj;

@Entity
public class UserOrder extends DatabaseObject implements JSONObj {
	private static final long serialVersionUID = 1L;
	private UserInfo info;

	@Index
	private Long date;
	@Index
	private Long userId;

	@Index
	private List<String> campaignsEmails;

	public UserOrder() {

	}

	public UserOrder(UserInfo info, Long date, Long userId, List<String> campaignEmails) {
		setInfo(info);
		setDate(date);
		setUserId(userId);
		setCampaignsEmails(campaignEmails);
	}

	public UserInfo getInfo() {
		return info;
	}

	public void setInfo(UserInfo info) {
		this.info = info;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<String> getCampaignsEmails() {
		return campaignsEmails;
	}

	public void setCampaignsEmails(List<String> campaignsEmails) {
		this.campaignsEmails = campaignsEmails;
	}

	public String toJSON() throws JSONException {
		JSONObject res = new JSONObject();
		if (date != null) {
			String strDate = "";
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
			strDate = df.format(new Date(date));
			res.put("date", strDate);
		}
		if (userId != null) 
			res.put("userId", userId);
		if (campaignsEmails != null && !campaignsEmails.isEmpty()) {
			JSONArray arr = new JSONArray(campaignsEmails);
			res.put("campaigns", arr);
		}
		if(info != null)
			res.put("userInfo", info.toJSON());
		if(getId() != null)
			res.put("Id", getId());
		return res.toString();
	}
}
