package net.ionoff.forex.ea.stratergy;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryDecisionNode {

    @AllArgsConstructor
    public static class FinalDecision {
        private String result;
        private BinaryDecisionNode node;
        public boolean isNone() {
            return "none".equals(result);
        }
        public boolean isClose() {
            return "close".equals(result);
        }
        public boolean isSell() {
            return "sell".equals(result);
        }
        public boolean isBuy() {
            return "buy".equals(result);
        }

        public static FinalDecision ofNone(BinaryDecisionNode node) {
            return new FinalDecision("none", node);
        }

        public static FinalDecision ofResult(String result, BinaryDecisionNode node) {
            return new FinalDecision(result, node);
        }

        public String getPath() {
            return node.getPath() + ": " + result;
        }
    }

    private String action;
    private String method;
    private Boolean answer;
    private BinaryDecisionNode parent;
    private List<BinaryDecisionNode> nodes;

    public Boolean isTrue(IStrategyAnalyzer analyzer) {
        if (method == null) {
            throw new IllegalStateException("Method cannot be null");
        }
        answer = analyzer.invoke(method);
        return answer;
    }

    public FinalDecision getDecision(IStrategyAnalyzer analyzer) {
        if (!Boolean.TRUE.equals(answer)) {
            throw new IllegalStateException("Cannot make decision when answer is false");
        }
        if (action == null) {
            for (BinaryDecisionNode node : nodes) {
                if (node.isTrue(analyzer)) {
                    return node.getDecision(analyzer);
                }
            }
            return FinalDecision.ofNone(nodes.get(nodes.size() - 1));
        } else {
            return FinalDecision.ofResult(action, this);
        }
    }

    public boolean isRoot() {
        return parent == null;
    }

    public String getPath() {
        if (isRoot()) {
            return method;
        }
        return getParent().getPath() + "." + method;
    }
}
