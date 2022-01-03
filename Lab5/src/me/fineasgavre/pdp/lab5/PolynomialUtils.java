package me.fineasgavre.pdp.lab5;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PolynomialUtils {
    public static Polynomial add(Polynomial a, Polynomial b) {
        int minDegree = Math.min(a.getDegree(), b.getDegree());
        int maxDegree = Math.max(a.getDegree(), b.getDegree());

        var coefficients = new ArrayList<Integer>(maxDegree + 1);

        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(a.getCoefficients().get(i) + b.getCoefficients().get(i));
        }

        if (minDegree != maxDegree) {
            if (maxDegree == a.getDegree()) {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(a.getCoefficients().get(i));
                }
            } else {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(b.getCoefficients().get(i));
                }
            }
        }

        return new Polynomial(coefficients);
    }

    public static Polynomial subtract(Polynomial a, Polynomial b) {
        var inverseB = new Polynomial(b.getCoefficients());
        inverseB.invert();

        var polynomial = add(a, inverseB);
        polynomial.removeTrailingZeroes();
        return polynomial;
    }

    public static Polynomial createWithLeadingZeroes(Polynomial start, int leadingCount) {
        var coefficients = IntStream.range(0, leadingCount).mapToObj(i -> 0).collect(Collectors.toList());
        coefficients.addAll(start.getCoefficients());
        return new Polynomial(coefficients);
    }
}
