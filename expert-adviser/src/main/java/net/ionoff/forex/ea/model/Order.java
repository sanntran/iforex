package net.ionoff.forex.ea.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

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

	@Column(name = "ticket")
	private Long ticket;

	@Column(name = "lots")
	private Double lots;

	@Column(name = "type")
	private Integer type;

	@Column(name = "profit")
	private Double profit;

	@Column(name = "open_time")
	private Instant openTime;

	@Column(name = "close_time")
	private Instant closeTime;

	@Column(name = "open_price")
	private Double openPrice;

	@Column(name = "close_price")
	private Double closePrice;

	@Column(name = "stop_loss")
	private Double stopLoss;

	@Column(name = "take_profit")
	private Double takeProfit;

	@Column(name = "swap")
	private Double swap;

	@Column(name = "commission")
	private Double commission; // for share/stock only

	@Column(name = "comment")
	private String comment;

	@Column(name = "expiration")
	private Instant expiration;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return id == order.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
