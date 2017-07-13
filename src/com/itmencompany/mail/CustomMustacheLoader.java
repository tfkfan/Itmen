package com.itmencompany.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public abstract class CustomMustacheLoader {
	final static Logger log = Logger.getLogger(CustomMustacheLoader.class.getName());
	
	public CustomMustacheLoader(){
		
	}
	
	public String generateEmailTemplate(ServletContext context, String filename) throws IOException {
		MustacheFactory mf = new DefaultMustacheFactory();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResourceAsStream(filename), "utf-8"));
		Mustache mustache = mf.compile(reader, filename);
		StringWriter sw = new StringWriter();
		mustache.execute(sw, this).flush();
		String toReturn = sw.toString();
		sw.flush();
		sw.close();
		return toReturn;
	}
}
