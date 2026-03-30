package itmo.karenin.genome;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReconstructStringKmerComposition extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected k and a list of k-mers in the input.");
        }

        List<String> patterns = new ArrayList<>();
        for (int index = 1; index < lines.size(); index++) {
            patterns.add(lines.get(index));
        }

        Map<String, Deque<String>> adjacency = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, Integer> outDegree = new HashMap<>();

        for (String pattern : patterns) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            String suffix = pattern.substring(1);

            adjacency.computeIfAbsent(prefix, ignored -> new ArrayDeque<>()).addLast(suffix);
            outDegree.merge(prefix, 1, Integer::sum);
            inDegree.merge(suffix, 1, Integer::sum);
            inDegree.putIfAbsent(prefix, inDegree.getOrDefault(prefix, 0));
            outDegree.putIfAbsent(suffix, outDegree.getOrDefault(suffix, 0));
        }

        String start = findStartNode(adjacency, inDegree, outDegree);
        List<String> path = findEulerianPath(adjacency, start);

        StringBuilder text = new StringBuilder(path.get(0));
        for (int index = 1; index < path.size(); index++) {
            String node = path.get(index);
            text.append(node.charAt(node.length() - 1));
        }

        return text.toString();
    }

    private List<String> findEulerianPath(Map<String, Deque<String>> adjacency, String start) {
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
        return path;
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
