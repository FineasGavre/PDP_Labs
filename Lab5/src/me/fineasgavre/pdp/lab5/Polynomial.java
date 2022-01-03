package me.fineasgavre.pdp.lab5;

import java.util.List;
import java.util.stream.Collectors;

public class Polynomial {
    private List<Integer> coefficients;

    public Polynomial(List<Integer> coefficients) {
        this.coefficients = coefficients;
    }

    public List<Integer> getCoefficients() {
        return coefficients;
    }

    public int getDegree() {
        return this.coefficients.size() - 1;
    }

    public void removeTrailingZeroes() {
        var degree = this.getDegree();
        while (degree >= 0 && this.getCoefficients().get(degree) == 0) {
            this.coefficients.remove(degree);
            degree--;
        }
    }

    public void invert() {
        this.coefficients = this.coefficients.stream().map(i -> i * -1).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();

        for (int i = this.getDegree(); i >= 0; i--) {
            int coefficient = this.coefficients.get(i);
            if (coefficient != 0) {
                if (i < this.getDegree()) {
                    builder.append("+ ");
                }

                builder.append(coefficient);

                if (i != 0) {
                    builder.append("X");
                }

                if (i != 0 && i != 1) {
                    builder.append("^")
                            .append(i);
                }

                builder.append(" ");
            }
        }

        return builder.toString();
    }
}
