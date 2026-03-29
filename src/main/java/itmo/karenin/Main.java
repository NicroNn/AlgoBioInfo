package itmo.karenin;

import itmo.karenin.core.AbstractBioinformaticsTask;
import itmo.karenin.core.RunResult;
import itmo.karenin.core.RunMode;
import itmo.karenin.core.TaskName;
import itmo.karenin.core.TaskRegistry;

public class Main {
    private static final TaskName DEFAULT_TASK = TaskName.PATTERN_MATCHING_BURROWS_WHEELER_TRANSFORM;
    private static final RunMode DEFAULT_MODE = RunMode.ACTUAL;
    private static final int DEFAULT_SAMPLE_INDEX = 2;

    public static void main(String[] args) {
        if (args.length == 0) {
            runDefaultTask();
            return;
        }

        if (args.length < 2) {
            printUsage();
            return;
        }

        TaskName taskName = TaskName.parse(args[0]);
        RunMode mode = RunMode.parse(args[1]);

        AbstractBioinformaticsTask task = TaskRegistry.create(taskName);

        switch (mode) {
            case SAMPLE -> runSample(task, args);
            case ACTUAL -> runActual(task);
        }
    }

    private static void runDefaultTask() {
        AbstractBioinformaticsTask task = TaskRegistry.create(DEFAULT_TASK);
        String[] defaultArgs = DEFAULT_MODE == RunMode.SAMPLE
                ? new String[]{DEFAULT_TASK.className(), DEFAULT_MODE.cliValue(), Integer.toString(DEFAULT_SAMPLE_INDEX)}
                : new String[]{DEFAULT_TASK.className(), DEFAULT_MODE.cliValue()};

        System.out.printf("Default run: %s %s", DEFAULT_TASK.className(), DEFAULT_MODE.cliValue());
        if (DEFAULT_MODE == RunMode.SAMPLE) {
            System.out.printf(" %d", DEFAULT_SAMPLE_INDEX);
        }
        System.out.println();
        System.out.println();

        switch (DEFAULT_MODE) {
            case SAMPLE -> runSample(task, defaultArgs);
            case ACTUAL -> runActual(task);
        }
    }

    private static void runSample(AbstractBioinformaticsTask task, String[] args) {
        int sampleIndex = args.length >= 3 ? Integer.parseInt(args[2]) : 1;
        RunResult result = task.runSample(sampleIndex);

        System.out.printf("Task: %s%n", task.getClass().getSimpleName());
        System.out.printf("Mode: sample #%d%n%n", sampleIndex);
        System.out.println("Result:");
        System.out.println(result.actualOutput());
        System.out.println();

        if (result.expectedOutput() != null) {
            System.out.println("Expected:");
            System.out.println(result.expectedOutput());
            System.out.println();
            System.out.printf("Status: %s%n", result.matchesExpected() ? "OK" : "MISMATCH");
        }
    }

    private static void runActual(AbstractBioinformaticsTask task) {
        String result = task.runActual();

        System.out.printf("Task: %s%n", task.getClass().getSimpleName());
        System.out.println("Mode: actual");
        System.out.println();
        System.out.println("Result:");
        System.out.println(result);
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  gradlew run");
        System.out.println("  gradlew run --args=\"<TaskName> sample [index]\"");
        System.out.println("  gradlew run --args=\"<TaskName> actual\"");
        System.out.println();
        System.out.println("Default run settings:");
        System.out.printf("  task=%s, mode=%s", DEFAULT_TASK.className(), DEFAULT_MODE.cliValue());
        if (DEFAULT_MODE == RunMode.SAMPLE) {
            System.out.printf(", sample=%d", DEFAULT_SAMPLE_INDEX);
        }
        System.out.println();
        System.out.println();
        System.out.println("Actual dataset file names supported:");
        System.out.println("  dataset.txt, actual.txt, input.txt, rosalind.txt");
        System.out.println();
        System.out.println("Known tasks:");
        for (TaskName taskName : TaskName.values()) {
            System.out.printf("  %s%n", taskName.className());
        }
    }
}
