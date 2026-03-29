package itmo.karenin.algorithms.strings;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.List;

public class ComputeEditDistanceBetweenTwoStrings extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected two strings in the input.");
        }

        String first = lines.get(0);
        String second = lines.get(1);

        if (second.length() > first.length()) {
            String temporary = first;
            first = second;
            second = temporary;
        }

        int[] previous = new int[second.length() + 1];
        int[] current = new int[second.length() + 1];

        for (int column = 0; column <= second.length(); column++) {
            previous[column] = column;
        }

        for (int row = 1; row <= first.length(); row++) {
            current[0] = row;

            for (int column = 1; column <= second.length(); column++) {
                int substitutionCost = first.charAt(row - 1) == second.charAt(column - 1) ? 0 : 1;

                current[column] = Math.min(
                        Math.min(
                                previous[column] + 1,
                                current[column - 1] + 1
                        ),
                        previous[column - 1] + substitutionCost
                );
            }

            int[] temporary = previous;
            previous = current;
            current = temporary;
        }

        return Integer.toString(previous[second.length()]);
    }
}
