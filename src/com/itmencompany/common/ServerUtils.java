package com.itmencompany.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ServerUtils {
	private ServerUtils() {
	}

	public static final String UPLOAD_PATH = "/upload";
	public static final String APP_NAME = "itmen-1261";
	public static final String RESOURCES_PATH = "/WEB-INF/resources/";
	public static final String SERVICE_NAME = "ITMEN Service";
	public static final String SERVICE_URL = "http://itmen-1261.appspot.com";
	public static final String SERVICE_DOMAIN = "itmen-1261.appspot.com";
	public static final String ANSWERS_URL = "answers@itmen-1261.appspotmail.com";
	public static String insertDiv(String val){
		return "<div>" + val + "</div>";
	}
	public static String readTextFile(String filepath) {
		String res = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filepath));
			String line;
			while ((line = br.readLine()) != null) 
				res += line;	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) 
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return res;
	}
}
