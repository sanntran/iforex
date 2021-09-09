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
                BinaryDecisionTree tree = new BinaryDecisionTree();
                BinaryDecisionNode node = newBinaryDecisionNode(null, key, decisionMap.get(key));
                tree.getNodes().add(node);
                decisionTrees.add(tree);
            }
        }
        return decisionTrees;
    }

    private static BinaryDecisionNode newBinaryDecisionNode(BinaryDecisionNode parent, String question, Object obj) {
        if (obj instanceof String) {
            return BinaryDecisionNode.builder()
                    .parent(parent)
                    .action(obj.toString())
                    .method(question)
                    .parent(parent)
                    .build();
        }
        LinkedHashMap linkedHashMap = (LinkedHashMap) obj;
        Set keys = linkedHashMap.keySet();
        List<BinaryDecisionNode> nodes = new ArrayList<>();
        BinaryDecisionNode newNode = BinaryDecisionNode.builder()
                .parent(parent)
                .method(question)
                .nodes(nodes)
                .build();
        for (Object key : keys) {
            newNode.getNodes().add(newBinaryDecisionNode(newNode, key.toString(), linkedHashMap.get(key)));
        }
        return newNode;
    }
}
