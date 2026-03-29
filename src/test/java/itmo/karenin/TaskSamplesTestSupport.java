package itmo.karenin;

import itmo.karenin.core.AbstractBioinformaticsTask;
import itmo.karenin.core.RunResult;
import itmo.karenin.core.TaskName;
import itmo.karenin.core.TaskRegistry;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TaskSamplesTestSupport {
    private static final Pattern SAMPLE_INDEX_PATTERN = Pattern.compile("input_(\\d+)\\.txt");

    private TaskSamplesTestSupport() {
    }

    public static void assertAllSamples(TaskName taskName) {
        AbstractBioinformaticsTask task = TaskRegistry.create(taskName);
        List<Integer> sampleIndexes = findSampleIndexes(task);

        Assertions.assertFalse(
                sampleIndexes.isEmpty(),
                () -> "No sample inputs found for task " + taskName.className()
        );

        for (int sampleIndex : sampleIndexes) {
            RunResult result = task.runSample(sampleIndex);
            Assertions.assertNotNull(
                    result.expectedOutput(),
                    () -> "Missing expected output for " + taskName.className() + " sample #" + sampleIndex
            );
            Assertions.assertTrue(
                    result.matchesExpected(),
                    () -> buildFailureMessage(taskName, sampleIndex, result)
            );
        }
    }

    private static List<Integer> findSampleIndexes(AbstractBioinformaticsTask task) {
        Path inputsDirectory = Path.of("src", "main", "resources", task.datasetRoot(), "inputs");
        if (!Files.exists(inputsDirectory)) {
            return List.of();
        }

        try (var files = Files.list(inputsDirectory)) {
            return files
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .map(SAMPLE_INDEX_PATTERN::matcher)
                    .filter(Matcher::matches)
                    .map(matcher -> Integer.parseInt(matcher.group(1)))
                    .sorted(Comparator.naturalOrder())
                    .toList();
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to list sample inputs for " + task.getClass().getSimpleName(), exception);
        }
    }

    private static String buildFailureMessage(TaskName taskName, int sampleIndex, RunResult result) {
        return """
                Sample failed for %s #%d
                Result:
                %s

                Expected:
                %s
                """.formatted(taskName.className(), sampleIndex, result.actualOutput(), result.expectedOutput());
    }
}
