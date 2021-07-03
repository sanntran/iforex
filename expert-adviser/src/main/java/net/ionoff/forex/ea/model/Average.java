package net.ionoff.forex.ea.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@javax.persistence.Entity
@Table(name = "averages")
public class Average {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long candle;
    private Instant time;
    private Double close;
    private Double pivot;
    private Double avgShort;
    private Double avgMedium;
    private Double avgLong;
    private Double distanceAvgShortAvgLong;
    private Double distanceAvgMediumAvgLong;

    private Double slopeAvgShort;
    private Double slopeAvgMedium;
    private Double slopeAvgLong;
    private Double slopeShortAvgShort;
    private Double slopeShortAvgMedium;
    private Double slopeShortAvgLong;

    private Double slopeDistanceAvgShortAvgLong;
    private Double slopeShortDistanceAvgShortAvgLong;

    private Double slopeDistanceAvgMediumAvgLong;
    private Double slopeShortDistanceAvgMediumAvgLong;

    private Double slopeSlopeAvgMedium;

    private Double supportCloseShort;
    private Double resistanceCloseShort;

    private Double supportMediumClose;
    private Double resistanceMediumClose;

    private Double supportLongClose;
    private Double resistanceLongClose;

    @Transient
    public Instant getCloseTime() {
        return time.plusSeconds(60); // period 1 minute
    }

    public Double getDistanceCloseAvgMedium() {
        if (close != null && avgMedium != null) {
            return close - avgMedium;
        }
        return null;

    }

    public Double getDistanceCloseAvgLong() {
            if (close != null && avgLong != null) {
            return close - avgLong;
        }
        return null;
    }

    public Double getDistanceAvgShortAvgMedium() {
        if (avgShort != null && avgMedium != null) {
            return avgShort - avgMedium;
        }
        return null;
    }

    public boolean isReadyForAnalyzing() {
        // all fields are required not null
        for (Field field : Average.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (field.get(this) == null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                return false;
            }
        }
        return true;
    }

    public boolean isShortAvgGoingUp() {
        return slopeShortAvgShort != null
                && slopeShortAvgShort > 0;
    }

    public boolean isShortAvgGoingDown() {
        return slopeShortAvgShort != null
                && slopeShortAvgShort < 0;
    }

    public boolean isShortAvgGoingUpAndBreakResistance() {
        return slopeShortAvgShort != null
                && resistanceAvgShort != null
                && avgShort != null
                && resistanceAvgShort != null
                && slopeShortAvgShort > 0
                && avgShort > resistanceAvgShort;
    }

    public boolean isShortAvgGoingDownAndBreakSupport() {
        return slopeShortAvgShort != null
                && supportAvgShort != null
                && avgShort != null
                && resistanceAvgShort != null
                && slopeShortAvgShort < 0
                && avgShort < supportAvgShort;
    }

    private boolean isAvgMediumGoingUp() {
        return slopeAvgMedium != null
                && slopeShortAvgMedium > 0;
    }

    private boolean isAvgMediumGoingDown() {
        return slopeAvgMedium != null
                && slopeAvgMedium < 0;
    }

    private boolean isAvgLongGoingDown() {
        return slopeAvgLong != null
                && slopeAvgLong < 0;
    }

    private boolean isAvgLongGoingUp() {
        return slopeAvgLong != null
                && slopeAvgLong > 0;
    }
}
