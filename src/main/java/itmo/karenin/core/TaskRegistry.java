package itmo.karenin.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class TaskRegistry {
    private static final List<String> PACKAGE_PREFIXES = List.of(
            "itmo.karenin.algorithms.strings.",
            "itmo.karenin.alignment.",
            "itmo.karenin.genome.",
            "itmo.karenin.indexing.",
            "itmo.karenin.ngs.",
            "itmo.karenin.phylogenetics.",
            "itmo.karenin.snp."
    );

    private TaskRegistry() {
    }

    public static AbstractBioinformaticsTask create(TaskName taskName) {
        String className = taskName.className();

        for (String packagePrefix : PACKAGE_PREFIXES) {
            try {
                Class<?> taskClass = Class.forName(packagePrefix + className);
                Object instance = taskClass.getDeclaredConstructor().newInstance();
                if (instance instanceof AbstractBioinformaticsTask task) {
                    return task;
                }
            } catch (ReflectiveOperationException ignored) {
            }
        }

        throw new IllegalArgumentException("Unknown task: " + taskName.className());
    }

    public static AbstractBioinformaticsTask create(String taskName) {
        return create(TaskName.parse(taskName));
    }

    public static List<String> availableTaskNames() {
        try (InputStream inputStream = TaskRegistry.class.getClassLoader().getResourceAsStream("names.txt")) {
            if (inputStream == null) {
                return List.of();
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
                    .lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .toList();
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to read names.txt", exception);
        }
    }
}
