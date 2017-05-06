package net.xapxinh.forex.server.persistence.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.entity.Wave;
import net.xapxinh.forex.server.persistence.dao.IWaveDao;

@Transactional
public class WaveDaoImpl extends AbstractGenericDao<Wave> implements IWaveDao {

	public WaveDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
		setClass(Wave.class);
	}
	
	@Override
	public List<Wave> loadAll() {
		return Collections.emptyList();
	}

	@Override
	public <T extends Wave> T findLast(Class<T> clazz) {
		DetachedCriteria maxId = DetachedCriteria.forClass(clazz)
			    .setProjection(Projections.max("id") );
		final Criteria criteria = getCurrentSession().createCriteria(clazz)
				   .add(Property.forName("id").eq(maxId));
		@SuppressWarnings("unchecked")
		List<T> results = criteria.list();
		if (results == null || results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Wave> T findByTime(Date date, Class<T> clazz) {
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
	public <T extends Wave> List<T> findInPeriod(Date fromDate, Date toDate, Class<T> clazz) {
		final Criteria criteria = getCurrentSession().createCriteria(clazz)
				   .add(Restrictions.between("time", fromDate, toDate))
				   .addOrder(Order.asc("time"));

		return criteria.list();
	}

}
