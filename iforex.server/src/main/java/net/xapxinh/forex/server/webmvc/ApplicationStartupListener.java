package net.xapxinh.forex.server.webmvc;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
	 
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		
	}
}