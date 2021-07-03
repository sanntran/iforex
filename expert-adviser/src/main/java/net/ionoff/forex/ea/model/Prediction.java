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
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long candle;
    private Instant time;
    private Double close;
    private Double slopeAvgShort;
    private Double slopeAvgLong;
    private Double predictedTime;
    private Double predictedPrice;
}
