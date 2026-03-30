package itmo.karenin.genome;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ConstructDeBruijnGraphString extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected k and Text in the input.");
        }

        int k = Integer.parseInt(lines.get(0));
        String text = lines.get(1);

        Map<String, List<String>> adjacency = new TreeMap<>();

        for (int start = 0; start <= text.length() - k; start++) {
            String kmer = text.substring(start, start + k);
            String prefix = kmer.substring(0, k - 1);
            String suffix = kmer.substring(1);

            adjacency.computeIfAbsent(prefix, ignored -> new ArrayList<>()).add(suffix);
        }

        return adjacency.entrySet().stream()
                .map(entry -> entry.getKey() + " -> " + entry.getValue().stream().collect(Collectors.joining(",")))
                .collect(Collectors.joining("\n"));
    }

    @Override
    protected String normalizeForComparison(String output) {
        return output.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(this::normalizeAdjacencyLine)
                .sorted()
                .collect(Collectors.joining("\n"));
    }

    private String normalizeAdjacencyLine(String line) {
        String[] parts = line.split("\\s*->\\s*");
        if (parts.length != 2) {
            return line;
        }

        String[] targets = parts[1].split(",");
        Arrays.sort(targets);
        return parts[0] + " -> " + String.join(",", targets);
    }
}
