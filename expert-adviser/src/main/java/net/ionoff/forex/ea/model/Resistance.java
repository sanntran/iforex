package net.ionoff.forex.ea.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@javax.persistence.Entity
@Table(name = "resistances")
public class Resistance {
    public enum Period {
        SHORT, MEDIUM, LONG
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "candle", referencedColumnName = "id")
    private Candle candle;
    private Instant time;
    private String period;
    private double close;
}
