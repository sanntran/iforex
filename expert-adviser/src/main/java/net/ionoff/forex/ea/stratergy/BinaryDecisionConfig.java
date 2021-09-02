package net.ionoff.forex.ea.stratergy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.*;

@Slf4j
public class BinaryDecisionConfig {

    public static List<BinaryDecisionTree> createDecisionTrees() {
        List<BinaryDecisionTree> decisionTrees = new ArrayList<>();
        YamlMapFactoryBean factory = new YamlMapFactoryBean();
        factory.setResources(new ClassPathResource("decision-tree.yml"));
        Map<String, Object> decisionMap = factory.getObject();
        if (decisionMap != null) {
            for (String key : decisionMap.keySet()) {
                BinaryDecisionNode root = newBinaryDecisionNode(key, decisionMap.get(key));
                decisionTrees.add(new BinaryDecisionTree(root));
            }
        }
        return decisionTrees;
    }

    private static BinaryDecisionNode newBinaryDecisionNode(String question, Object node) {
        if (node instanceof String) {
            return BinaryDecisionNode.FinalDecision.ofAction(node.toString());
        }
        LinkedHashMap linkedHashMap = (LinkedHashMap) node;
        Set keys = linkedHashMap.keySet();
        int index = 0;
        BinaryDecisionNode trueNode = null;
        BinaryDecisionNode falseNode = null;
        for (Object key : keys) {
            if (index == 0) {
                trueNode = newBinaryDecisionNode(key.toString(), linkedHashMap.get(key));
            } else if (index == 1) {
                falseNode = newBinaryDecisionNode(key.toString(), linkedHashMap.get(key));
            } else if (index > 1) {
                log.warn("!!!!! Node {} is ignored as node {} expects no more than 2 children nodes", key, question);
            }
            index++;
        }
        return BinaryDecisionNode.builder()
                .question(question)
                .trueNode(trueNode)
                .falseNode(falseNode)
                .build();

    }
}
