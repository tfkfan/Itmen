package com.itmencompany.mail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.itmencompany.common.ServerUtils;

public class GenerateCampaignEmail {
	final static Logger log = Logger.getLogger(GenerateCampaignEmail.class.getName());
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

	public String generateEmailTemplate(ServletContext context) {
		MustacheFactory mf = new DefaultMustacheFactory();
		String filename = ServerUtils.RESOURCES_PATH + "campaignMessage.mustache";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResourceAsStream(filename), "UTF-8"));
			Mustache mustache = mf.compile(reader, filename);
			StringWriter sw = new StringWriter();
			mustache.execute(sw, this).flush();
			String toReturn = sw.toString();
			sw.flush();
			sw.close();
			return toReturn;
		} catch (IOException e) {
			e.printStackTrace();
			log.info("ERROR creating email!");
		}
		return "";
	}
}
