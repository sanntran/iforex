package net.ionoff.forex.ea.stratergy;

import net.ionoff.forex.ea.model.Action;
import net.ionoff.forex.ea.model.Candle;
import net.ionoff.forex.ea.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

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
        MovingAverageAnalyzer analyzer = movingAverageSupport.newMovingAverageAnalyzer(order, candle);
        for (BinaryDecisionTree tree : binaryDecisionTreeList) {
            BinaryDecisionNode.FinalDecision decision = tree.getDecision(analyzer);
            if (!decision.isNone()) {
                return Action.none();
            }
        }
        return Action.none();
    }

}
