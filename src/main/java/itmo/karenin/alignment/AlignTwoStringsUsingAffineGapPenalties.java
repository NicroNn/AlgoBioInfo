package itmo.karenin.alignment;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.List;

public class AlignTwoStringsUsingAffineGapPenalties extends AbstractBioinformaticsTask {
    private static final int GAP_OPENING_PENALTY = 11;
    private static final int GAP_EXTENSION_PENALTY = 1;
    private static final int NEGATIVE_INFINITY = Integer.MIN_VALUE / 4;

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

        int[][] lower = new int[n + 1][m + 1];
        int[][] middle = new int[n + 1][m + 1];
        int[][] upper = new int[n + 1][m + 1];

        MatrixState[][] lowerParent = new MatrixState[n + 1][m + 1];
        MatrixState[][] middleParent = new MatrixState[n + 1][m + 1];
        MatrixState[][] upperParent = new MatrixState[n + 1][m + 1];

        initializeMatrices(n, m, lower, middle, upper, lowerParent, middleParent, upperParent);

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int extendLower = lower[i - 1][j] - GAP_EXTENSION_PENALTY;
                int openLower = middle[i - 1][j] - GAP_OPENING_PENALTY;
                if (extendLower >= openLower) {
                    lower[i][j] = extendLower;
                    lowerParent[i][j] = MatrixState.LOWER;
                } else {
                    lower[i][j] = openLower;
                    lowerParent[i][j] = MatrixState.MIDDLE;
                }

                int extendUpper = upper[i][j - 1] - GAP_EXTENSION_PENALTY;
                int openUpper = middle[i][j - 1] - GAP_OPENING_PENALTY;
                if (extendUpper >= openUpper) {
                    upper[i][j] = extendUpper;
                    upperParent[i][j] = MatrixState.UPPER;
                } else {
                    upper[i][j] = openUpper;
                    upperParent[i][j] = MatrixState.MIDDLE;
                }

                int diagonal = middle[i - 1][j - 1] + Blosum62.score(first.charAt(i - 1), second.charAt(j - 1));
                int best = diagonal;
                MatrixState state = MatrixState.MIDDLE;

                if (lower[i][j] > best) {
                    best = lower[i][j];
                    state = MatrixState.LOWER;
                }

                if (upper[i][j] > best) {
                    best = upper[i][j];
                    state = MatrixState.UPPER;
                }

                middle[i][j] = best;
                middleParent[i][j] = state;
            }
        }

        StringBuilder alignedFirst = new StringBuilder();
        StringBuilder alignedSecond = new StringBuilder();

        int i = n;
        int j = m;
        MatrixState state = MatrixState.MIDDLE;

        while (i > 0 || j > 0) {
            if (state == MatrixState.MIDDLE) {
                MatrixState previousState = middleParent[i][j];
                if (previousState == MatrixState.MIDDLE) {
                    alignedFirst.append(first.charAt(i - 1));
                    alignedSecond.append(second.charAt(j - 1));
                    i--;
                    j--;
                }
                state = previousState;
            } else if (state == MatrixState.LOWER) {
                MatrixState previousState = lowerParent[i][j];
                alignedFirst.append(first.charAt(i - 1));
                alignedSecond.append('-');
                i--;
                state = previousState;
            } else {
                MatrixState previousState = upperParent[i][j];
                alignedFirst.append('-');
                alignedSecond.append(second.charAt(j - 1));
                j--;
                state = previousState;
            }
        }

        return middle[n][m] + "\n"
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

    private void initializeMatrices(
            int n,
            int m,
            int[][] lower,
            int[][] middle,
            int[][] upper,
            MatrixState[][] lowerParent,
            MatrixState[][] middleParent,
            MatrixState[][] upperParent
    ) {
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                lower[i][j] = NEGATIVE_INFINITY;
                middle[i][j] = NEGATIVE_INFINITY;
                upper[i][j] = NEGATIVE_INFINITY;
            }
        }

        middle[0][0] = 0;

        for (int i = 1; i <= n; i++) {
            lower[i][0] = -(GAP_OPENING_PENALTY + (i - 1) * GAP_EXTENSION_PENALTY);
            lowerParent[i][0] = i == 1 ? MatrixState.MIDDLE : MatrixState.LOWER;
            middle[i][0] = lower[i][0];
            middleParent[i][0] = MatrixState.LOWER;
        }

        for (int j = 1; j <= m; j++) {
            upper[0][j] = -(GAP_OPENING_PENALTY + (j - 1) * GAP_EXTENSION_PENALTY);
            upperParent[0][j] = j == 1 ? MatrixState.MIDDLE : MatrixState.UPPER;
            middle[0][j] = upper[0][j];
            middleParent[0][j] = MatrixState.UPPER;
        }
    }

    private enum MatrixState {
        LOWER,
        MIDDLE,
        UPPER
    }
}
