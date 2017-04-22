package net.xapxinh.forex.server.strategy;

import net.xapxinh.forex.server.entity.Candle;
import net.xapxinh.forex.server.entity.Decision;
import net.xapxinh.forex.server.entity.Tick;

public interface IStrategy {
	
	Decision getDecision(Candle candle, Tick tick);
}
