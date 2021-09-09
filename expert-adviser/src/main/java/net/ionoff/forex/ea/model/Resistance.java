package net.ionoff.forex.ea.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@javax.persistence.Entity
@Table(name = "resistances")
public class Resistance {
    @Getter
    @AllArgsConstructor
    public enum Period {
        SHORT(-0.001), MEDIUM(-0.001), LONG(-0.001);
        private final double slope;
        public double getSlope() {
            return slope;
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "candle", referencedColumnName = "id")
    private Candle candle;
    private Instant time;
    @Enumerated(value = EnumType.STRING)
    private Period period;
    private double close;
}
