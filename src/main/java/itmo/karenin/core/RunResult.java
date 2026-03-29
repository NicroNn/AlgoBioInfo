package itmo.karenin.core;

public record RunResult(String actualOutput, String expectedOutput, boolean matchesExpected) {
}
