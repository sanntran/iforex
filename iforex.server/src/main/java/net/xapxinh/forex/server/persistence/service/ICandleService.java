package net.xapxinh.forex.server.persistence.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.entity.Candle;

@Transactional
public interface ICandleService extends IGenericService<Candle> {
	
	<T extends Candle> T findByTime(Date date, Class<T> clazz);
	<T extends Candle> List<T> findInPeriod(Date fromDate, Date toDate, Class<T> clazz);
}
