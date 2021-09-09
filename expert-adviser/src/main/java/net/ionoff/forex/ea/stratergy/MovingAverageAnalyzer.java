package net.ionoff.forex.ea.stratergy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import net.ionoff.forex.ea.model.*;

import java.lang.reflect.Method;

@Slf4j
@Builder
@AllArgsConstructor
public class MovingAverageAnalyzer implements IStrategyAnalyzer {

    private Order order;
    private MovingAverageSummary ma;

    @Override
    public boolean invoke(String method) {
        try {
            Method isTrue = MovingAverageAnalyzer.class.getMethod(method);
            return (Boolean) isTrue.invoke(this);
        } catch (Exception e) {
            log.error("Error when invoke {}. {}", method, e.getMessage(), e);
            return false;
        }
    }

    public boolean hasNoOrder() {
        return order == null;
    }
    public boolean hasOrderBuy() {
        return order != null && order.isBuy();
    }
    public boolean hasOrderSell() {
        return order != null && order.isSell();
    }

    public boolean isPredictPriceCloseToAvgMedium() {
        return ma.isShortReady()
                && ma.getDistanceCloseAvgShort().asPip() < 20 && ma.getDistanceCloseAvgShort().asPip() > -20;
    }

    public boolean isPredictPriceCloseToAvgShort() {
        return ma.isShortReady()
                && ma.getDistanceCloseAvgShort().asPip() < 10 && ma.getDistanceCloseAvgShort().asPip() > -10;
    }

    public boolean isPredictAvgShortLowerThanAvgMedium() {
        return ma.isShortReady() && ma.isMediumReady()
                && ma.getDistanceAvgShortAvgMedium().asPip() < 0;
    }

    public boolean isPredictAvgShortHigherThanAvgMedium() {
        return ma.isShortReady() && ma.isMediumReady()
                && ma.getDistanceAvgShortAvgMedium().asPip() > 0;
    }

    public boolean isPredictAvgMediumLowerThanAvgLong() {
        return ma.isMediumReady() && ma.isLongReady()
                && ma.getDistanceAvgMediumAvgLong().asPip() < 0;
    }

    public boolean isPredictAvgMediumHigherThanAvgLong() {
        return ma.isMediumReady() && ma.isLongReady()
                && ma.getDistanceAvgMediumAvgLong().asPip() > 0;
    }

    public boolean isPredictPriceHigherThanAvgShort() {
        return ma.isShortReady() && ma.getCandle().getClose() > ma.getAvgShort().getAverage();
    }

    public boolean isPriceLowerThanAvgShort() {
        return ma.isMediumReady() && ma.getCandle().getClose() < ma.getAvgShort().getAverage();
    }

    public boolean isPriceHigherThanAvgMedium() {
        return ma.isMediumReady()
                && ma.getCandle().getClose() > ma.getAvgMedium().getAverage();
    }

    public boolean isPriceLowerThanAvgMedium() {
        return ma.isMediumReady()
                && ma.getCandle().getClose() < ma.getAvgMedium().getAverage();
    }

    public boolean isPredictAvgLongGoingUpFast() {
        return ma.isLongReady()
                && ma.getPredictSlopeLong().asReadableSlope() > Slope.Period.LONG.getReadableSlopeFast();
    }
    public boolean isPredictAvgLongGoingDownFast() {
        return ma.isLongReady()
                && ma.getPredictSlopeLong().asReadableSlope() < -Slope.Period.LONG.getReadableSlopeFast();
    }
    public boolean isPredictAvgMediumGoingUpFast() {
        return ma.isMediumReady()
                && ma.getPredictSlopeMedium().asReadableSlope() > Slope.Period.MEDIUM.getReadableSlopeFast();
    }
    public boolean isPredictAvgMediumGoingDownFast() {
        return ma.isMediumReady()
                && ma.getPredictSlopeMedium().asReadableSlope() < -Slope.Period.MEDIUM.getReadableSlopeFast();
    }
    public boolean isPredictAvgShortGoingUpFast() {
        return ma.isShortReady()
                && ma.getPredictSlopeShort().asReadableSlope() > Slope.Period.SHORT.getReadableSlopeFast();
    }
    public boolean isPredictAvgShortGoingDownFast() {
        return ma.isShortReady()
                && ma.getPredictSlopeShort().asReadableSlope() < -Slope.Period.SHORT.getReadableSlopeFast();
    }
    public boolean isPredictAvgLongGoingUp() {
        return ma.isLongReady()
                && ma.getPredictSlopeLong().asReadableSlope() > Support.Period.LONG.getSlope();
    }
    public boolean isPredictAvgLongGoingDown() {
        return ma.isLongReady()
                && ma.getPredictSlopeLong().asReadableSlope() < Resistance.Period.LONG.getSlope();
    }
    public boolean isPredictAvgMediumGoingUp() {
        return ma.isMediumReady()
                && ma.getPredictSlopeMedium().asReadableSlope() > Support.Period.MEDIUM.getSlope();
    }
    public boolean isPredictAvgMediumGoingDown() {
        return ma.isMediumReady()
                && ma.getPredictSlopeMedium().asReadableSlope() < Resistance.Period.MEDIUM.getSlope();
    }
    public boolean isPredictAvgShortGoingUp() {
        return ma.isShortReady()
                && ma.getPredictSlopeShort().asReadableSlope() > Support.Period.SHORT.getSlope();
    }
    public boolean isPredictAvgShortGoingDown() {
        return ma.isShortReady()
                && ma.getPredictSlopeShort().asReadableSlope() < Resistance.Period.SHORT.getSlope();
    }

