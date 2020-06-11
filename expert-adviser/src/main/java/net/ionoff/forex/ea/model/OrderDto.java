package net.ionoff.forex.ea.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Builder
public class OrderDto {
	private Long ticket;
	private Double lots;
	private Integer type;
	private Double profit;
	private String openTime;
	private String closeTime;
	private Double openPrice;
	private Double closePrice;
	private Double stopLoss;
	private Double takeProfit;
	private Double swap;
	private String comment;
	private String expiration;
}
