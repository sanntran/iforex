package net.ionoff.forex.ea.stratergy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ionoff.forex.ea.model.Order;

@Builder
@AllArgsConstructor
public class MovingAverageAnalyzer implements IStrategyAnalyzer {

    private Order order;
    private MovingAverageSummary ma;

    @Override
    public boolean isYes(String question) {
        return false;
    }
}
