package net.ionoff.forex.ea.stratergy;

import lombok.extern.slf4j.Slf4j;
import net.ionoff.forex.ea.model.*;
import net.ionoff.forex.ea.repository.AverageRepository;
import net.ionoff.forex.ea.repository.OrderRepository;
import net.ionoff.forex.ea.repository.PredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Slf4j
public class AbstractStrategy {

    static final double SPREAD = 0.00007;
    protected AverageRepository averageRepository;
    protected OrderRepository orderRepository;

    @Autowired
    public AbstractStrategy(AverageRepository averageRepository, OrderRepository orderRepository) {
        this.averageRepository = averageRepository;
        this.orderRepository = orderRepository;
    }

    Average getAvgReadyForAnalyzing() {
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

    Action openOrderSell(Average avg) {
        return Action.openOrder(Order.builder()
                .type(Order.TYPE.SELL.getValue())
                .lots(1d)
                .openPrice(avg.getClose())
                .openTime(avg.getCloseTime())
                .takeProfit(avg.getResistanceCloseMedium())
                .stopLoss(avg.getResistanceCloseMedium())
                .candle(avg.getCandle())
                .build());
    }

    Action openOrderBuy(Average avg) {
        return Action.openOrder(Order.builder()
                .type(Order.TYPE.BUY.getValue())
                .lots(1d)
                .openPrice(avg.getClose())
                .openTime(avg.getCloseTime())
                .takeProfit(avg.getSupportCloseMedium())
                .stopLoss(avg.getSupportCloseMedium())
                .candle(avg.getCandle())
                .build());
    }

    Action closeOrderBuy(Order order, Candle candle) {
        Action action = Action.closeOrder(order);
        action.getOrder().setCloseTime(candle.getTime());
        action.getOrder().setClosePrice(candle.getClose());
        return action;
    }

    Action closeOrderSell(Order order, Candle candle) {
        Action action = Action.closeOrder(order);
        action.getOrder().setCloseTime(candle.getTime());
        action.getOrder().setClosePrice(candle.getClose() + SPREAD);
        return action;
    }

    public Double getStopLossPips(Order order) {
        return Math.abs(getPip(order.getOpenPrice() - order.getStopLoss()));
    }

}
