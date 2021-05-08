package net.ionoff.forex.ea.model;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticks")
public class Tick implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "time")
	private Instant time;

	@Column(name = "bid")
	private Double bid;

	@Column(name = "ask")
	private Double ask;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tick order = (Tick) o;
		return id == order.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Tick{" +
				"time=" + time +
				", bid=" + bid +
				", ask=" + ask +
				'}';
	}
}
