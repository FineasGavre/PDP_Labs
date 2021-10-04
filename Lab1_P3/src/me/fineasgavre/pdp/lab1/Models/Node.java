package me.fineasgavre.pdp.lab1.Models;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    public ReentrantLock mutex = new ReentrantLock();

    private final ArrayList<Node> parents = new ArrayList<>();
    private final ArrayList<Node> children = new ArrayList<>();

    private int value;

    public Node(int value) {
        this.value = value;
    }

    public Node() {
        this.value = 0;
    }

    public void incrementValue(int increment) {
        try {
            mutex.lock();

            this.value += increment;

            this.getChildren().forEach(child -> {
                child.incrementValue(increment);
            });
        } finally {
            mutex.unlock();
        }
    }

    public ArrayList<Node> getParents() {
        return this.parents;
    }

    public ArrayList<Node> getChildren() {
        return this.children;
    }

    public int getValue() {
        return value;
    }

    public void addChild(Node child) {
        this.children.add(child);
        child.addParent(this);
        child.incrementValue(value);
    }

    private void addParent(Node parent) {
        this.parents.add(parent);
    }
}
