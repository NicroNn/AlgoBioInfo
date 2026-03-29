package itmo.karenin.alignment;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.List;

public class FindHighestScoringGlobalAlignmentTwoStrings extends AbstractBioinformaticsTask {
    private static final int GAP_PENALTY = 5;

    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected two amino acid strings in the input.");
        }

        String first = lines.get(lines.size() - 2);
        String second = lines.get(lines.size() - 1);

        int n = first.length();
        int m = second.length();

        int[][] score = new int[n + 1][m + 1];
        Direction[][] parent = new Direction[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            score[i][0] = score[i - 1][0] - GAP_PENALTY;
            parent[i][0] = Direction.UP;
        }

        for (int j = 1; j <= m; j++) {
            score[0][j] = score[0][j - 1] - GAP_PENALTY;
            parent[0][j] = Direction.LEFT;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int diagonal = score[i - 1][j - 1] + Blosum62.score(first.charAt(i - 1), second.charAt(j - 1));
                int up = score[i - 1][j] - GAP_PENALTY;
                int left = score[i][j - 1] - GAP_PENALTY;

                int best = diagonal;
                Direction direction = Direction.DIAGONAL;

                if (up > best) {
                    best = up;
                    direction = Direction.UP;
                }

                if (left > best) {
                    best = left;
                    direction = Direction.LEFT;
                }

                score[i][j] = best;
                parent[i][j] = direction;
            }
        }

        StringBuilder alignedFirst = new StringBuilder();
        StringBuilder alignedSecond = new StringBuilder();

        int i = n;
        int j = m;
        while (i > 0 || j > 0) {
            Direction direction = parent[i][j];
            if (direction == Direction.DIAGONAL) {
                alignedFirst.append(first.charAt(i - 1));
                alignedSecond.append(second.charAt(j - 1));
                i--;
                j--;
            } else if (direction == Direction.UP) {
                alignedFirst.append(first.charAt(i - 1));
                alignedSecond.append('-');
                i--;
            } else {
                alignedFirst.append('-');
                alignedSecond.append(second.charAt(j - 1));
                j--;
            }
        }

        return score[n][m] + "\n"
                + alignedFirst.reverse() + "\n"
                + alignedSecond.reverse();
    }

    @Override
    protected String normalizeForComparison(String output) {
        List<String> lines = output.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
        return lines.isEmpty() ? "" : lines.get(0);
    }

    private enum Direction {
        DIAGONAL,
        UP,
        LEFT
    }
}
