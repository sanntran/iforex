package net.ionoff.forex.ea.stratergy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BinaryDecisionTree {

    private final List<BinaryDecisionNode> nodes = new ArrayList<>();

    public BinaryDecisionNode.FinalDecision getDecision(IStrategyAnalyzer analyzer) {
        for (BinaryDecisionNode node : nodes) {
            if (node.isTrue(analyzer)) {
                return node.getDecision(analyzer);
            }
        }
        if (!nodes.isEmpty()) {
            return BinaryDecisionNode.FinalDecision.ofNone(nodes.get(nodes.size() - 1));
        }
        return BinaryDecisionNode.FinalDecision.ofNone(null);
    }
}
