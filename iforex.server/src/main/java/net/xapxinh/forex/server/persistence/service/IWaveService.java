package net.xapxinh.forex.server.persistence.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.entity.Wave;

@Transactional
public interface IWaveService extends IGenericService<Wave> {
	
	<T extends Wave> T findByTime(Date date, Class<T> clazz);
	<T extends Wave> List<T> findInPeriod(Date fromDate, Date toDate, Class<T> clazz);
}
