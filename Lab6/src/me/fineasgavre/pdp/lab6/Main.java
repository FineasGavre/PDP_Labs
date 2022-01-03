package me.fineasgavre.pdp.lab6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        // HCycle => 0-1-2-4-3-0
        var graphWithHamiltonianCycle = new Graph(
                new ArrayList<>(List.of(0,1,2,3,4)),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1)),
                        new ArrayList<>(List.of(2, 3)),
                        new ArrayList<>(List.of(4)),
                        new ArrayList<>(List.of(0)),
                        new ArrayList<>(List.of(1, 3, 4))
                )));

        // Removed 4-3 edge, no more happy Hamilton
        var graphWithoutHamiltonianCycle = new Graph(
                new ArrayList<>(List.of(0,1,2,3,4)),
                new ArrayList<>(List.of(
                        new ArrayList<>(List.of(1)),
                        new ArrayList<>(List.of(2, 3)),
                        new ArrayList<>(List.of(4)),
                        new ArrayList<>(List.of(0)),
                        new ArrayList<>(List.of(1, 4))
                )));

        var randomGraph = new Graph(10);


        final var shouldFindFinder = new HamiltonianCycleFinder(graphWithHamiltonianCycle);
        final var noFindFinder = new HamiltonianCycleFinder(graphWithoutHamiltonianCycle);
        final var randomGraphFinder = new HamiltonianCycleFinder(randomGraph);

        runAndTime("Hamiltonian cycle finder that should find a cycle", shouldFindFinder::startSearch);
        runAndTime("Hamiltonian cycle finder that should NOT find a cycle", noFindFinder::startSearch);
        runAndTime("Hamiltonian cycle finder that can maybe find a cycle", randomGraphFinder::startSearch);
    }

    private static void runAndTime(String action, Runnable runnable) {
        logger.info("Started running " + action);

        var time = System.nanoTime();
        runnable.run();
        var timeDifference = System.nanoTime() - time;

        logger.info("Running " + action + " took " + timeDifference + " nanoseconds (" + timeDifference / 1e6 + " ms).");
    }
}