    public boolean isPredictAvgLongStartGoingUp() {
        return ma.isLongReady()
            && ma.getPredictLong().getAverage() > ma.getAvgLong().getAverage();
    }
    public boolean isPredictAvgLongStartGoingDown() {
        return ma.isLongReady()
                && ma.getPredictLong().getAverage() < ma.getAvgLong().getAverage();
    }

    public boolean isPredictAvgLongStoppingGoingUp() {
        return isPredictAvgLongGoingUp()
                && ma.getPredictSlopeSlopeLong().asReadableSlopeSlope() < 0;
    }

    public boolean isPredictAvgLongStoppingGoingDown() {
        return isPredictAvgLongGoingDown()
                && ma.getPredictSlopeSlopeLong().asReadableSlopeSlope() > 0;
    }

    public boolean isPredictAvgMediumStartGoingDown() {
        return ma.isMediumReady()
                && ma.getPredictMedium().getAverage() < ma.getAvgMedium().getAverage();
    }
    public boolean isPredictAvgMediumStartGoingUp() {
        return  ma.isMediumReady()
                && ma.getPredictMedium().getAverage() > ma.getAvgMedium().getAverage();
    }

    public boolean isPredictAvgMediumStoppingGoingDown() {
        return ma.isMediumReady()
                && ma.getPredictSlopeMedium().asReadableSlope() < 0
                && ma.getPredictSlopeSlopeMedium().asReadableSlopeSlope() > 0;
    }
    public boolean isPredictAvgMediumStoppingGoingUp() {
        return ma.isMediumReady()
                && ma.getPredictSlopeMedium().asReadableSlope() > 0
                && ma.getPredictSlopeSlopeMedium().asReadableSlopeSlope() < 0;
    }

    public boolean isPredictAvgShortStartGoingDown() {
        return ma.isShortReady()
                && ma.getPredictShort().getAverage() < ma.getAvgShort().getAverage();
    }
    public boolean isPredictAvgShortStartGoingUp() {
        return ma.isShortReady()
                && ma.getPredictShort().getAverage() > ma.getAvgShort().getAverage();
    }

    public boolean isPredictAvgShortContinueGoingDown() {
        return isPredictAvgShortGoingDown()
                && !isPredictAvgShortStoppingGoingDown()
                && ma.getPredictShort().getAverage() < ma.getAvgShort().getAverage();
    }
    public boolean isPredictAvgShortContinueGoingUp() {
        return isPredictAvgShortGoingUp()
                && !isPredictAvgShortStoppingGoingUp()
                && ma.getPredictShort().getAverage() > ma.getAvgShort().getAverage();
    }

    public boolean isPredictAvgMediumContinueGoingDown() {
        return isPredictAvgMediumGoingDown()
                && !isPredictAvgMediumStoppingGoingDown()
                && ma.getPredictMedium().getAverage() < ma.getAvgMedium().getAverage();
    }
    public boolean isPredictAvgMediumContinueGoingUp() {
        return isPredictAvgMediumGoingUp()
                && !isPredictAvgMediumStoppingGoingUp()
                && ma.getPredictMedium().getAverage() > ma.getAvgMedium().getAverage();
    }

    public boolean isPredictAvgLongContinueGoingDown() {
        return isPredictAvgLongGoingDown()
                && !isPredictAvgLongStoppingGoingDown()
                && ma.getPredictLong().getAverage() < ma.getAvgLong().getAverage();
    }
    public boolean isPredictAvgLongContinueGoingUp() {
        return isPredictAvgLongGoingUp()
                && !isPredictAvgLongStoppingGoingUp()
                && ma.getPredictLong().getAverage() > ma.getAvgLong().getAverage();
    }

    public boolean isPredictAvgShortStoppingGoingDown() {
        return ma.isShortReady()
                && ma.getPredictSlopeShort().asReadableSlope() < 0
                && ma.getPredictSlopeSlopeShort().asReadableSlopeSlope() > 0;
    }
    public boolean isPredictAvgShortStoppingGoingUp() {
        return ma.isShortReady()
                && ma.getPredictSlopeShort().asReadableSlope() > 0
                && ma.getPredictSlopeSlopeShort().asReadableSlopeSlope() < 0;
    }

    public boolean isCloseVeryHighFromAvgShort() {
        return ma.isShortReady() && ma.getDistanceCloseAvgShort().asPip() > 50;
    }

    public boolean isAvgMediumVeryHighFromAvgLong() {
        return ma.isMediumReady() && ma.isLongReady() && ma.getDistanceAvgMediumAvgLong().asPip() > 300;
    }

    public boolean isPredictAvgLongGoingSideways() {
        return ma.isLongReady()
                && !isPredictAvgLongGoingDown()
                && !isPredictAvgLongGoingUp();
    }

    public boolean isPredictAvgMediumGoingSideways() {
        return ma.isMediumReady()
                && !isPredictAvgMediumGoingDown()
                && !isPredictAvgMediumGoingUp();
    }

    public boolean isAvgShortVeryHighFromAvgMedium() {
        return ma.isShortReady() && ma.isMediumReady() && ma.getDistanceAvgShortAvgMedium().asPip() > 300;
    }
}
