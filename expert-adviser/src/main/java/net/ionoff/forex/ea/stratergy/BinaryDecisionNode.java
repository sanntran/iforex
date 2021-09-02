package net.ionoff.forex.ea.stratergy;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryDecisionNode {

    @Getter
    @AllArgsConstructor
    public static class FinalDecision extends BinaryDecisionNode {
        public static final FinalDecision NONE = ofAction("none");

        private String action;

        public boolean isNone() {
            return "none".equals(action);
        }

        @Override
        public FinalDecision getDecision(IStrategyAnalyzer analyzer) {
            if (analyzer.isYes(getQuestion())) {
                return this;
            }
            return NONE;
        }

        @Override
        public boolean isFinalDecision() {
            return true;
        }


        public static FinalDecision ofAction(String action) {
            return new FinalDecision(action);
        }
    }

    private String question;
    private BinaryDecisionNode trueNode;
    private BinaryDecisionNode falseNode;

    public FinalDecision getDecision(IStrategyAnalyzer analyzer) {
        boolean yes = analyzer.isYes(question);
        if (yes && trueNode != null) {
            return trueNode.getDecision(analyzer);
        }
        if (!yes && falseNode != null) {
            return falseNode.getDecision(analyzer);
        }
        return FinalDecision.NONE;
    }

    public boolean isFinalDecision() {
        return false;
    }
}
