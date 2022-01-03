package me.fineasgavre.pdp.lab5;

import java.util.List;

public class SectionMultiplication implements Runnable {
    private final int start;
    private final int end;
    private final Polynomial a;
    private final Polynomial b;
    private final List<Integer> resultingCoefficients;

    public SectionMultiplication(int start, int end, Polynomial a, Polynomial b, List<Integer> resultingCoefficients) {
        this.start = start;
        this.end = end;
        this.a = a;
        this.b = b;
        this.resultingCoefficients = resultingCoefficients;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            if (i >= resultingCoefficients.size()) {
                return;
            }

            for (int j = 0; j <= i; j++) {
                if (j < a.getCoefficients().size() && (i - j) < b.getCoefficients().size()) {
                    resultingCoefficients.set(i, resultingCoefficients.get(i) + a.getCoefficients().get(j) * b.getCoefficients().get(i - j));
                }
            }
        }
    }
}
