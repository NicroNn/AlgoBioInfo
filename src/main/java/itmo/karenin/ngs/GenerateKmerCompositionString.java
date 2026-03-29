package itmo.karenin.ngs;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateKmerCompositionString extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Expected k and a string in the input.");
        }

        int k = Integer.parseInt(lines.get(0));
        String text = lines.get(1);

        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive.");
        }

        if (k > text.length()) {
            return "";
        }

        List<String> kmers = new ArrayList<>();
        for (int start = 0; start <= text.length() - k; start++) {
            kmers.add(text.substring(start, start + k));
        }

        Collections.sort(kmers);
        return String.join("\n", kmers);
    }
}
