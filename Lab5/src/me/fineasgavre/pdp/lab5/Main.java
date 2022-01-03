package me.fineasgavre.pdp.lab5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {
    public static final Logger logger = Logger.getLogger(Main.class.getName());
    public static final Random random = new Random();

    public static void main(String[] args) throws Exception {
//        final var x = new Polynomial(List.of(5, 2, -2, 6, 3));
//        final var y = new Polynomial(List.of(3, 4, -7, 0, 2, 4, 6));

        final var x = generateQuestionablyBigPolynomial();
        final var y = generateQuestionablyBigPolynomial();

        runAndTime("regular sequential multiplication", () -> {
            return MultiplicationAlgorithms.sequentialRegularMultiplication(x, y);
        });

        runAndTime("regular parallel multiplication", () -> {
            try {
                return MultiplicationAlgorithms.parallelRegularMultiplication(x, y);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        });

        runAndTime("Karatsuba sequential multiplication", () -> {
            return MultiplicationAlgorithms.sequentialKaratsubaMultiplication(x, y);
        });

        runAndTime("Karatsuba parallel multiplication", () -> {
            try {
                return MultiplicationAlgorithms.parallelKaratsubaMultiplication(x, y, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    private static void runAndTime(String action, Callable<Polynomial> callable) throws Exception {
        logger.info("Started running " + action);

        var time = System.nanoTime();
        var p = callable.call();
        var timeDifference = System.nanoTime() - time;

        logger.info("Running " + action + " took " + timeDifference + " nanoseconds (" + timeDifference / 1e6 + " ms).");
        // logger.info("Resulting polynomial: " + p);
    }

    private static Polynomial generateQuestionablyBigPolynomial() {
        final var coefficientCount = random.nextInt(10000);
        var coefficients = new ArrayList<Integer>(coefficientCount);

        for (int i = 0; i < coefficientCount; i++) {
            coefficients.add(random.nextInt(10000));
        }

        return new Polynomial(coefficients);
    }

}
