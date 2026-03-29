package itmo.karenin.ngs;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FindClumpsString extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected Genome and integers k, L, t in the input.");
        }

        String genome = lines.get(0);
        String[] parameters = lines.get(1).split("\\s+");
        if (parameters.length < 3) {
            throw new IllegalArgumentException("Expected k, L, and t on the second line.");
        }

        int k = Integer.parseInt(parameters[0]);
        int windowLength = Integer.parseInt(parameters[1]);
        int threshold = Integer.parseInt(parameters[2]);

        if (k <= 0 || windowLength <= 0 || threshold <= 0) {
            throw new IllegalArgumentException("k, L, and t must be positive.");
        }

        if (k > genome.length() || windowLength > genome.length() || k > windowLength) {
            return "";
        }

        Map<String, Integer> frequencies = new HashMap<>();
        Set<String> clumps = new LinkedHashSet<>();

        int lastStartInWindow = windowLength - k;
        for (int start = 0; start <= lastStartInWindow; start++) {
            String kmer = genome.substring(start, start + k);
            int count = frequencies.getOrDefault(kmer, 0) + 1;
            frequencies.put(kmer, count);
            if (count >= threshold) {
                clumps.add(kmer);
            }
        }

        for (int windowStart = 1; windowStart <= genome.length() - windowLength; windowStart++) {
            String outgoing = genome.substring(windowStart - 1, windowStart - 1 + k);
            int outgoingCount = frequencies.get(outgoing) - 1;
            if (outgoingCount == 0) {
                frequencies.remove(outgoing);
            } else {
                frequencies.put(outgoing, outgoingCount);
            }

            int incomingStart = windowStart + windowLength - k;
            String incoming = genome.substring(incomingStart, incomingStart + k);
            int incomingCount = frequencies.getOrDefault(incoming, 0) + 1;
            frequencies.put(incoming, incomingCount);
            if (incomingCount >= threshold) {
                clumps.add(incoming);
            }
        }

        return String.join(" ", clumps);
    }

    @Override
    protected String normalizeForComparison(String output) {
        String normalized = output.trim();
        if (normalized.isEmpty()) {
            return normalized;
        }

        String[] tokens = normalized.split("\\s+");
        Arrays.sort(tokens);
        return String.join(" ", tokens);
    }
}
