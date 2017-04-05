package net.xapxinh.forex.server.persistence.dao;

import org.springframework.transaction.annotation.Transactional;

import net.xapxinh.forex.server.entity.Candle;

@Transactional
public interface ICandleDao extends IGenericDao<Candle> {

}
