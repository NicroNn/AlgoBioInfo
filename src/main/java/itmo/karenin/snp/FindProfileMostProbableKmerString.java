package itmo.karenin.snp;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.List;

public class FindProfileMostProbableKmerString extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 6) {
            throw new IllegalArgumentException("Expected Text, k, and a 4 x k profile matrix in the input.");
        }

        String text = lines.get(0);
        int k = Integer.parseInt(lines.get(1));
        double[][] profile = new double[4][k];

        for (int row = 0; row < 4; row++) {
            String[] probabilities = lines.get(row + 2).split("\\s+");
            for (int column = 0; column < k; column++) {
                profile[row][column] = Double.parseDouble(probabilities[column]);
            }
        }

        String bestPattern = text.substring(0, k);
        double bestProbability = probability(bestPattern, profile);

        for (int start = 1; start <= text.length() - k; start++) {
            String pattern = text.substring(start, start + k);
            double currentProbability = probability(pattern, profile);
            if (currentProbability > bestProbability) {
                bestProbability = currentProbability;
                bestPattern = pattern;
            }
        }

        return bestPattern;
    }

    private double probability(String pattern, double[][] profile) {
        double probability = 1.0;
        for (int index = 0; index < pattern.length(); index++) {
            probability *= profile[rowIndex(pattern.charAt(index))][index];
        }
        return probability;
    }

    private int rowIndex(char nucleotide) {
        return switch (nucleotide) {
            case 'A' -> 0;
            case 'C' -> 1;
            case 'G' -> 2;
            case 'T' -> 3;
            default -> throw new IllegalArgumentException("Unsupported nucleotide: " + nucleotide);
        };
    }
}
