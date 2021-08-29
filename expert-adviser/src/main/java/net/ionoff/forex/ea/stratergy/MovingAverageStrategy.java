package net.ionoff.forex.ea.stratergy;

import net.ionoff.forex.ea.model.*;
import net.ionoff.forex.ea.repository.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Component
public class MovingAverageStrategy extends AbstractStrategy {

    private SupportRepository supportRepository;
    private ResistanceRepository resistanceRepository;
    protected PredictionRepository predictionRepository;

    public MovingAverageStrategy(AverageRepository averageRepository,
                                 OrderRepository orderRepository,
                                 SupportRepository supportRepository,
                                 ResistanceRepository resistanceRepository,
                                 PredictionRepository predictionRepository) {
        super(averageRepository, orderRepository);
        this.supportRepository = supportRepository;
        this.resistanceRepository = resistanceRepository;
        this.predictionRepository = predictionRepository;
    }

    public Action getAction(Candle candle) {
        if (candle == null) {
            return Action.noOrder();
        }
        Prediction prediction = getPredictionReadyForAnalyzing();
        if (prediction == null) {
            return Action.noOrder();
        }
        Average avg = getAvgReadyForAnalyzing();
        if (avg == null) {
            return Action.noOrder();
        }
        if (prediction.isAvgShortGoingDownFast()
            && prediction.isAvgMediumGoingDown()
            && prediction.getAvgShort() < avg.getAvgMedium()
                && isCloseToAvgLong(candle, prediction)
        ) {
            System.out.println("OPEN ORDER SELL");
            return openOrderSell(avg);
        } else if (
            prediction.isAvgShortGoingUpFast()
            && prediction.isAvgMediumGoingUp()
            && prediction.getAvgShort() > avg.getAvgMedium()
            && isCloseToAvgLong(candle, prediction)) {
            System.out.println("OPEN ORDER BUY");
            return openOrderBuy(avg);
        }
        return Action.noOrder();
    }

    private boolean isRetraceUpLessThanGoldenRatio(Average avg) {
        // less than 38.2%
        Optional<Resistance> prevResistance = resistanceRepository.findPreviousShort();
        if (!prevResistance.isPresent()) {
            return false;
        }
        Double retraceLength = avg.getResistanceCloseShort() - avg.getSupportCloseShort();
        Double waveLength = prevResistance.get().getClose() - avg.getSupportCloseShort();
        if (retraceLength / waveLength < 0.5) {
            return true;
        }
        return false;
    }

    private boolean isRetraceDownLessThanGoldenRatio(Average avg) {
        // less than 38.2%
        Optional<Support> prevSupport = supportRepository.findPreviousShort();
        if (!prevSupport.isPresent()) {
            return false;
        }
        Double retraceLength = avg.getResistanceCloseShort() - avg.getSupportCloseShort();
        Double waveLength = avg.getResistanceCloseShort() - prevSupport.get().getClose();
        if (retraceLength / waveLength < 0.5) {
            return true;
        }
        return false;
    }

    private Prediction getPredictionReadyForAnalyzing() {
        List<Prediction> predictions = predictionRepository.findLatest(1);
        if (predictions.isEmpty()) {
            return null;
        }
        Prediction prediction = predictions.get(0);
        if (!prediction.isReadyForAnalyzing()) {
            return null;
        }
        return prediction;
    }

    public Action getAction(Order order, Candle candle) {
        if (candle == null) {
            return Action.noOrder();
        }
        Prediction prediction = getPredictionReadyForAnalyzing();
        if (prediction == null) {
            return Action.noOrder();
        }
        if (order.isBuy()) {
            if (prediction.getAvgShort() < prediction.getAvgMedium()) {
                return closeOrderBuy(order, candle);
            }
        } else if (order.isSell()) {
            if (prediction.getAvgShort() > prediction.getAvgMedium()) {
                return closeOrderSell(order, candle);
            }
        }
        return Action.noOrder();
    }

    private boolean isCloseToAvgLong(Candle candle, Prediction prediction) {
        return prediction.getAvgLong() != null && Math.abs(getPip(candle.getClose() - prediction.getAvgLong())) < 100;
    }

    private boolean isProfitGoingToZero(Order order, Average avg) {
        if (order.isBuy()) {
            return getPip(avg.getClose() - avg.getAvgLong()) < 0;
        } else if (order.isSell()) {
            return getPip(avg.getClose() - avg.getAvgLong()) > 0;
        }
        return false;
    }

