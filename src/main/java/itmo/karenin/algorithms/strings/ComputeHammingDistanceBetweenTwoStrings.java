package itmo.karenin.algorithms.strings;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.List;

public class ComputeHammingDistanceBetweenTwoStrings extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected two DNA strings in the input.");
        }

        String first = lines.get(0);
        String second = lines.get(1);

        if (first.length() != second.length()) {
            throw new IllegalArgumentException("Strings must have the same length to compute Hamming distance.");
        }

        int distance = 0;
        for (int index = 0; index < first.length(); index++) {
            if (first.charAt(index) != second.charAt(index)) {
                distance++;
            }
        }

        return Integer.toString(distance);
    }
}
