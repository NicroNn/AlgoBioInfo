package itmo.karenin.indexing;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ImplementTrieMatching extends AbstractBioinformaticsTask {
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

        List<Node> trie = buildTrie(patterns);
        Set<Integer> matchedPositions = new LinkedHashSet<>();

        for (int start = 0; start < text.length(); start++) {
            int currentNode = 0;

            for (int position = start; position < text.length(); position++) {
                Integer nextNode = trie.get(currentNode).next(text.charAt(position));
                if (nextNode == null) {
                    break;
                }

                currentNode = nextNode;
                if (!trie.get(currentNode).matchedPatterns().isEmpty()) {
                    matchedPositions.add(start);
                }
            }
        }

        return matchedPositions.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }

    private List<Node> buildTrie(List<String> patterns) {
        List<Node> trie = new ArrayList<>();
        trie.add(new Node());

        for (String pattern : patterns) {
            int currentNode = 0;

            for (int index = 0; index < pattern.length(); index++) {
                char symbol = pattern.charAt(index);
                Integer nextNode = trie.get(currentNode).next(symbol);

                if (nextNode == null) {
                    nextNode = trie.size();
                    trie.get(currentNode).add(symbol, nextNode);
                    trie.add(new Node());
                }

                currentNode = nextNode;
            }

            trie.get(currentNode).matchedPatterns().add(pattern);
        }

        return trie;
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

    private static final class Node {
        private final List<Edge> edges = new ArrayList<>();
        private final List<String> matchedPatterns = new ArrayList<>();

        Integer next(char symbol) {
            for (Edge edge : edges) {
                if (edge.symbol == symbol) {
                    return edge.to;
                }
            }
            return null;
        }

        void add(char symbol, int to) {
            edges.add(new Edge(symbol, to));
        }

        List<String> matchedPatterns() {
            return matchedPatterns;
        }
    }

    private record Edge(char symbol, int to) {
    }
}