    private boolean shouldTakeProfitBuy(Average avg, Order order) {
        if (getPip(avg.getClose() - order.getOpenPrice()) >= 200
                && avg.getSlopeDistanceAvgShortAvgLong() < 0
        ) {
            System.out.println("TakeProfitBuy >= 200");
            return true;
        }
        return (getPip(avg.getDistanceAvgShortAvgLong()) >= 130
                && avg.getSlopeDistanceAvgShortAvgLong() < 0);
    }

    private boolean shouldStopLossBuy(Average avg) {
        if ((getPip(avg.getDistanceAvgShortAvgLong()) > 1
                && getPip((avg.getClose() - avg.getAvgLong())) < -1)) {
            System.out.println("SLOPLOSS getDistanceAvgShortAvgLong");
            return true;
        }
        if (avg.getSlopeDistanceAvgMediumAvgLong() < 0
                && avg.getClose() > avg.getAvgShort()
                && avg.getAvgShort() > avg.getAvgMedium()) {
            System.out.println("SLOPLOSS getSlopeDistanceAvgMediumAvgLong");
            return true;
        }
        return false;
    }

    private boolean shouldStopLossSell(Average avg) {
        if ((getPip(avg.getDistanceAvgShortAvgLong()) < -1
            && getPip((avg.getPivot() - avg.getAvgLong())) > 1)) {
            System.out.println("SLOPLOSS getDistanceAvgShortAvgLong");
            return true;
        }
        if (avg.getSlopeDistanceAvgMediumAvgLong() > 0
            && avg.getPivot() > avg.getAvgShort()
            && avg.getAvgShort() > avg.getAvgMedium()) {
            System.out.println("SLOPLOSS getSlopeDistanceAvgMediumAvgLong");
            return true;
        }
        return false;
    }

    private boolean shouldTakeProfitSell(Average avg, Order order) {
        if (getPip((avg.getClose() - order.getOpenPrice())) <= -200
                && avg.getSlopeDistanceAvgShortAvgLong() > 0
        ) {
            System.out.println("TakeProfitSell >= 200");
            return true;
        }
        return (getPip(avg.getDistanceAvgShortAvgLong()) <= -130
                && avg.getSlopeDistanceAvgShortAvgLong() > 0);
    }


    private boolean isAvgMediumGoingUpFromBottom(Average avg) {
       return
               (abs(avg.getSlopeAvgMedium()) > 0
            && avg.getSlopeDistanceAvgMediumAvgLong() > 0
            && getPip(avg.getDistanceAvgMediumAvgLong()) <= -99
            && abs(getPip(avg.getDistanceCloseAvgMedium())) <= 9)
        || (
               avg.getSlopeDistanceAvgMediumAvgLong() > 0
               && getPip(avg.getDistanceCloseAvgLong()) < -300
        );

    }

    private boolean isAvgMediumGoingDownFromAvgLong(Average avg) {
        return  avg.getSlopeAvgLong() != null
                && avg.getSlopeAvgShort() < 0
                && avg.getSlopeAvgLong() < 0
                && getPip(avg.getDistanceAvgShortAvgLong() ) < -30
                && abs(getPip(avg.getDistanceAvgMediumAvgLong())) <= 7;
    }

    private boolean isAvgMediumGoingDownFromTop(Average avg) {
        return (avg.getSlopeAvgMedium() < 0
                && avg.getSlopeDistanceAvgMediumAvgLong() < 0
                && getPip(avg.getDistanceAvgMediumAvgLong()) >= 99
                && abs(getPip(avg.getDistanceCloseAvgMedium())) <= 9)

            || (
                avg.getSlopeDistanceAvgMediumAvgLong() < 0
                && getPip(avg.getDistanceCloseAvgLong()) > 300
            );

    }

    private boolean shouldOpenOrderBuy(Average avg, Double currentPrice) {
        return avg.isAvgShortGoingUpFast()
                && avg.isDistanceAvgShortAvgLongGoingUp()
                && currentPrice < avg.getAvgShort();
    }

    private boolean mustOpenOrderBuy(Average avg, Double currentPrice) {
        return avg.isAvgShortGoingUpFast()
                && avg.getSlopeDistanceAvgShortAvgLong() > avg.getSlopeDistanceAvgMediumAvgLong();
    }
}
