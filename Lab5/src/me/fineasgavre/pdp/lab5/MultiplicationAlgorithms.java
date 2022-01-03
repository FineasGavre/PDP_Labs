package me.fineasgavre.pdp.lab5;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiplicationAlgorithms {
    public static final int THREAD_COUNT = 10;
    public static final int MAX_DEPTH = 10;

    public static Polynomial sequentialRegularMultiplication(Polynomial a, Polynomial b) {
        var resultDegree = a.getDegree() + b.getDegree();
        var resultingCoefficients = new ArrayList<Integer>();

        for (int i = 0; i <= resultDegree; i++) {
            resultingCoefficients.add(0);
        }

        for (int i = 0; i <= a.getDegree(); i++) {
            for (int j = 0; j <= b.getDegree(); j++) {
                resultingCoefficients.set(i + j, resultingCoefficients.get(i + j) + a.getCoefficients().get(i) * b.getCoefficients().get(j));
            }
        }

        var result = new Polynomial(resultingCoefficients);
        result.removeTrailingZeroes();
        return result;
    }

    public static Polynomial parallelRegularMultiplication(Polynomial a, Polynomial b) throws InterruptedException {
        var resultDegree = a.getDegree() + b.getDegree();
        var resultingCoefficients = new ArrayList<Integer>();

        for (int i = 0; i <= resultDegree; i++) {
            resultingCoefficients.add(0);
        }

        var sizePerThread = Math.max(((resultDegree + 1) / THREAD_COUNT), 1);
        var executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i <= resultDegree; i += sizePerThread) {
            executorService.execute(new SectionMultiplication(i, i + sizePerThread, a, b, resultingCoefficients));
        }

        executorService.shutdown();
        executorService.awaitTermination(15, TimeUnit.SECONDS);

        var result = new Polynomial(resultingCoefficients);
        result.removeTrailingZeroes();
        return result;
    }

    public static Polynomial sequentialKaratsubaMultiplication(Polynomial a, Polynomial b) {
        if (a.getDegree() < 2 || b.getDegree() < 2) {
            return MultiplicationAlgorithms.sequentialRegularMultiplication(a, b);
        }

        var len = Math.min(a.getDegree(), b.getDegree()) / 2;

        var lowA = new Polynomial(a.getCoefficients().subList(0, len));
        var highA = new Polynomial(a.getCoefficients().subList(len, a.getCoefficients().size()));
        var lowB = new Polynomial(b.getCoefficients().subList(0, len));
        var highB = new Polynomial(b.getCoefficients().subList(len, b.getCoefficients().size()));

        var z1 = sequentialKaratsubaMultiplication(lowA, lowB);
        var z2 = sequentialKaratsubaMultiplication(PolynomialUtils.add(lowA, highA), PolynomialUtils.add(lowB, highB));
        var z3 = sequentialKaratsubaMultiplication(highA, highB);

        var r1 = PolynomialUtils.createWithLeadingZeroes(z3, 2 * len);
        var r2 = PolynomialUtils.createWithLeadingZeroes(PolynomialUtils.subtract(PolynomialUtils.subtract(z2, z3), z1), len);

        return PolynomialUtils.add(PolynomialUtils.add(r1, r2), z1);
    }

    public static Polynomial parallelKaratsubaMultiplication(Polynomial a, Polynomial b, int currentDepth) throws InterruptedException, ExecutionException {
        if (currentDepth > MAX_DEPTH) {
            return sequentialKaratsubaMultiplication(a, b);
        }

        if (a.getDegree() < 2 || b.getDegree() < 2) {
            return sequentialKaratsubaMultiplication(a, b);
        }

        var len = Math.min(a.getDegree(), b.getDegree()) / 2;

        var lowA = new Polynomial(a.getCoefficients().subList(0, len));
        var highA = new Polynomial(a.getCoefficients().subList(len, a.getCoefficients().size()));
        var lowB = new Polynomial(b.getCoefficients().subList(0, len));
        var highB = new Polynomial(b.getCoefficients().subList(len, b.getCoefficients().size()));

        var executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        var f1 = executorService.submit(() -> parallelKaratsubaMultiplication(lowA, lowB, currentDepth + 1));
        var f2 = executorService.submit(() -> parallelKaratsubaMultiplication(PolynomialUtils.add(lowA, highA), PolynomialUtils.add(lowB, highB), currentDepth + 1));
        var f3 = executorService.submit(() -> parallelKaratsubaMultiplication(highA, highB, currentDepth + 1));
        executorService.shutdown();

        var z1 = f1.get();
        var z2 = f2.get();
        var z3 = f3.get();

        executorService.awaitTermination(15, TimeUnit.SECONDS);

        var r1 = PolynomialUtils.createWithLeadingZeroes(z3, 2 * len);
        var r2 = PolynomialUtils.createWithLeadingZeroes(PolynomialUtils.subtract(PolynomialUtils.subtract(z2, z3), z1), len);

        return PolynomialUtils.add(PolynomialUtils.add(r1, r2), z1);
    }
}
