package net.xapxinh.forex.server.entity;

public class Decision {
	
	public enum NAME {
		IGNORE, BUY, SELL, CLOSE
	}
	
	private String name;
	private Order order;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
}
