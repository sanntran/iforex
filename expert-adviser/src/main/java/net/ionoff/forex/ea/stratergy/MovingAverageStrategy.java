package net.ionoff.forex.ea.stratergy;

import lombok.AllArgsConstructor;
import net.ionoff.forex.ea.model.Action;
import net.ionoff.forex.ea.model.Average;
import net.ionoff.forex.ea.model.Order;
import net.ionoff.forex.ea.repository.AverageRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Math.abs;
import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Component
@AllArgsConstructor
public class MovingAverageStrategy {

    public static final double SPREAD = 0.00007;
    private AverageRepository averageRepository;

    private Average getAvgReadyForAnalyzing() {
        List<Average> averages = averageRepository.findLatest(1);
        if (averages.isEmpty()) {
            return null;
        }
        Average avg = averages.get(0);
        if (!avg.isReadyForAnalyzing()) {
            return null;
        }
        return avg;
    }

    public Action getAction() {
        Average avg = getAvgReadyForAnalyzing();
        if (avg == null) {
            return Action.noOrder();
        }

        if (isAvgMediumGoingDownFromTop(avg)) {
            System.out.println("OPEN ORDER SELL");
            return Action.openOrder(
                    Order.builder()
                            .type(Order.TYPE.SELL.getValue())
                            .lots(1d)
                            .openPrice(avg.getClose())
                            .openTime(avg.getCloseTime())
                            .takeProfit(avg.getClose() - 0.01)
                            .stopLoss(avg.getClose() + 0.01)
                            .build());
        } else if (isAvgMediumGoingUpFromBottom(avg)) {
            System.out.println("OPEN ORDER BUY");
            return Action.openOrder(Order.builder()
                    .type(Order.TYPE.BUY.getValue())
                    .lots(1d)
                    .openPrice(avg.getClose())
                    .openTime(avg.getCloseTime())
                    .takeProfit(avg.getClose() + 0.01)
                    .stopLoss(avg.getClose() - 0.01)
                    .build());
        }
        return Action.noOrder();
    }

    public Action getAction(Order order) {
        Average avg = getAvgReadyForAnalyzing();
        if (avg == null) {
            return Action.noOrder();
        }
        if (order.isBuy()
                && (shouldStopLossBuy(avg) || shouldTakeProfitBuy(avg, order))
        ) {
            return closeOrderBuy(order, avg);
        } else if (order.isSell()
                && (shouldStopLossSell(avg) || shouldTakeProfitSell(avg, order))
        ) {
            return closeOrderSell(order, avg);
        }
        return Action.noOrder();
    }

    private Action closeOrderBuy(Order order, Average avg) {
        Action action = Action.closeOrder(order);
        action.getOrder().setCloseTime(avg.getCloseTime());
        action.getOrder().setClosePrice(avg.getClose());
        return action;
    }

    private Action closeOrderSell(Order order, Average avg) {
        Action action = Action.closeOrder(order);
        action.getOrder().setCloseTime(avg.getCloseTime());
        action.getOrder().setClosePrice(avg.getClose() + SPREAD);
        return action;
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

    private boolean isAvgMediumGoingUpFromAvgLong(Average avg) {
        return
                avg.getSlopeAvgLong() != null
                && avg.getSlopeAvgShort() > 0
                && avg.getSlopeAvgLong() > 0
                && avg.getSlopeSlopeAvgMedium() > 0
                && getPip(avg.getDistanceAvgShortAvgLong() ) > 30
                && abs(getPip(avg.getDistanceAvgMediumAvgLong())) <= 7;
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
                && avg.getSlopeSlopeAvgMedium() < 0
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

    private boolean isAvgShortGoingUpFromAvgMedium(Average avg) {
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
}
