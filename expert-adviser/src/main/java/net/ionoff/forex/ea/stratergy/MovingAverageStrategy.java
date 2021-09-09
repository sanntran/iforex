package net.ionoff.forex.ea.stratergy;

import lombok.extern.slf4j.Slf4j;
import net.ionoff.forex.ea.model.Action;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MovingAverageStrategy {

    @Autowired
    @Qualifier("binaryDecisionTreeList")
    private List<BinaryDecisionTree> binaryDecisionTreeList;

    @Autowired
    private MovingAverageSupport movingAverageSupport;

    public Action getAction(Candle candle) {
        return getAction(null, candle);
    }

    public Action getAction(Order order, Candle candle) {
        MovingAverageSummary ma = movingAverageSupport.getMovingAverage(candle);
        MovingAverageAnalyzer analyzer = movingAverageSupport.newMovingAverageAnalyzer(order, ma);
        for (BinaryDecisionTree tree : binaryDecisionTreeList) {
            BinaryDecisionNode.FinalDecision decision = tree.getDecision(analyzer);
            if (!decision.isNone()) {
                log.info(decision.getPath());
            }
            if (decision.isClose()) {
                return close(order, ma);
            } else if (decision.isBuy()) {
                return buy(ma);
            } else if (decision.isSell()) {
                return sell(ma);
            }
        }
        return Action.none();
    }

    private Action sell(MovingAverageSummary ma) {
        return Action.open(Order.builder()
                .type(Order.TYPE.SELL.getValue())
                .lots(1d)
                .time(ma.getCandle().getTime())
                .openTime(ma.getCandle().getTime())
                .openPrice(ma.getCandle().getClose())
                .candle(ma.getCandle())
                .comment(ma.getCandle().getId() + "")
                .build());
    }

    private Action buy(MovingAverageSummary ma) {
        return Action.open(Order.builder()
                .type(Order.TYPE.BUY.getValue())
                .lots(1d)
                .time(ma.getCandle().getTime())
                .openTime(ma.getCandle().getTime())
                .openPrice(ma.getCandle().getClose())
                .candle(ma.getCandle())
                .comment(ma.getCandle().getId() + "")
                .build());
    }

    private Action close(Order order, MovingAverageSummary ma) {
        Action action = Action.close(order);
        action.getOrder().setCloseTime(ma.getCandle().getTime());
        action.getOrder().setClosePrice(ma.getCandle().getClose());
        return action;
    }
}
