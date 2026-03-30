package itmo.karenin.phylogenetics;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ConstructUPGMATree extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected n and a distance matrix in the input.");
        }

        int n = Integer.parseInt(lines.get(0));
        double[][] matrix = new double[n][n];
        for (int row = 0; row < n; row++) {
            String[] values = lines.get(row + 1).split("\\s+");
            for (int column = 0; column < n; column++) {
                matrix[row][column] = Double.parseDouble(values[column]);
            }
        }

        Map<Integer, Cluster> clusters = new HashMap<>();
        Map<Integer, Map<Integer, Double>> distances = new HashMap<>();
        Map<Integer, List<Edge>> tree = new HashMap<>();

        for (int node = 0; node < n; node++) {
            clusters.put(node, new Cluster(node, 1, 0.0));
            distances.put(node, new HashMap<>());
            tree.put(node, new ArrayList<>());
        }

        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                distances.get(row).put(column, matrix[row][column]);
            }
        }

        int nextNodeId = n;

        while (clusters.size() > 1) {
            Pair closest = findClosestPair(clusters.keySet(), distances);
            Cluster left = clusters.get(closest.left());
            Cluster right = clusters.get(closest.right());

            int newNodeId = nextNodeId++;
            double age = closest.distance() / 2.0;
            Cluster merged = new Cluster(newNodeId, left.size() + right.size(), age);

            tree.putIfAbsent(newNodeId, new ArrayList<>());
            addBidirectionalEdge(tree, newNodeId, left.id(), age - left.age());
            addBidirectionalEdge(tree, newNodeId, right.id(), age - right.age());

            Map<Integer, Double> newDistances = new HashMap<>();
            for (int otherId : new TreeSet<>(clusters.keySet())) {
                if (otherId == left.id() || otherId == right.id()) {
                    continue;
                }

                Cluster other = clusters.get(otherId);
                double distance = (
                        distances.get(left.id()).get(otherId) * left.size()
                                + distances.get(right.id()).get(otherId) * right.size()
                ) / (left.size() + right.size());

                newDistances.put(otherId, distance);
                distances.get(otherId).put(newNodeId, distance);
            }

            distances.put(newNodeId, new HashMap<>(newDistances));
            distances.get(newNodeId).put(newNodeId, 0.0);

            clusters.remove(left.id());
            clusters.remove(right.id());

            distances.remove(left.id());
            distances.remove(right.id());
            for (Map<Integer, Double> row : distances.values()) {
                row.remove(left.id());
                row.remove(right.id());
            }

            clusters.put(newNodeId, merged);
        }

        return tree.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .flatMap(entry -> entry.getValue().stream()
                        .sorted(Comparator.comparingInt(Edge::to))
                        .map(edge -> formatEdge(entry.getKey(), edge)))
                .collect(Collectors.joining("\n"));
    }

    private Pair findClosestPair(Iterable<Integer> clusterIds, Map<Integer, Map<Integer, Double>> distances) {
        double bestDistance = Double.POSITIVE_INFINITY;
        int bestLeft = -1;
        int bestRight = -1;

        List<Integer> ids = new ArrayList<>();
        clusterIds.forEach(ids::add);
        ids.sort(Integer::compareTo);

        for (int i = 0; i < ids.size(); i++) {
            for (int j = i + 1; j < ids.size(); j++) {
                int left = ids.get(i);
                int right = ids.get(j);
                double distance = distances.get(left).get(right);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestLeft = left;
                    bestRight = right;
                }
            }
        }

        return new Pair(bestLeft, bestRight, bestDistance);
    }

    private void addBidirectionalEdge(Map<Integer, List<Edge>> tree, int from, int to, double weight) {
        tree.computeIfAbsent(from, ignored -> new ArrayList<>()).add(new Edge(to, weight));
        tree.computeIfAbsent(to, ignored -> new ArrayList<>()).add(new Edge(from, weight));
    }

    private String formatEdge(int from, Edge edge) {
        return from + "->" + edge.to() + ":" + String.format(java.util.Locale.US, "%.3f", edge.weight());
    }

    @Override
    protected String normalizeForComparison(String output) {
        return output.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .sorted()
                .collect(Collectors.joining("\n"));
    }

    private record Cluster(int id, int size, double age) {
    }

    private record Edge(int to, double weight) {
    }

    private record Pair(int left, int right, double distance) {
    }
}
