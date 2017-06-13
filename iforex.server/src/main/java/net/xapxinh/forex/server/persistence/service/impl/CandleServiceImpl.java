package net.xapxinh.forex.server.persistence.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.event.CandleEventNotifier;
import net.xapxinh.forex.server.event.CandleInsertedEvent;
import net.xapxinh.forex.server.persistence.dao.ICandleDao;
import net.xapxinh.forex.server.persistence.service.ICandleService;

@Transactional
public class CandleServiceImpl extends AbstractGenericService<Candle> implements ICandleService {

	private ICandleDao candleDao;
	
	@Autowired
	private CandleEventNotifier candleEventNotifier;
	
	public CandleServiceImpl(ICandleDao candleDao) {
		this.candleDao = candleDao;
	}
	
	@Override
	public Candle save(Candle entity) {
		if (entity.isNew()) {
			getDao().insert(entity);
			candleEventNotifier.notifyListeners(new CandleInsertedEvent(entity));
		}
		else {
			getDao().update(entity);
		}
		return entity;
	}

	@Override
	protected ICandleDao getDao() {
		return candleDao;
	}
	
	@Override
	public <T extends Candle> List<T> loadAll(Class<T> clazz) {
		return getDao().loadAll(clazz);
	}

	@Override
	public <T extends Candle> T findByTime(Date date, Class<T> clazz) {
		return getDao().findByTime(date, clazz);
	}

	@Override
	public <T extends Candle> List<T> findInPeriod(Date fromDate, Date toDate, Class<T> clazz) {
		return getDao().findInPeriod(fromDate, toDate, clazz);
	}
}
