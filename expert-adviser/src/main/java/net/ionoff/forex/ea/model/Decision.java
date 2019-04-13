package net.ionoff.forex.ea.model;

public class Decision {
	
	public enum NAME {
		NO_ORDER, OPEN_ORDER, CLOSE_ORDER
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
