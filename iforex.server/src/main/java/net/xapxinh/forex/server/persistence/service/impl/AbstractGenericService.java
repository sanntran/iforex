package net.xapxinh.forex.server.persistence.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.persistence.dao.IGenericDao;
import net.xapxinh.forex.server.persistence.service.IGenericService;

@Transactional
public abstract class AbstractGenericService<T extends Serializable> implements IGenericService<T> {

	@Override
	public T save(T entity) {
		return getDao().save(entity);
	}
	
	@Override
	public T insert(final T entity) {
		return getDao().insert(entity);
	}
	@Override
	public T update(final T entity) {
		return getDao().update(entity);
	}
	@Override
	public void delete(final T entity) {
		getDao().delete(entity);
	}
	@Override
	public T findById(final long id) {
		return getDao().findById(id);
	}
	@Override
	public void deleteById(final long entityId) {
		getDao().deleteById(entityId);
	}
	@Override
	public List<T> loadAll() {
		return getDao().loadAll();
	}
	protected abstract IGenericDao<T> getDao();
}
