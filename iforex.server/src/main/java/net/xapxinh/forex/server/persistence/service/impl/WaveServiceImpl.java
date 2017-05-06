package net.xapxinh.forex.server.persistence.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.persistence.dao.IWaveDao;
import net.xapxinh.forex.server.persistence.service.IWaveService;

@Transactional
public class WaveServiceImpl extends AbstractGenericService<Wave> implements IWaveService {

	private IWaveDao waveDao;
	
	public WaveServiceImpl(IWaveDao waveDao) {
		this.waveDao = waveDao;
	}
	
	@Override
	public Wave save(Wave entity) {
		if (entity.isNew()) {
			getDao().insert(entity);
		}
		else {
			getDao().update(entity);
		}
		return entity;
	}

	@Override
	protected IWaveDao getDao() {
		return waveDao;
	}

	@Override
	public <T extends Wave> T findLast(Class<T> clazz) {
		return getDao().findLast(clazz);
	}
	
	@Override
	public <T extends Wave> T findByTime(Date date, Class<T> clazz) {
		return getDao().findByTime(date, clazz);
	}

	@Override
	public <T extends Wave> List<T> findInPeriod(Date fromDate, Date toDate, Class<T> clazz) {
		return getDao().findInPeriod(fromDate, toDate, clazz);
	}
}
