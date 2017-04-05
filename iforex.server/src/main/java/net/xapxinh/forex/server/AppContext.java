package net.xapxinh.forex.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppContext implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		context = ctx;
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}
}