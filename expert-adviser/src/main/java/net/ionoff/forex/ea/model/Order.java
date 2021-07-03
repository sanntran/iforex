package net.ionoff.forex.ea.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;

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
		private Integer value;
		TYPE(int value) {
			this.value = value;
		}
		public Integer getValue() {
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
	private Double commission; // for share/stock only
	private String comment;
	private Instant expiration;
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean waitingForClose;

	@JsonIgnore
	@Transient
	public boolean isOpen() {
		return !isClosed();
	}

	@JsonIgnore
	@Transient
	public boolean isClosed() {
		return closeTime != null && closePrice != null;
	}

	@JsonIgnore
	@Transient
	public boolean isSell() {
		return TYPE.SELL.value.equals(type);
	}

	@JsonIgnore
	@Transient
	public boolean isBuy() {
		return TYPE.BUY.value.equals(type);
	}

	@JsonIgnore
	@Transient
	public boolean isWaitingForClose() {
		return Boolean.TRUE.equals(waitingForClose);
	}

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
