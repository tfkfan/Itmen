package com.itmencompany.mail;

public class GenerateCampaignEmail extends CustomMustacheLoader {
	String photos;
	String length;
	String height;
	String material;
	String is_parlor;
	String wishes;
	String add_wishes;
	String date;
	String service;
	String service_domain;
	String service_url;
	String campaign;
	String answers_url;

	public GenerateCampaignEmail(String photos, String length, String height, String material, String is_parlor,
			String wishes, String add_wishes, String date, String service, String service_domain, String service_url,
			String campaign, String answers_url) {
		this.photos = photos;
		this.length = length;
		this.height = height;
		this.material = material;
		this.is_parlor = is_parlor;
		this.wishes = wishes;
		this.add_wishes = add_wishes;
		this.date = date;
		this.service = service;
		this.service_domain = service_domain;
		this.service_url = service_url;
		this.campaign = campaign;
		this.answers_url = answers_url;
	}


}
