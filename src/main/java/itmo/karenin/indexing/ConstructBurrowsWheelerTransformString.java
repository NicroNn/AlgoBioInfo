package itmo.karenin.indexing;

import itmo.karenin.core.AbstractBioinformaticsTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConstructBurrowsWheelerTransformString extends AbstractBioinformaticsTask {
    @Override
    protected String solve(String input) {
        List<String> lines = readNonBlankLines(input);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Expected a string in the input.");
        }

        String text = lines.get(0);
        List<String> rotations = new ArrayList<>();

        for (int start = 0; start < text.length(); start++) {
            rotations.add(text.substring(start) + text.substring(0, start));
        }

        Collections.sort(rotations);

        StringBuilder bwt = new StringBuilder();
        for (String rotation : rotations) {
            bwt.append(rotation.charAt(rotation.length() - 1));
        }

        return bwt.toString();
    }
}
