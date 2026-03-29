package itmo.karenin.alignment;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.List;

public class FindHighestScoringFittingAlignment extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected two strings in the input.");
        }

        String longer = lines.get(0);
        String shorter = lines.get(1);

        int n = longer.length();
        int m = shorter.length();

        int[][] score = new int[n + 1][m + 1];
        Direction[][] parent = new Direction[n + 1][m + 1];

        for (int j = 1; j <= m; j++) {
            score[0][j] = -j;
            parent[0][j] = Direction.LEFT;
        }

        for (int i = 1; i <= n; i++) {
            score[i][0] = 0;
            parent[i][0] = Direction.UP;
        }

        int bestScore = Integer.MIN_VALUE;
        int bestRow = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int matchOrMismatch = score[i - 1][j - 1]
                        + (longer.charAt(i - 1) == shorter.charAt(j - 1) ? 1 : -1);
                int delete = score[i - 1][j] - 1;
                int insert = score[i][j - 1] - 1;

                int bestCellScore = matchOrMismatch;
                Direction bestDirection = Direction.DIAGONAL;

                if (delete > bestCellScore) {
                    bestCellScore = delete;
                    bestDirection = Direction.UP;
                }

                if (insert > bestCellScore) {
                    bestCellScore = insert;
                    bestDirection = Direction.LEFT;
                }

                score[i][j] = bestCellScore;
                parent[i][j] = bestDirection;
            }

            if (score[i][m] > bestScore) {
                bestScore = score[i][m];
                bestRow = i;
            }
        }

        StringBuilder alignedLonger = new StringBuilder();
        StringBuilder alignedShorter = new StringBuilder();

        int i = bestRow;
        int j = m;
        while (j > 0) {
            Direction direction = parent[i][j];
            if (direction == Direction.DIAGONAL) {
                alignedLonger.append(longer.charAt(i - 1));
                alignedShorter.append(shorter.charAt(j - 1));
                i--;
                j--;
            } else if (direction == Direction.UP) {
                alignedLonger.append(longer.charAt(i - 1));
                alignedShorter.append('-');
                i--;
            } else {
                alignedLonger.append('-');
                alignedShorter.append(shorter.charAt(j - 1));
                j--;
            }
        }

        return bestScore + "\n"
                + alignedLonger.reverse() + "\n"
                + alignedShorter.reverse();
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
