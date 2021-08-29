package net.ionoff.forex.ea.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vavr.control.Try;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.mapping.Collection;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

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
	private Instant time;
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
	@Transient
	private List<Event> events;
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean waitingForClose;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "candle", referencedColumnName = "id")
	private Candle candle;

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

	public void addEvent(Event event) {
		getEvents().add(event);
		comment = getEvents().stream().map(Enum::name)
				.collect(Collectors.joining(","));
	}

	@Transient
	public List<Event> getEvents() {
		if (events != null) {
			return events;
		}
		events = new ArrayList<>();
		if (comment == null) {
			return events;
		}
		for (String event : comment.split(",")) {
			Optional<Event> e = Try.of(() -> Event.valueOf(event)).toJavaOptional();
			e.ifPresent(ev -> events.add(ev));
		}
		return events;
	}
}
