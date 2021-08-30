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
        SHORT(12, 6, 3, Duration.ofMinutes(5)),
        MEDIUM(24, 6, 3, Duration.ofMinutes(60)),
        LONG(36, 6, 3, Duration.ofMinutes(240));
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
    @Enumerated(value = EnumType.STRING)
    private Average.Period period;
    private Instant time;
    private Double pivot;
    private Double average;
    private Double slope;
    private Double distance;
    private Double slopeSlope;
    private Double slopeDistance;
}
