package net.ionoff.forex.ea.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name = "predictions")
public class Prediction implements Avg {
    // the data is in future when predict
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Instant time;
    private Double pivot;
    private Double avgShort;
    private Double avgMedium;
    private Double avgLong;
    private Double slopeAvgShort;
    private Double slopeAvgMedium;
    private Double slopeAvgLong;
    private Double distanceAvgShortAvgLong;
    private Double distanceAvgMediumAvgLong;
    private Double slopeDistanceAvgShortAvgLong;
    private Double slopeDistanceAvgMediumAvgLong;

}
