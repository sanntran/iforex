package net.ionoff.forex.ea.stratergy;

import net.ionoff.forex.ea.model.*;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.OrderRepository;
import net.ionoff.forex.ea.repository.ResistanceRepository;
import net.ionoff.forex.ea.repository.SupportRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.Math.abs;
import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Component
public class ExtremaPointStrategy extends AbstractStrategy {

    private SupportRepository supportRepository;
    private ResistanceRepository resistanceRepository;



    public ExtremaPointStrategy(AverageRepository averageRepository,
                                 OrderRepository orderRepository,
                                 SupportRepository supportRepository,
                                 ResistanceRepository resistanceRepository) {
        super(averageRepository, orderRepository);
        this.supportRepository = supportRepository;
        this.resistanceRepository = resistanceRepository;
    }

    public Action getAction(Candle candle) {
        Average avg = getAvgReadyForAnalyzing();
        if (avg == null) {
            return Action.noOrder();
        }
        if (avg.isBreakSupportMedium()
                && avg.isAvgLongGoingDown()
                && avg.isAvgShortGoingDown()) {
            System.out.println("OPEN ORDER SELL");
            return openOrderSell(avg);
        } else if (avg.isBreakResistanceMedium()
                && avg.isAvgLongGoingUp()
                && avg.isAvgShortGoingUp()) {
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

    public Action getAction(Order order, Candle candle) {
        Average avg = getAvgReadyForAnalyzing();
        if (avg == null) {
            return Action.noOrder();
        }
        if (order.isBuy()) {
            if (avg.getClose() < order.getStopLoss()
                    || avg.getClose() < order.getTakeProfit()) {
                return closeOrderBuy(order, candle);
            } else if (avg.getSupportCloseShort() != null
                    && avg.getSupportCloseShort() > order.getOpenPrice()) {
                order.setTakeProfit(avg.getSupportCloseShort());
                orderRepository.save(order);
            } else if (avg.isBreakResistanceMedium()) {
                order.setStopLoss(avg.getSupportCloseMedium());
                orderRepository.save(order);
            }
        } else if (order.isSell()) {
            if (avg.getClose() > order.getStopLoss()
                    || avg.getClose() > order.getTakeProfit()) {
                return closeOrderSell(order, candle);
            } else if (avg.getResistanceCloseShort() != null
                    && avg.getResistanceCloseShort() < order.getOpenPrice()) {
                order.setTakeProfit(avg.getResistanceCloseShort());
                orderRepository.save(order);
            } else if (avg.isBreakSupportMedium()) {
                order.setStopLoss(avg.getResistanceCloseMedium());
                orderRepository.save(order);
            }
        }
        return Action.noOrder();
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

}
