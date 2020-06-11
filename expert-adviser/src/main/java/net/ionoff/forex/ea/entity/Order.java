package net.ionoff.forex.ea.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
	
	public enum TYPE {
		BUY(0), SELL(1);
		private int value;
		TYPE(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Long ticket;
	private Double lots;
	private Integer type;
	private Double profit;
	private Instant openTime;
	private Instant closeTime;
	private Double openPrice;
	private Double closePrice;
	private Double stopLoss;
	private Double takeProfit;
	private Double swap;
	// private Double commission; // for share/stock only
	private String comment;
	private Instant expiration;

}
