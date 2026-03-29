package itmo.karenin.ngs;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FindMostFrequentWordsString extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected a DNA string and k in the input.");
        }

        String text = lines.get(0);
        int k = Integer.parseInt(lines.get(1));

        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive.");
        }

        if (k > text.length()) {
            return "";
        }

        Map<String, Integer> frequencies = new HashMap<>();
        int maxFrequency = 0;

        for (int start = 0; start <= text.length() - k; start++) {
            String kmer = text.substring(start, start + k);
            int frequency = frequencies.getOrDefault(kmer, 0) + 1;
            frequencies.put(kmer, frequency);
            maxFrequency = Math.max(maxFrequency, frequency);
        }

        Set<String> mostFrequentKmers = new LinkedHashSet<>();
        for (int start = 0; start <= text.length() - k; start++) {
            String kmer = text.substring(start, start + k);
            if (frequencies.get(kmer) == maxFrequency) {
                mostFrequentKmers.add(kmer);
            }
        }

        return String.join(" ", mostFrequentKmers);
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
