package itmo.karenin.genome;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ConstructDeBruijnGraphCollectionKmers extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> patterns = readNonBlankLines(input);
        Map<String, List<String>> adjacency = new TreeMap<>();

        for (String pattern : patterns) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            String suffix = pattern.substring(1);
            adjacency.computeIfAbsent(prefix, ignored -> new ArrayList<>()).add(suffix);
        }

        return adjacency.entrySet().stream()
                .map(entry -> entry.getKey() + " -> " + String.join(",", entry.getValue()))
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
