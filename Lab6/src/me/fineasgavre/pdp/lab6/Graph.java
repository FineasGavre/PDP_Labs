package me.fineasgavre.pdp.lab6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class Graph {
    private static final Random random = new Random();

    private final List<Integer> nodes;
    private List<List<Integer>> edges;

    public Graph(List<Integer> nodes, List<List<Integer>> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public Graph(int nodes) {
        this.nodes = new ArrayList<>();
        for (var i = 0; i < nodes; ++i) {
            this.nodes.add(i);
        }
        generateEdges();
    }

    public void addEdge(int n1, int n2) {
        if (!edges.get(n1).contains(n2)) {
            edges.get(n1).add(n2);
        }
    }

    public void generateEdges() {
        edges = new ArrayList<>();
        for (var ignored : nodes) {
            edges.add(new ArrayList<>());
        }

        var size = nodes.size() * nodes.size();

        for (int i = 0; i < nodes.size() - 1; i++) {
            addEdge(i, i + 1);
        }

        addEdge(nodes.size() - 1, 0);

        for (int i = 0; i < size / 2; i++) {
            int nodeA = random.nextInt(nodes.size());
            int nodeB = random.nextInt(nodes.size());

            addEdge(nodeA, nodeB);
        }
    }

    public List<Integer> edgesFromNode(int node) {
        return edges.get(node);
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    public List<List<Integer>> getEdges() {
        return edges;
    }

    public int size() {
        return nodes.size();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Graph.class.getSimpleName() + "[", "]")
                .add("nodes=" + nodes)
                .add("edges=" + edges)
                .toString();
    }
}