package net.xapxinh.forex.server.persistence.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.persistence.dao.ICandleDao;

@Transactional
public class CandleDaoImpl extends AbstractGenericDao<Candle> implements ICandleDao {

	public CandleDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
		setClass(Candle.class);
	}
	
	@Override
	public List<Candle> loadAll() {
		return Collections.emptyList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Candle> T findByTime(Date date, Class<T> clazz) {
		final Criteria criteria = getCurrentSession().createCriteria(clazz)
				   .add(Restrictions.eq("time", date));

		List<T> results = criteria.list();
		if (results == null || results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Candle> List<T> findInPeriod(Date fromDate, Date toDate, Class<T> clazz) {
		final Criteria criteria = getCurrentSession().createCriteria(clazz)
				   .add(Restrictions.between("time", fromDate, toDate))
				   .addOrder(Order.asc("time"));

		return criteria.list();
	}
}
