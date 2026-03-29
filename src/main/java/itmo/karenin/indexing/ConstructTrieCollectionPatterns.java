package itmo.karenin.indexing;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConstructTrieCollectionPatterns extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> patterns = input.lines()
                .flatMap(line -> List.of(line.trim().split("\\s+")).stream())
                .map(String::trim)
                .filter(pattern -> !pattern.isEmpty())
                .toList();

        List<Node> trie = new ArrayList<>();
        trie.add(new Node());

        List<String> adjacencyList = new ArrayList<>();

        for (String pattern : patterns) {
            int currentNode = 0;

            for (int index = 0; index < pattern.length(); index++) {
                char symbol = pattern.charAt(index);
                Integer nextNode = trie.get(currentNode).next(symbol);

                if (nextNode == null) {
                    nextNode = trie.size();
                    trie.get(currentNode).add(symbol, nextNode);
                    trie.add(new Node());
                    adjacencyList.add(currentNode + "->" + nextNode + ":" + symbol);
                }

                currentNode = nextNode;
            }
        }

        return adjacencyList.stream()
                .sorted(Comparator
                        .comparingInt(this::fromNode)
                        .thenComparingInt(this::toNode))
                .collect(Collectors.joining("\n"));
    }

    @Override
    protected String normalizeForComparison(String output) {
        return output.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(this::normalizeEdge)
                .sorted()
                .collect(Collectors.joining("\n"));
    }

    private String normalizeEdge(String edge) {
        if (edge.contains("->") && edge.contains(":")) {
            return edge;
        }

        String[] parts = edge.split("\\s+");
        if (parts.length == 3) {
            return parts[0] + "->" + parts[1] + ":" + parts[2];
        }

        return edge;
    }

    private int fromNode(String edge) {
        String normalized = normalizeEdge(edge);
        int arrowIndex = normalized.indexOf("->");
        return Integer.parseInt(normalized.substring(0, arrowIndex));
    }

    private int toNode(String edge) {
        String normalized = normalizeEdge(edge);
        int arrowIndex = normalized.indexOf("->");
        int colonIndex = normalized.indexOf(':');
        return Integer.parseInt(normalized.substring(arrowIndex + 2, colonIndex));
    }

    private static final class Node {
        private final List<Edge> edges = new ArrayList<>();

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
    }

    private record Edge(char symbol, int to) {
    }
}
