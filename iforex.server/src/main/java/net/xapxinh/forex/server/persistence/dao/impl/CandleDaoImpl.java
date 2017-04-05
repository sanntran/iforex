package net.xapxinh.forex.server.persistence.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.persistence.dao.ICandleDao;

@Transactional
public class CandleDaoImpl extends AbstractGenericDao<Candle> implements ICandleDao {

	public CandleDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
		setClass(Candle.class);
	}

}
