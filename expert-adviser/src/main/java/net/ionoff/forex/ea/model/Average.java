package net.ionoff.forex.ea.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@javax.persistence.Entity
@Table(name = "averages")
public class Average implements Avg {

    public static final Double MAX_DISTANCE = 1165D;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "candle", referencedColumnName = "id")
    private Candle candle;
    private Instant time;
    private Double close;
    private Double open;
    private Double pivot;
    private Double avgShort;
    private Double avgMedium;
    private Double avgLong;
    private Double distanceAvgShortAvgLong;
    private Double distanceAvgMediumAvgLong;
    private Double slopeAvgShort;
    private Double slopeAvgMedium;
    private Double slopeAvgLong;
    private Double slopeDistanceAvgShortAvgLong;
    private Double slopeDistanceAvgMediumAvgLong;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "support_candle_short", referencedColumnName = "id")
    private Candle supportCandleShort;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resistance_candle_short", referencedColumnName = "id")
    private Candle resistanceCandleShort;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "support_candle_medium", referencedColumnName = "id")
    private Candle supportCandleMedium;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resistance_candle_medium", referencedColumnName = "id")
    private Candle resistanceCandleMedium;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "support_candle_long", referencedColumnName = "id")
    private Candle supportCandleLong;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resistance_candle_long", referencedColumnName = "id")
    private Candle resistanceCandleLong;

    private Double takeProfitLong;
    private Double takeProfitMedium;

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
        return avgShort != null
        && avgMedium != null
        //&& avgLong != null
        //&& distanceAvgShortAvgLong != null
        //&& distanceAvgMediumAvgLong != null
        && slopeAvgShort != null
        && slopeAvgMedium != null;
        //&& slopeAvgLong != null
        ///&& slopeDistanceAvgShortAvgLong != null
        //&& slopeDistanceAvgMediumAvgLong != null;
    }

    public boolean isAvgShortGoingUpAndBreakResistance() {
        if(isBreakResistanceShort()) {
            return isAvgShortGoingUp();
        } else {
            return false;
        }
    }

    public boolean isAvgShortGoingDownAndBreakSupport() {
        if (isBreakSupportShort()) {
            return isAvgShortGoingDown();
        } else {
            return false;
        }
    }

    public boolean isBreakSupportShort() {
        return supportCandleShort != null
        && close < supportCandleShort.getClose()
        && open > supportCandleShort.getClose();
    }

    public boolean isBreakResistanceShort() {
        return  resistanceCandleShort != null
                && close > resistanceCandleShort.getClose()
                && open < resistanceCandleShort.getClose();
    }

    public boolean isBreakSupportMedium() {
        return supportCandleMedium != null
                && close < supportCandleMedium.getClose()
                && open > supportCandleMedium.getClose();
    }

    public boolean isBreakResistanceMedium() {
        return  resistanceCandleMedium != null
                && close > resistanceCandleMedium.getClose()
                && open < resistanceCandleMedium.getClose();
    }

    public boolean isBreakSupportLong() {
        return supportCandleLong != null
                && close < supportCandleLong.getClose()
                && open > supportCandleLong.getClose();
    }

    public boolean isBreakResistanceLong() {
        return  resistanceCandleLong != null
                && close > resistanceCandleLong.getClose()
                && open < resistanceCandleLong.getClose();
    }

    public boolean isAvgMediumGoingUpAndBreakResistance() {
        return slopeAvgMedium != null
                && resistanceCandleMedium != null
                && avgMedium != null
                && resistanceCandleMedium != null
                && slopeAvgMedium > 0
                && avgMedium > resistanceCandleMedium.getClose();
    }

    public boolean isMediumAvgGoingDownAndBreakSupport() {
        return slopeAvgMedium != null
                && supportCandleMedium != null
                && avgMedium != null
                && supportCandleMedium != null
                && slopeAvgMedium < 0
                && avgMedium < supportCandleMedium.getClose();
    }

    public boolean isLongAvgGoingUpAndBreakResistance() {
        return slopeAvgLong != null
                && resistanceCandleLong != null
                && avgLong != null
                && resistanceCandleLong != null
                && slopeAvgLong > 0
                && avgMedium > resistanceCandleLong.getClose();
    }

    public boolean isLongAvgGoingDownAndBreakSupport() {
        return slopeAvgLong != null
                && supportCandleLong != null
                && avgLong != null
                && supportCandleLong != null
                && slopeAvgLong < 0
                && avgLong < supportCandleLong.getClose();
    }


    public boolean isAtBottom() {
        return close != null
                && avgLong != null
                && getPip(close - avgLong) < -MAX_DISTANCE;
    }

    public boolean isAtTop() {
        return close != null
                && avgLong != null
                && getPip(close - avgLong) > MAX_DISTANCE;
    }

    public Double getSupportCloseShort() {
        return supportCandleShort == null ? null : supportCandleShort.getClose();
    }
    public Double getResistanceCloseShort() {
        return resistanceCandleShort == null ? null : resistanceCandleShort.getClose();
    }
    public Double getSupportCloseMedium() {
        return supportCandleMedium == null ? null : supportCandleMedium.getClose();
    }
    public Double getResistanceCloseMedium() {
        return resistanceCandleMedium == null ? null : resistanceCandleMedium.getClose();
    }
    public Double getSupportCloseLong() {
        return supportCandleLong == null ? null : supportCandleLong.getClose();
    }
    public Double getResistanceCloseLong() {
        return resistanceCandleLong == null ? null : resistanceCandleLong.getClose();
    }
    public Instant getSupportTimeMedium() {
        return supportCandleMedium == null ? null : supportCandleMedium.getTime();
    }
    public Instant getResistanceTimeMedium() {
        return resistanceCandleMedium == null ? null : resistanceCandleMedium.getTime();
    }
    public Instant getSupportTimeLong() {
        return supportCandleLong == null ? null : supportCandleLong.getTime();
    }
    public Instant getResistanceTimeLong() {
        return resistanceCandleLong == null ? null : resistanceCandleLong.getTime();
    }

    public boolean isGoingUpFromAvgMedium() {
        return close != null
                && avgShort != null
                && avgMedium != null
                && getPip(close - avgMedium) > 0 && getPip(close - avgMedium) > 30;
    }

    public boolean isGoingDownFromAvgMedium() {
        return close != null
                && avgShort != null
                && avgMedium != null
                && getPip(close - avgMedium) < 0
                && getPip(close - avgMedium) > -30;
    }

    public boolean isGoingUpFromAvgLong() {
        return close != null
                && avgShort != null
                && avgLong != null
                && getPip(close - avgLong) > 0
                && getPip(close - avgLong) < 30;
    }

    public boolean isGoingDownFromAvgLong() {
        return close != null
                && avgShort != null
                && avgLong != null
                && getPip(close - avgLong) < 0
                && getPip(close - avgLong) > -30;
    }

    public double getAbsDistanceCloseAvgLongPip() {
        return Math.abs(getPip(getDistanceCloseAvgLong()));
    }

    public double getAbsDistanceAvgMediumAvgLongPip() {
        return Math.abs(getPip(getDistanceAvgMediumAvgLong()));
    }

}
