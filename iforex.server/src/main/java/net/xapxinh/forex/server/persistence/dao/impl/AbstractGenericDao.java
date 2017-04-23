package net.xapxinh.forex.server.persistence.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import net.xapxinh.forex.server.entity.Pojo;
import net.xapxinh.forex.server.persistence.dao.IGenericDao;

@Transactional
@SuppressWarnings("unchecked")
public class AbstractGenericDao<T extends Pojo> implements IGenericDao<T> {

	protected static final String DATE_TIME = "dateTime";
	protected static final String MESSAGE = "message";
	private Class<T> clazz;
	

	private final SessionFactory sessionFactory;

	public AbstractGenericDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected final void setClass(final Class<T> clasz) {
		clazz = Preconditions.checkNotNull(clasz);
	}
	
	@Override
	public T save(T entity) {
		Preconditions.checkNotNull(entity);
		if (entity.isNew()) {
			return insert(entity);
		}
		else {
			return update(entity);
		}
	}
	
	@Override
	public T insert(T entity) {
		Preconditions.checkNotNull(entity);
		getCurrentSession().save(entity);
		return entity;
	}

	@Override

	public T update(T entity) {
		Preconditions.checkNotNull(entity);
		return (T) getCurrentSession().merge(entity);
	}

	@Override
	public void delete(T entity) {
		Preconditions.checkNotNull(entity);
		getCurrentSession().delete(entity);
	}

	@Override
	public void deleteById(long id) {
		final T entity = findById(id);
		Preconditions.checkState(entity != null);
		delete(entity);
	}

	@Override
	public List<T> loadAll() {
		return getCurrentSession().createQuery("from " + clazz.getName()).list();
	}

	@Override
	public T findById(long id) {
		return (T) getCurrentSession().get(clazz, id);
	}

	@Override
	public List<T> findMany(Query query) {
		return query.list();
	}

	@Override
	public List<T> findMany(Query query, Integer startIndex, Integer maxResult) {
		if (startIndex == null && maxResult != null) {
			return query.setFirstResult(0).setMaxResults(maxResult).list();
		}
		else if (startIndex != null && maxResult != null) {
			return query.setFirstResult(startIndex).setMaxResults(maxResult).list();
		}
		else if (startIndex != null && maxResult == null) {
			return query.setFirstResult(startIndex).list();
		}
		else { // startIndex == null && maxResult == null
			return query.list();
		}
	}

	protected T getFirst(List<T> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	protected long countObjects(Query query) {
		final Long result = Long.parseLong(query.list().get(0).toString());
		return result;
	}

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
}
