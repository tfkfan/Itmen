package com.itmencompany.datastore.objectify;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.googlecode.objectify.ObjectifyService;
import com.itmencompany.mvc.datastore.entities.AppUser;
import com.itmencompany.mvc.datastore.entities.Campaign;
import com.itmencompany.mvc.datastore.entities.IncomingInfo;
import com.itmencompany.mvc.datastore.entities.UserOrder;

public class OfyHelper implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
		ObjectifyService.register(AppUser.class);
		ObjectifyService.register(Campaign.class);
		ObjectifyService.register(UserOrder.class);
		ObjectifyService.register(IncomingInfo.class);
	}

	public void contextDestroyed(ServletContextEvent event) {
		// App Engine does not currently invoke this method.
	}
}
