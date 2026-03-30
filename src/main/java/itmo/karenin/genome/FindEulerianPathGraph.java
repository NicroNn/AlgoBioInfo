package itmo.karenin.genome;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FindEulerianPathGraph extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        Map<String, Deque<String>> adjacency = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, Integer> outDegree = new HashMap<>();

        for (String line : readNonBlankLines(input)) {
            String[] parts = line.split("\\s*->\\s*");
            String from = parts[0];
            Deque<String> targets = new ArrayDeque<>();

            for (String target : parts[1].split(",")) {
                String trimmedTarget = target.trim();
                targets.addLast(trimmedTarget);
                outDegree.merge(from, 1, Integer::sum);
                inDegree.merge(trimmedTarget, 1, Integer::sum);
                inDegree.putIfAbsent(from, inDegree.getOrDefault(from, 0));
                outDegree.putIfAbsent(trimmedTarget, outDegree.getOrDefault(trimmedTarget, 0));
            }

            adjacency.put(from, targets);
        }

        String start = findStartNode(adjacency, inDegree, outDegree);

        Deque<String> stack = new ArrayDeque<>();
        List<String> path = new ArrayList<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            String node = stack.peek();
            Deque<String> outgoing = adjacency.get(node);

            if (outgoing != null && !outgoing.isEmpty()) {
                stack.push(outgoing.removeFirst());
            } else {
                path.add(stack.pop());
            }
        }

        java.util.Collections.reverse(path);
        return String.join("->", path);
    }

    @Override
    protected String normalizeForComparison(String output) {
        List<String> nodes = List.of(output.trim().split("->"));
        if (nodes.size() < 2) {
            return output.trim();
        }

        List<String> edges = new ArrayList<>();
        for (int index = 0; index < nodes.size() - 1; index++) {
            edges.add(nodes.get(index) + "->" + nodes.get(index + 1));
        }

        return edges.stream()
                .sorted()
                .collect(Collectors.joining("|"));
    }

    private String findStartNode(
            Map<String, Deque<String>> adjacency,
            Map<String, Integer> inDegree,
            Map<String, Integer> outDegree
    ) {
        for (String node : outDegree.keySet()) {
            int out = outDegree.getOrDefault(node, 0);
            int in = inDegree.getOrDefault(node, 0);
            if (out == in + 1) {
                return node;
            }
        }

        return adjacency.keySet().iterator().next();
    }
}
