package net.xapxinh.forex.server.persistence.service.impl;

import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.persistence.dao.ICandleDao;
import net.xapxinh.forex.server.persistence.service.ICandleService;

@Transactional
public class CandleServiceImpl extends AbstractGenericService<Candle> implements ICandleService {

	private ICandleDao candleDao;
	
	public CandleServiceImpl(ICandleDao candleDao) {
		this.candleDao = candleDao;
	}

	@Override
	protected ICandleDao getDao() {
		return candleDao;
	}
}
