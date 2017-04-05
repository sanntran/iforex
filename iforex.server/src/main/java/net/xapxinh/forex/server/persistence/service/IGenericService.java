package net.xapxinh.forex.server.persistence.service;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.persistence.IPersistence;

@Transactional
public interface IGenericService<T extends Serializable> extends IPersistence<T> {

}
