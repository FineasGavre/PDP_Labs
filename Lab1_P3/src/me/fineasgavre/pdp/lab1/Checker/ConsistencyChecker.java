package me.fineasgavre.pdp.lab1.Checker;

import me.fineasgavre.pdp.lab1.Models.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsistencyChecker {
    private final ArrayList<Node> parentNodes;

    public ConsistencyChecker(ArrayList<Node> parentNodes) {
        this.parentNodes = parentNodes;
    }

    public boolean runChecker() {
        var isValid = true;

        try {
            lockAllNodes();
            isValid = checkNodes();
        } finally {
            unlockAllNodes();
        }

        return isValid;
    }

    private boolean checkNodes() {
        var nodeSet = new HashSet<Node>();
        AtomicBoolean isValid = new AtomicBoolean(true);

        parentNodes.forEach(parent -> {
            nodeSet.add(parent);
            addChildNodes(parent, nodeSet);
        });

        System.out.println((long) nodeSet.size() + " nodeSet size");

        nodeSet.forEach(node -> {
            if (!checkNode(node)) {
                isValid.set(false);
            }
        });

        return isValid.get();
    }

    private void addChildNodes(Node parent, HashSet<Node> nodeSet) {
        parent.getChildren().forEach(child -> {
            nodeSet.add(child);
            addChildNodes(child, nodeSet);
        });
    }

    private boolean checkNode(Node node) {
        if (node.getParents().size() == 0) {
            return true;
        }

        int expectedValue = node.getParents().stream().reduce(0, (subtotal, node1) -> subtotal + node1.getValue(), Integer::sum);
        return expectedValue == node.getValue();
    }

    private void lockAllNodes() {
        parentNodes.forEach(parent -> {
            parent.mutex.lock();
            lockChildNodes(parent);
        });
    }

    private void lockChildNodes(Node parent) {
        parent.getChildren().forEach(child -> {
            child.mutex.lock();
            lockChildNodes(child);
        });
    }

    private void unlockAllNodes() {
        parentNodes.forEach(parent -> {
            parent.mutex.unlock();
            unlockChildNodes(parent);
        });
    }

    private void unlockChildNodes(Node parent) {
        parent.getChildren().forEach(child -> {
            child.mutex.unlock();
            unlockChildNodes(child);
        });
    }
}
