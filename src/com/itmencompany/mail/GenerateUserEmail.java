package com.itmencompany.mail;

public class GenerateUserEmail extends CustomMustacheLoader {
	String title;
	String photos;
	String length;
	String height;
	String material;
	String username;
	String add_info;
	String cost;
	String description;
	String date;
	String release_date;
	String service;
	String service_domain;
	String service_url;
	String campaign;
	String phone;
	String email;

	public GenerateUserEmail(String title, String photos, String length, String height, String material, String add_info,  String date, String release_date,
			String username, String campaign, String phone, String email, String cost, String description, String service, String service_domain, String service_url) {
		this.title = title;
		this.phone = phone;
		this.email = email;
		this.photos = photos;
		this.length = length;
		this.height = height;
		this.material = material;
		this.release_date = release_date;
		this.username = username;
		this.date = date;
		this.cost = cost;
		this.description = description;
		this.service = service;
		this.service_domain = service_domain;
		this.service_url = service_url;
		this.campaign = campaign;
	}
}
