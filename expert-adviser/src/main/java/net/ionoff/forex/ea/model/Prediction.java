package net.ionoff.forex.ea.model;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name = "predictions")
public class Prediction {

    @Getter
    public enum Period {
        SHORT(8, 6, 3, Duration.ofMinutes(6)),
        MEDIUM(14, 8, 4, Duration.ofMinutes(30)),
        LONG(20, 10, 5, Duration.ofMinutes(90));
        private final int avgPoints;
        private final int slopePoints;
        private final int slopeSlopePoints;
        private final Duration duration;

        Period(int avgPoints, int slopePoints, int slopeSlopePoints, Duration duration) {
            this.avgPoints = avgPoints;
            this.slopePoints = slopePoints;
            this.slopeSlopePoints = slopeSlopePoints;
            this.duration = duration;
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long candle;
    @Enumerated(value = EnumType.STRING)
    private Period period;
    private Instant time;
    private Double pivot;
    private Double slope;
    private Double average;
    private Double distance;
    private Double slopeSlope;
    private Double slopeDistance;

    @Transient
    public boolean isReady() {
        return pivot != null
            && average != null
            && slope != null
            && slopeSlope != null;
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
