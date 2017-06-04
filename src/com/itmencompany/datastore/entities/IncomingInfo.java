package com.itmencompany.datastore.entities;

import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.itmencompany.common.UserInfo;

@Entity
public class IncomingInfo extends DatabaseObject {
	private static final long serialVersionUID = 1L;
	@Index
	private String title;
	
	@Index 
	private String description;
	
	@Index
	private String width;
	
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
	private String contactPhone;

	@Index
	private String companyTitle;

	@Index
	private String campaignEmail;
	
	@Index
	private List<String> images;
	
	@Index
	private Long orderID;

	@Index
	private Long date;
	
	public IncomingInfo() {

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

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
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

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getCompanyTitle() {
		return companyTitle;
	}

	public void setCompanyTitle(String companyTitle) {
		this.companyTitle = companyTitle;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Long getOrderID() {
		return orderID;
	}

	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}
	
	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getCampaignEmail() {
		return campaignEmail;
	}

	public void setCampaignEmail(String campaignEmail) {
		this.campaignEmail = campaignEmail;
	}

}
