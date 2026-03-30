package itmo.karenin.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class AbstractBioinformaticsTask {
    private static final String BASE_PACKAGE = "itmo.karenin";
    private static final List<String> ACTUAL_DATASET_CANDIDATES = List.of(
            "dataset.txt",
            "actual.txt",
            "input.txt",
            "rosalind.txt"
    );

    protected abstract String solve(String input);

    public final RunResult runSample(int index) {
        String input = readRequiredResource(sampleInputPath(index));
        String actualOutput = normalizeOutput(solve(input));
        String expectedOutput = readOptionalResource(sampleOutputPath(index));
        if (expectedOutput != null) {
            expectedOutput = normalizeOutput(expectedOutput);
        }
        boolean matchesExpected = expectedOutput != null
                && normalizeForComparison(actualOutput, input).equals(normalizeForComparison(expectedOutput, input));
        return new RunResult(actualOutput, expectedOutput, matchesExpected);
    }

    public final String runActual() {
        String input = readActualDataset();
        return normalizeOutput(solve(input));
    }

    public final String datasetRoot() {
        String packageName = getClass().getPackageName();
        String packageSuffix = packageName.equals(BASE_PACKAGE)
                ? ""
                : packageName.substring((BASE_PACKAGE + ".").length()).replace('.', '/');

        if (packageSuffix.isEmpty()) {
            return "datasets/" + getClass().getSimpleName();
        }

        return "datasets/" + packageSuffix + "/" + getClass().getSimpleName();
    }

    protected final List<String> readNonBlankLines(String input) {
        return input.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
    }

    private String readActualDataset() {
        for (String candidate : ACTUAL_DATASET_CANDIDATES) {
            String resourcePath = datasetRoot() + "/" + candidate;
            String content = readOptionalResource(resourcePath);
            if (content != null) {
                return content;
            }
        }

        throw new IllegalArgumentException(
                "Actual dataset not found for " + getClass().getSimpleName()
                        + ". Put one of these files into " + datasetRoot() + ": "
                        + String.join(", ", ACTUAL_DATASET_CANDIDATES)
        );
    }

    private String sampleInputPath(int index) {
        return datasetRoot() + "/inputs/input_" + index + ".txt";
    }

    private String sampleOutputPath(int index) {
        return datasetRoot() + "/outputs/output_" + index + ".txt";
    }

    private String readRequiredResource(String path) {
        String content = readOptionalResource(path);
        if (content == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return content;
    }

    private String readOptionalResource(String path) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                return null;
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to read resource: " + path, exception);
        }
    }

    private String normalizeOutput(String output) {
        return output.replace("\r\n", "\n").trim();
    }

    protected String normalizeForComparison(String output) {
        return normalizeOutput(output);
    }

    protected String normalizeForComparison(String output, String input) {
        return normalizeForComparison(output);
    }
}
