package net.xapxinh.forex.server.persistence.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.persistence.IPersistence;

@Transactional
public interface IGenericDao<T extends Serializable> extends IPersistence<T> {	
	
	String NAME = "name";
	
	List<T> findMany(Query query);
	List<T> findMany(Query query, Integer fromIndex, Integer maxResult);
}
