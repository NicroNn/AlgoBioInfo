package itmo.karenin.snp;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.List;

public class FindMedianString extends AbstractBioinformaticsTask {
    private static final char[] NUCLEOTIDES = {'A', 'C', 'G', 'T'};

    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Expected k and DNA strings in the input.");
        }

        int k = Integer.parseInt(lines.get(0));
        List<String> dna = new ArrayList<>();

        for (int lineIndex = 1; lineIndex < lines.size(); lineIndex++) {
            for (String strand : lines.get(lineIndex).split("\\s+")) {
                if (!strand.isEmpty()) {
                    dna.add(strand);
                }
            }
        }

        String bestPattern = null;
        int bestDistance = Integer.MAX_VALUE;

        for (String pattern : generateAllKmers(k)) {
            int distance = distanceToDna(pattern, dna);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestPattern = pattern;
            }
        }

        return bestPattern;
    }

    @Override
    protected String normalizeForComparison(String output, String input) {
        List<String> inputLines = readNonBlankLines(input);
        int k = Integer.parseInt(inputLines.get(0));
        List<String> dna = new ArrayList<>();
        for (int lineIndex = 1; lineIndex < inputLines.size(); lineIndex++) {
            for (String strand : inputLines.get(lineIndex).split("\\s+")) {
                if (!strand.isEmpty()) {
                    dna.add(strand);
                }
            }
        }

        String pattern = output.trim();
        return Integer.toString(distanceToDna(pattern, dna));
    }

    private List<String> generateAllKmers(int k) {
        List<String> kmers = new ArrayList<>();
        generateAllKmersRecursive(new StringBuilder(), k, kmers);
        return kmers;
    }

    private void generateAllKmersRecursive(StringBuilder prefix, int k, List<String> kmers) {
        if (prefix.length() == k) {
            kmers.add(prefix.toString());
            return;
        }

        for (char nucleotide : NUCLEOTIDES) {
            prefix.append(nucleotide);
            generateAllKmersRecursive(prefix, k, kmers);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    private int distanceToDna(String pattern, List<String> dna) {
        int totalDistance = 0;
        for (String strand : dna) {
            totalDistance += distanceToText(pattern, strand);
        }
        return totalDistance;
    }

    private int distanceToText(String pattern, String text) {
        int best = Integer.MAX_VALUE;
        for (int start = 0; start <= text.length() - pattern.length(); start++) {
            best = Math.min(best, hammingDistance(pattern, text.substring(start, start + pattern.length())));
        }
        return best;
    }

    private int hammingDistance(String left, String right) {
        int distance = 0;
        for (int index = 0; index < left.length(); index++) {
            if (left.charAt(index) != right.charAt(index)) {
                distance++;
            }
        }
        return distance;
    }
}
