package me.fineasgavre.pdp.lab6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class HamiltonianCycleFinder {
    private static final Logger logger = Logger.getLogger(HamiltonianCycleFinder.class.getName());
    private static final int THREAD_COUNT = 4;

    private final Graph graph;

    public HamiltonianCycleFinder(Graph graph) {
        this.graph = graph;
    }

    public void startSearch() {
        var path = new ArrayList<Integer>();
        path.add(0);

        try {
            search(0, path);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void search(int currentNode, List<Integer> path) throws InterruptedException {
        // Exit cases --
        // We can reach the first node from the current node && the pathCount is == to the number of nodes
        if (graph.edgesFromNode(currentNode).contains(0) && path.size() == graph.size()) {
            logger.info("Found Hamiltonian Cycle: " + path);
            return;
        }

        // We visited all nodes with no luck, we just return
        if (path.size() == graph.size()) {
            return;
        }

        // Search --
        for (int i = 0; i < graph.size(); i++) {
            if (graph.edgesFromNode(currentNode).contains(i) && !checkVisited(i, path)) {
                path.add(i);
                graph.edgesFromNode(currentNode).remove(Integer.valueOf(i));

                var executor = Executors.newFixedThreadPool(THREAD_COUNT);

                int finalI = i;
                executor.execute(() -> {
                    try {
                        search(finalI, path);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                executor.shutdown();
                executor.awaitTermination(30, TimeUnit.SECONDS);

                graph.edgesFromNode(currentNode).add(i);
                path.remove(path.size() - 1);
            }
        }
    }

    private boolean checkVisited(int node, List<Integer> path) {
        return path.stream().anyMatch(i -> i == node);
    }
}
