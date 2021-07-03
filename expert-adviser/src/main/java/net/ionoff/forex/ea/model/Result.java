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
@Table(name = "results")
public class Result {

	public static String OPEN = "open";
	public static String CLOSE = "close";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Instant time;
	private String type;
	private Double size;
	private Double price;
	private Double profit;
	private Long orderId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Result order = (Result) o;
		return id == order.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
