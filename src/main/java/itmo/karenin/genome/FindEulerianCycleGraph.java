package itmo.karenin.genome;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FindEulerianCycleGraph extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        Map<String, Deque<String>> adjacency = new HashMap<>();

        for (String line : readNonBlankLines(input)) {
            String[] parts = line.split("\\s*->\\s*");
            String from = parts[0];
            Deque<String> targets = new ArrayDeque<>();
            for (String target : parts[1].split(",")) {
                targets.addLast(target.trim());
            }
            adjacency.put(from, targets);
        }

        String start = adjacency.keySet().iterator().next();
        Deque<String> stack = new ArrayDeque<>();
        List<String> cycle = new ArrayList<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            String node = stack.peek();
            Deque<String> outgoing = adjacency.get(node);

            if (outgoing != null && !outgoing.isEmpty()) {
                stack.push(outgoing.removeFirst());
            } else {
                cycle.add(stack.pop());
            }
        }

        java.util.Collections.reverse(cycle);
        return String.join("->", cycle);
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
}
