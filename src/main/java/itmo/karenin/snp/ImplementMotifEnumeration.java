package itmo.karenin.snp;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ImplementMotifEnumeration extends AbstractBioinformaticsTask {
    private static final char[] NUCLEOTIDES = {'A', 'C', 'G', 'T'};

    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected k, d, and DNA strings in the input.");
        }

        String[] parameters = lines.get(0).split("\\s+");
        int k = Integer.parseInt(parameters[0]);
        int d = Integer.parseInt(parameters[1]);

        List<String> dna = new ArrayList<>();
        for (int lineIndex = 1; lineIndex < lines.size(); lineIndex++) {
            for (String strand : lines.get(lineIndex).split("\\s+")) {
                if (!strand.isEmpty()) {
                    dna.add(strand);
                }
            }
        }

        Set<String> motifs = new LinkedHashSet<>();
        for (String strand : dna) {
            for (int start = 0; start <= strand.length() - k; start++) {
                String pattern = strand.substring(start, start + k);
                for (String neighbor : neighbors(pattern, d)) {
                    if (appearsInAll(neighbor, dna, d)) {
                        motifs.add(neighbor);
                    }
                }
            }
        }

        String result = motifs.stream()
                .sorted()
                .collect(Collectors.joining(" "));
        return result.isEmpty() ? "nan" : result;
    }

    @Override
    protected String normalizeForComparison(String output) {
        String normalized = output.trim();
        if (normalized.equalsIgnoreCase("nan")) {
            return "";
        }

        return output.lines()
                .flatMap(line -> List.of(line.trim().split("\\s+")).stream())
                .filter(token -> !token.isEmpty())
                .sorted()
                .collect(Collectors.joining(" "));
    }

    private boolean appearsInAll(String pattern, List<String> dna, int d) {
        for (String strand : dna) {
            if (!appearsWithMismatches(pattern, strand, d)) {
                return false;
            }
        }
        return true;
    }

    private boolean appearsWithMismatches(String pattern, String text, int d) {
        int k = pattern.length();
        for (int start = 0; start <= text.length() - k; start++) {
            if (hammingDistance(pattern, text.substring(start, start + k)) <= d) {
                return true;
            }
        }
        return false;
    }

    private Set<String> neighbors(String pattern, int d) {
        Set<String> result = new LinkedHashSet<>();
        generateNeighbors(pattern.toCharArray(), 0, d, result);
        return result;
    }

    private void generateNeighbors(char[] current, int index, int mismatchesLeft, Set<String> result) {
        if (index == current.length) {
            result.add(new String(current));
            return;
        }

        char original = current[index];

        generateNeighbors(current, index + 1, mismatchesLeft, result);

        if (mismatchesLeft == 0) {
            return;
        }

        for (char nucleotide : NUCLEOTIDES) {
            if (nucleotide == original) {
                continue;
            }

            current[index] = nucleotide;
            generateNeighbors(current, index + 1, mismatchesLeft - 1, result);
        }

        current[index] = original;
    }

    private int hammingDistance(String left, String right) {
        int mismatches = 0;
        for (int index = 0; index < left.length(); index++) {
            if (left.charAt(index) != right.charAt(index)) {
                mismatches++;
            }
        }
        return mismatches;
    }
}
