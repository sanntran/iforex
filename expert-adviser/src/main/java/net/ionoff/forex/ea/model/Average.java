package net.ionoff.forex.ea.model;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@javax.persistence.Entity
@Table(name = "averages")
public class Average {


    @Getter
    public enum Period {
        SHORT(10, 6, 3),
        MEDIUM(14, 8, 4),
        LONG(20, 10, 5);
        private final int avgPoints;
        private final int slopePoints;
        private final int slopeSlopePoints;
        Period(int avgPoints, int slopePoints, int slopeSlopePoints) {
            this.avgPoints = avgPoints;
            this.slopePoints = slopePoints;
            this.slopeSlopePoints = slopeSlopePoints;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(value = EnumType.STRING)
    private Period period;

    private Instant time;
    private Double close;
    private Double open;
    private Double pivot;
    private Double average;
    private Double slope;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "candle", referencedColumnName = "id")
    private Candle candle;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "support", referencedColumnName = "id")
    private Candle support;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resistance", referencedColumnName = "id")
    private Candle resistance;

    @Transient
    public boolean isReady() {
        return open != null
                && close != null
                && pivot != null
                && average != null
                && slope != null
                && candle != null
                && support != null
                && resistance != null;
    }



    @Transient
    public boolean isShort() {
        return Period.SHORT.equals(period);
    }

    @Transient
    public boolean isMedium() {
        return Period.MEDIUM.equals(period);
    }
}
