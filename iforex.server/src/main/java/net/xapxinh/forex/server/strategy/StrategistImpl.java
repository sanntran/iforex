package net.xapxinh.forex.server.strategy;

import org.springframework.beans.factory.annotation.Autowired;

import net.xapxinh.forex.server.entity.Decision;
import net.xapxinh.forex.server.persistence.service.ICandleService;

public class StrategistImpl implements IStrategist {

	@Autowired
	private ICandleService waveService;
	
	@Autowired
	private ICandleService candleService;
	
	@Autowired
	private SnipperStrategy snipperStrategy;
	
	@Override
	public Decision makeDecision() {
		Decision decision = snipperStrategy.makeDecision();
		return decision;
	}
	
}
