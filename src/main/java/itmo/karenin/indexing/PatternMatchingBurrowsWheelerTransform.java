package itmo.karenin.indexing;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PatternMatchingBurrowsWheelerTransform extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected Text and at least one pattern in the input.");
        }

        String text = lines.get(0);
        List<String> patterns = new ArrayList<>();
        for (int lineIndex = 1; lineIndex < lines.size(); lineIndex++) {
            for (String pattern : lines.get(lineIndex).split("\\s+")) {
                if (!pattern.isEmpty()) {
                    patterns.add(pattern);
                }
            }
        }

        Integer[] suffixArray = buildSuffixArray(text);
        Set<Integer> matchedPositions = new LinkedHashSet<>();

        for (String pattern : patterns) {
            int firstMatch = lowerBound(text, suffixArray, pattern);
            int afterLastMatch = upperBound(text, suffixArray, pattern);

            for (int index = firstMatch; index < afterLastMatch; index++) {
                matchedPositions.add(suffixArray[index]);
            }
        }

        return matchedPositions.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }

    private Integer[] buildSuffixArray(String text) {
        Integer[] suffixArray = new Integer[text.length()];
        for (int index = 0; index < text.length(); index++) {
            suffixArray[index] = index;
        }

        Arrays.sort(suffixArray, Comparator.comparing(text::substring));
        return suffixArray;
    }

    private int lowerBound(String text, Integer[] suffixArray, String pattern) {
        int left = 0;
        int right = suffixArray.length;

        while (left < right) {
            int middle = (left + right) / 2;
            String suffix = text.substring(suffixArray[middle]);
            if (suffix.compareTo(pattern) < 0) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return left;
    }

    private int upperBound(String text, Integer[] suffixArray, String pattern) {
        int left = 0;
        int right = suffixArray.length;

        while (left < right) {
            int middle = (left + right) / 2;
            String suffix = text.substring(suffixArray[middle]);
            if (startsWith(suffix, pattern) || suffix.compareTo(pattern) < 0) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return left;
    }

    private boolean startsWith(String text, String prefix) {
        return text.length() >= prefix.length() && text.startsWith(prefix);
    }

    @Override
    protected String normalizeForComparison(String output) {
        List<Integer> positions = new ArrayList<>();

        for (String line : output.lines().map(String::trim).filter(line -> !line.isEmpty()).toList()) {
            if (line.contains(":")) {
                String suffix = line.substring(line.indexOf(':') + 1).trim();
                if (!suffix.isEmpty()) {
                    for (String token : suffix.split("\\s+")) {
                        positions.add(Integer.parseInt(token));
                    }
                }
            } else {
                for (String token : line.split("\\s+")) {
                    if (!token.isEmpty()) {
                        positions.add(Integer.parseInt(token));
                    }
                }
            }
        }

        return positions.stream()
                .distinct()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }
}
