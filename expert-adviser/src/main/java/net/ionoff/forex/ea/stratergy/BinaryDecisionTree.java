package net.ionoff.forex.ea.stratergy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class BinaryDecisionTree {

    private BinaryDecisionNode root;

    public BinaryDecisionNode.FinalDecision getDecision(IStrategyAnalyzer analyzer) {
        if (root == null) {
            return BinaryDecisionNode.FinalDecision.NONE;
        }
        return root.getDecision(analyzer);
    }

}
