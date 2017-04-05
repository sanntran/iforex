package net.xapxinh.forex.server.persistence.service;

import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.entity.Candle;

@Transactional
public interface ICandleService extends IGenericService<Candle> {		
	
}
