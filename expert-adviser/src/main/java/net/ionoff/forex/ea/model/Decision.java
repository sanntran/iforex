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



	private String action;
	private Order order;
}
