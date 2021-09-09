package net.ionoff.forex.ea.stratergy;

import lombok.Builder;
import lombok.Getter;
import net.ionoff.forex.ea.model.*;

@Getter
@Builder
public class MovingAverageSummary {

    private Candle candle;
    private Average avgShort;
    private Average avgMedium;
    private Average avgLong;
    private Prediction predictShort;
    private Prediction predictMedium;
    private Prediction predictLong;
    private Support supportShort;
    private Support supportMedium;
    private Support supportLong;
    private Resistance resistanceShort;
    private Resistance resistanceMedium;
    private Resistance resistanceLong;

    boolean isReady() {
        return isShortReady() && isMediumReady();
    }

    boolean isShortReady() {
        return candle != null
                && avgShort != null
                && avgShort.isReady()
                && predictShort != null
                && predictShort.isReady()
                && supportShort != null
                && resistanceShort != null;
    }

    boolean isMediumReady() {
        return avgMedium != null
                && avgMedium.isReady()
                && predictMedium != null
                && predictMedium.isReady()
                && supportMedium != null
                && resistanceMedium != null;
    }

    boolean isLongReady() {
        return avgLong != null
                && avgLong.isReady()
                && supportLong != null
                && predictLong != null
                && predictLong.isReady()
                && resistanceLong != null;
    }

    Distance getDistanceAvgShortAvgMedium() {
        return Distance.of(avgShort.getAverage(), avgMedium.getAverage());
    }

    Distance getDistanceAvgMediumAvgLong() {
        return Distance.of(avgMedium.getAverage(), avgLong.getAverage());
    }

    Distance getDistanceCloseAvgShort() {
        return Distance.of(candle.getClose(), avgShort.getAverage());
    }

    Slope getPredictSlopeShort() {
        return Slope.of(predictShort.getSlope(), predictShort.getPeriod().name());
    }

    Slope getPredictSlopeSlopeShort() {
        return Slope.of(predictShort.getSlopeSlope(), predictShort.getPeriod().name());
    }

    Slope getPredictSlopeMedium() {
        return Slope.of(predictMedium.getSlope(), predictMedium.getPeriod().name());
    }

    Slope getPredictSlopeSlopeMedium() {
        return Slope.of(predictMedium.getSlopeSlope(), predictMedium.getPeriod().name());
    }

    Slope getPredictSlopeLong() {
        return Slope.of(predictLong.getSlope(), predictLong.getPeriod().name());
    }

    Slope getPredictSlopeSlopeLong() {
        return Slope.of(predictLong.getSlopeSlope(), predictLong.getPeriod().name());
    }
}
