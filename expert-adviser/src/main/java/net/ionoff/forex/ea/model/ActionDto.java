package net.ionoff.forex.ea.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActionDto {
	private Long ticket;
	private Double lots;
	private Integer type;
	private Double stopLoss;
	private Double takeProfit;
	private String comment;
}
