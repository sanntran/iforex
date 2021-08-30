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
        SHORT(-0.00236), MEDIUM(-0.00236), LONG(-0.00118);
        private final double slope;
        Period(double slope) {
            this.slope = slope;
        }
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
