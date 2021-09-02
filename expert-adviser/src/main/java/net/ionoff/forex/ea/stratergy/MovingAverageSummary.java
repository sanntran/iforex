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

    public boolean isReady() {
        return isShortReady() && isMediumReady();
    }

    private boolean isShortReady() {
        return candle != null
                && avgShort != null
                && avgShort.isReady()
                && predictShort != null
                && predictShort.isReady()
                && supportShort != null
                && resistanceShort != null;
    }

    private boolean isMediumReady() {
        return avgMedium != null
                && avgMedium.isReady()
                && predictMedium != null
                && predictMedium.isReady()
                && supportMedium != null
                && resistanceMedium != null;
    }

    private boolean isLongReady() {
        return avgLong != null
                && avgLong.isReady()
                && supportLong != null
                && predictLong != null
                && predictLong.isReady()
                && resistanceLong != null;
    }
}
