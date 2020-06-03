package net.ionoff.forex.ea.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Decision {

	public Decision action(String action) {
		this.action = action;
		return this;
	}

	public enum ACTION {
		NO_ORDER, OPEN_ORDER, CLOSE_ORDER
	}

	private String action;
	private Order order;
}
