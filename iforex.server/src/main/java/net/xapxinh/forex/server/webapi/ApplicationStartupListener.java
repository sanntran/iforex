package net.xapxinh.forex.server.webapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import net.xapxinh.forex.server.persistence.service.ICandleService;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
	 
	@Autowired
	private ICandleService candleService;
	
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		
	}
}