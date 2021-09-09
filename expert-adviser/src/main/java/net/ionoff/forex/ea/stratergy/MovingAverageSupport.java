package net.ionoff.forex.ea.stratergy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ionoff.forex.ea.model.*;
import net.ionoff.forex.ea.repository.*;
import org.springframework.stereotype.Component;

import static net.ionoff.forex.ea.service.PriceConverter.getPip;

@Slf4j
@Component
@AllArgsConstructor
public class MovingAverageSupport {

    static final double SPREAD = 0.00007;
    private OrderRepository orderRepository;
    private SupportRepository supportRepository;
    private ResistanceRepository resistanceRepository;
    private AverageRepository averageRepository;
    private PredictionRepository predictionRepository;

    public MovingAverageSummary getMovingAverage(Candle candle) {
        return MovingAverageSummary.builder()
                .candle(candle)
                .avgShort(averageRepository.findByCandle(candle.getId(), Average.Period.SHORT.name()).orElse(null))
                .avgMedium(averageRepository.findByCandle(candle.getId(), Average.Period.MEDIUM.name()).orElse(null))
                .avgLong(averageRepository.findByCandle(candle.getId(), Average.Period.LONG.name()).orElse(null))
                .predictShort(predictionRepository.findByCandle(candle.getId(), Prediction.Period.SHORT.name()).orElse(null))
                .predictMedium(predictionRepository.findByCandle(candle.getId(), Prediction.Period.MEDIUM.name()).orElse(null))
                .predictLong(predictionRepository.findByCandle(candle.getId(), Prediction.Period.LONG.name()).orElse(null))
                .supportShort(supportRepository.findByCandle(candle.getId(), Support.Period.SHORT.name()).orElse(null))
                .supportMedium(supportRepository.findByCandle(candle.getId(), Support.Period.MEDIUM.name()).orElse(null))
                .supportLong(supportRepository.findByCandle(candle.getId(), Support.Period.LONG.name()).orElse(null))
                .resistanceShort(resistanceRepository.findByCandle(candle.getId(), Resistance.Period.SHORT.name()).orElse(null))
                .resistanceMedium(resistanceRepository.findByCandle(candle.getId(), Resistance.Period.MEDIUM.name()).orElse(null))
                .resistanceLong(resistanceRepository.findByCandle(candle.getId(), Resistance.Period.LONG.name()).orElse(null))
                .build();
    }

    Action openOrderSell(Candle candle) {
        return Action.open(Order.builder()
                .type(Order.TYPE.SELL.getValue())
                .lots(1d)
                .openPrice(candle.getClose())
                .openTime(candle.getClosed())
                .candle(candle)
                .build());
    }

    Action openOrderBuy(Candle candle) {
        return Action.open(Order.builder()
                .type(Order.TYPE.BUY.getValue())
                .lots(1d)
                .openPrice(candle.getClose())
                .openTime(candle.getClosed())
                .candle(candle)
                .build());
    }

    Action closeOrderBuy(Order order, Candle candle) {
        Action action = Action.close(order);
        action.getOrder().setCloseTime(candle.getTime());
        action.getOrder().setClosePrice(candle.getClose());
        return action;
    }

    Action closeOrderSell(Order order, Candle candle) {
        Action action = Action.close(order);
        action.getOrder().setCloseTime(candle.getTime());
        action.getOrder().setClosePrice(candle.getClose() + SPREAD);
        return action;
    }

    public Double getStopLossPips(Order order) {
        return Math.abs(getPip(order.getOpenPrice() - order.getStopLoss()));
    }

    public MovingAverageAnalyzer newMovingAverageAnalyzer(Order order, MovingAverageSummary ma) {
        return MovingAverageAnalyzer.builder()
                .order(order)
                .ma(ma)
                .build();
    }
}
