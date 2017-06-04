package com.itmencompany.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ServerUtils {
	private ServerUtils() {
	}

	public static String UPLOAD_PATH = "/upload";

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
