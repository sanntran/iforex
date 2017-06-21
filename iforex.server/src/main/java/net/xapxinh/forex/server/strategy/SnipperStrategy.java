package net.xapxinh.forex.server.strategy;

import java.util.List;

import net.xapxinh.forex.server.entity.Decision;
import net.xapxinh.forex.server.entity.Order;
import net.xapxinh.forex.server.util.calendar.DateTimeUtil;

/* 
Wait for good signal 

Conditions
- Break previous wave
- Volume is very high
*/

public class SnipperStrategy {
	
	public Decision makeDecision() {
		
		List<Order> openingOrders = getOpeningOrder();
		if (openingOrders == null || openingOrders.isEmpty()) {
			// no order to close
			makeNewOrderIfPossible();
		}
		else {			
			closeOpeningOrderIfPossible(openingOrders);
		}
		
		
		return null;
		
		
	}
	
	/**
	 * Close orders before 23:59
	 */
	
	private void closeOpeningOrderIfPossible(List<Order> openingOrders) {
		
		
	}

	/** 
 	Do not open order if time is in Asian session
 	Do not open order if EU-US session is end in 1 hour
	 **/
	private Order makeNewOrderIfPossible() {
		if (DateTimeUtil.isInAsianSession()) {
			return null;
		}
		return null;
	}
	
	private List<Order> getOpeningOrder() {
		// TODO Auto-generated method stub
		return null;
	}
}
