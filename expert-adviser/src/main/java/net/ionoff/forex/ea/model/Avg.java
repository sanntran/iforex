package net.ionoff.forex.ea.model;

import java.time.Instant;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

public interface Avg {

    int AVG_SHORT_CANDLES = 4;
    int AVG_MEDIUM_CANDLES = 12;
    int AVG_LONG_CANDLES = 48;
    int SLOPE_AVG_SHORT_CANDLES = 3;
    int SLOPE_AVG_MEDIUM_CANDLES = 5;
    int SLOPE_AVG_LONG_CANDLES = 10;
    int SLOPE_DISTANCE_AVG_SHORT_AVG_LONG_CANDLES = 3;
    int SLOPE_DISTANCE_AVG_MEDIUM_AVG_LONG_CANDLES = 3;

    Instant getTime();
    Double getPivot();
    Double getAvgShort();
    Double getAvgMedium();
    Double getAvgLong();
    Double getDistanceAvgShortAvgLong();
    Double getDistanceAvgMediumAvgLong();
    Double getSlopeAvgShort();
    Double getSlopeAvgMedium();
    Double getSlopeAvgLong();
    Double getSlopeDistanceAvgShortAvgLong();
    Double getSlopeDistanceAvgMediumAvgLong();

    default boolean isReadyForAnalyzing() {
        return getAvgShort() != null
        && getAvgMedium() != null
        //&& getAvgLong() != null
        //&& distanceAvgShortAvgLong() != null
        //&& distanceAvgMediumAvgLong() != null
        && getSlopeAvgShort() != null
        && getSlopeAvgMedium() != null;
        //&& getSlopeAvgLong() != null
        ///&& getSlopeDistanceAvgShortAvgLong() != null
        //&& getSlopeDistanceAvgMediumAvgLong() != null;
    }

    default boolean isAvgShortGoingUp() {
        return getSlopeAvgShort() != null
                && getSlopeAvgShort() > 0;
    }

    default boolean isAvgShortGoingDown() {
        return getSlopeAvgShort() != null
                && getSlopeAvgShort() < 0;
    }

    default boolean isAvgShortGoingUpFast() {
        return getSlopeAvgShort() != null
                && getPip(getSlopeAvgShort()) > 0.00236;
    }

    default boolean isAvgShortGoingDownFast() {
        return getSlopeAvgShort() != null
                && getPip(getSlopeAvgShort()) < -0.00236;
    }

    default boolean isAvgShortGoingUpVeryFast() {
        return getSlopeAvgShort() != null
                && getPip(getSlopeAvgShort()) > 0.0161;
    }

    default boolean isAvgShortGoingDownVeryFast() {
        return getSlopeAvgShort() != null
                && getPip(getSlopeAvgShort()) < -0.0161;
    }

    default boolean isAvgMediumGoingUpFast() {
        return getSlopeAvgMedium() != null
                && getPip(getSlopeAvgMedium()) > 0.00236;
    }

    default boolean isAvgMediumGoingDownFast() {
        return getSlopeAvgMedium() != null
                && getPip(getSlopeAvgMedium()) < -0.00236;
    }

    default boolean isAvgMediumGoingUpVeryFast() {
        return getSlopeAvgMedium() != null
                && getPip(getSlopeAvgMedium()) > 0.00618;
    }
    default boolean isAvgMediumGoingDownVeryFast() {
        return getSlopeAvgMedium() != null
                && getPip(getSlopeAvgMedium()) < -0.00618;
    }

    default boolean isAvgLongGoingUpFast() {
        return getSlopeAvgLong() != null
                && getPip(getSlopeAvgLong()) > 0.00161;
    }
    default boolean isAvgLongGoingDownFast() {
        return getSlopeAvgLong() != null
                && getPip(getSlopeAvgLong()) < -0.00161;
    }

    default boolean isAvgLongGoingUpVeryFast() {
        return getSlopeAvgLong() != null
                && getPip(getSlopeAvgLong()) > 0.00382;
    }
    default boolean isAvgLongGoingDownVeryFast() {
        return getSlopeAvgLong() != null
                && getPip(getSlopeAvgLong()) < -0.00382;
    }

    default boolean isAvgMediumGoingUp() {
        return getSlopeAvgMedium() != null
                && getPip(getSlopeAvgMedium()) > 0.0001;
    }

    default boolean isAvgMediumGoingDown() {
        return getSlopeAvgMedium() != null
                && getPip(getSlopeAvgMedium()) < -0.0001;
    }

    default boolean isAvgLongGoingDown() {
        return getSlopeAvgLong() != null
                && getPip(getSlopeAvgLong()) < 0.0001;
    }

    default boolean isAvgLongGoingUp() {
        return getSlopeAvgLong() != null
                && getSlopeAvgLong() > 0;
    }

    default boolean isDistanceAvgShortAvgLongGoingDown() {
        return getSlopeDistanceAvgShortAvgLong() != null
                && getPip(getSlopeDistanceAvgShortAvgLong()) < -0.0001;
    }

    default boolean isDistanceAvgShortAvgLongGoingUp() {
        return getSlopeDistanceAvgShortAvgLong() != null
                && getPip(getSlopeDistanceAvgShortAvgLong()) > 0.0001;
    }

    default boolean isDistanceAvgShortAvgLongGoingUpFast() {
        return getSlopeDistanceAvgShortAvgLong() != null
                && getPip(getSlopeDistanceAvgShortAvgLong()) > 0.00263;
    }

    default boolean isDistanceAvgShortAvgLongGoingDownFast() {
        return getSlopeDistanceAvgShortAvgLong() != null
                && getPip(getSlopeDistanceAvgShortAvgLong()) < -0.00263;
    }

    default boolean isDistanceAvgMediumAvgLongGoingDown() {
        return getSlopeDistanceAvgMediumAvgLong() != null
                && getPip(getSlopeDistanceAvgMediumAvgLong()) < 0.0001;
    }

    default boolean isDistanceAvgMediumAvgLongGoingUp() {
        return getSlopeDistanceAvgMediumAvgLong() != null
                && getPip(getSlopeDistanceAvgMediumAvgLong()) > 0.0001;
    }
}
