package itmo.karenin.phylogenetics;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class ImplementNeighborJoining extends AbstractBioinformaticsTask {
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

        List<Integer> labels = new ArrayList<>();
        for (int index = 0; index < n; index++) {
            labels.add(index);
        }

        Map<Integer, List<Edge>> tree = new HashMap<>();
        NextNodeId nextNodeId = new NextNodeId(n);
        neighborJoin(matrix, labels, tree, nextNodeId);

        return tree.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .flatMap(entry -> entry.getValue().stream()
                        .sorted(Comparator.comparingInt(Edge::to))
                        .map(edge -> formatEdge(entry.getKey(), edge)))
                .collect(Collectors.joining("\n"));
    }

    @Override
    protected String normalizeForComparison(String output) {
        return output.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .sorted()
                .collect(Collectors.joining("\n"));
    }

    private void neighborJoin(
            double[][] matrix,
            List<Integer> labels,
            Map<Integer, List<Edge>> tree,
            NextNodeId nextNodeId
    ) {
        int n = labels.size();
        if (n == 2) {
            int left = labels.get(0);
            int right = labels.get(1);
            addBidirectionalEdge(tree, left, right, matrix[0][1]);
            return;
        }

        double[] totalDistance = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                totalDistance[i] += matrix[i][j];
            }
        }

        int bestI = -1;
        int bestJ = -1;
        double bestValue = Double.POSITIVE_INFINITY;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double value = (n - 2) * matrix[i][j] - totalDistance[i] - totalDistance[j];
                if (value < bestValue) {
                    bestValue = value;
                    bestI = i;
                    bestJ = j;
                }
            }
        }

        double delta = (totalDistance[bestI] - totalDistance[bestJ]) / (n - 2);
        double limbI = (matrix[bestI][bestJ] + delta) / 2.0;
        double limbJ = (matrix[bestI][bestJ] - delta) / 2.0;

        int newLabel = nextNodeId.next();

        List<Integer> reducedLabels = new ArrayList<>();
        for (int index = 0; index < n; index++) {
            if (index != bestI && index != bestJ) {
                reducedLabels.add(labels.get(index));
            }
        }
        reducedLabels.add(newLabel);

        double[][] reducedMatrix = new double[n - 1][n - 1];
        List<Integer> oldIndexes = new ArrayList<>();
        for (int index = 0; index < n; index++) {
            if (index != bestI && index != bestJ) {
                oldIndexes.add(index);
            }
        }

        for (int row = 0; row < oldIndexes.size(); row++) {
            for (int column = 0; column < oldIndexes.size(); column++) {
                reducedMatrix[row][column] = matrix[oldIndexes.get(row)][oldIndexes.get(column)];
            }
        }

        for (int index = 0; index < oldIndexes.size(); index++) {
            int oldIndex = oldIndexes.get(index);
            double value = (matrix[oldIndex][bestI] + matrix[oldIndex][bestJ] - matrix[bestI][bestJ]) / 2.0;
            reducedMatrix[index][n - 2] = value;
            reducedMatrix[n - 2][index] = value;
        }
        reducedMatrix[n - 2][n - 2] = 0.0;

        neighborJoin(reducedMatrix, reducedLabels, tree, nextNodeId);

        addBidirectionalEdge(tree, newLabel, labels.get(bestI), limbI);
        addBidirectionalEdge(tree, newLabel, labels.get(bestJ), limbJ);
    }

    private void addBidirectionalEdge(Map<Integer, List<Edge>> tree, int from, int to, double weight) {
        tree.computeIfAbsent(from, ignored -> new ArrayList<>()).add(new Edge(to, weight));
        tree.computeIfAbsent(to, ignored -> new ArrayList<>()).add(new Edge(from, weight));
    }

    private String formatEdge(int from, Edge edge) {
        return from + "->" + edge.to() + ":" + String.format(Locale.US, "%.3f", edge.weight());
    }

    private record Edge(int to, double weight) {
    }

    private static final class NextNodeId {
        private int next;

        private NextNodeId(int start) {
            this.next = start;
        }

        int next() {
            return next++;
        }
    }
}
