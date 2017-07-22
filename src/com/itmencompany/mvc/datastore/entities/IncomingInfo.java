package com.itmencompany.mvc.datastore.entities;

import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.itmencompany.datastore.entities.interfaces.JSONObj;

@Entity
public class IncomingInfo extends DatabaseObject implements JSONObj{
	private static final long serialVersionUID = 1L;
	@Index
	private String title;
	
	@Index 
	private String description;
	
	@Index
	private String length;
	
	@Index
	private String height;
	
	@Index
	private String material;
	
	@Index
	private String productionTime;

	@Index
	private String cost;
	
	@Index
	private String addInfo;
	
	@Index
	private String campaign_phone;

	@Index
	private String campaign_title;

	@Index
	private String campaign_email;
	
	@Index
	private List<String> images;
	
	@Index
	private Long order_id;
	
	@Index
	private Long userId;

	@Index
	private Boolean isFavorite;


	@Index
	private String release_date;
	
	public IncomingInfo() {
		isFavorite = false;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Boolean getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getProductionTime() {
		return productionTime;
	}

	public void setProductionTime(String productionTime) {
		this.productionTime = productionTime;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getAddInfo() {
		return addInfo;
	}

	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}

	public String getCampaign_phone() {
		return campaign_phone;
	}

	public void setCampaign_phone(String contactPhone) {
		this.campaign_phone = contactPhone;
	}

	public String getCampaign_title() {
		return campaign_title;
	}

	public void setCampaign_title(String companyTitle) {
		this.campaign_title = companyTitle;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Long getOrderID() {
		return order_id;
	}

	public void setOrderID(Long orderID) {
		this.order_id = orderID;
	}
	
	public String getRelease_date() {
		return release_date;
	}

	public void setRelease_date(String date) {
		this.release_date = date;
	}

	public String getCampaign_email() {
		return campaign_email;
	}

	public void setCampaign_email(String campaignEmail) {
		this.campaign_email = campaignEmail;
	}

	@Override
	public String toJSON() throws JSONException {
		JSONObject res = new JSONObject();
		if(images != null && !images.isEmpty())
			res.put("images", new JSONArray(images));
		if(length != null)
			res.put("length", length);
		if(material != null)
			res.put("material", material);
		if(title != null)
			res.put("title", title);
		if(description != null)
			res.put("description", description);
		if(height != null)
			res.put("height", height);
		if(release_date != null)
			res.put("release_date", release_date);
		if(cost != null)
			res.put("cost", cost);
		if(addInfo != null)
			res.put("add_info", addInfo);
		if(campaign_phone!= null)
			res.put("campaign_phone", campaign_phone);
		if(campaign_email != null)
			res.put("campaign_email", campaign_email);
		if(campaign_title != null)
			res.put("campaign_title", campaign_title);
		if(order_id != null)
			res.put("order_id", order_id);
		
		return res.toString();
	}

}
