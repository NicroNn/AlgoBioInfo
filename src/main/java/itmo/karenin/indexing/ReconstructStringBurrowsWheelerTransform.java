package itmo.karenin.indexing;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReconstructStringBurrowsWheelerTransform extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Expected a transform string in the input.");
        }

        String transform = lines.get(0);
        List<LabeledSymbol> lastColumn = labelSymbols(transform);

        char[] firstColumnChars = transform.toCharArray();
        java.util.Arrays.sort(firstColumnChars);
        List<LabeledSymbol> firstColumn = labelSymbols(new String(firstColumnChars));

        Map<LabeledSymbol, Integer> firstColumnIndex = new HashMap<>();
        for (int index = 0; index < firstColumn.size(); index++) {
            firstColumnIndex.put(firstColumn.get(index), index);
        }

        int row = transform.indexOf('$');
        StringBuilder reversed = new StringBuilder();

        for (int step = 0; step < transform.length() - 1; step++) {
            row = firstColumnIndex.get(lastColumn.get(row));
            reversed.append(lastColumn.get(row).symbol());
        }

        return reversed.reverse().append('$').toString();
    }

    private List<LabeledSymbol> labelSymbols(String text) {
        Map<Character, Integer> counts = new HashMap<>();
        List<LabeledSymbol> labeled = new ArrayList<>();

        for (int index = 0; index < text.length(); index++) {
            char symbol = text.charAt(index);
            int occurrence = counts.getOrDefault(symbol, 0) + 1;
            counts.put(symbol, occurrence);
            labeled.add(new LabeledSymbol(symbol, occurrence));
        }

        return labeled;
    }

    private record LabeledSymbol(char symbol, int occurrence) {
    }
}
