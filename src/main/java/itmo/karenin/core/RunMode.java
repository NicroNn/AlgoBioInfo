package itmo.karenin.core;

public enum RunMode {
    SAMPLE("sample"),
    ACTUAL("actual");

    private final String cliValue;

    RunMode(String cliValue) {
        this.cliValue = cliValue;
    }

    public String cliValue() {
        return cliValue;
    }

    public static RunMode parse(String value) {
        for (RunMode mode : values()) {
            if (mode.cliValue.equalsIgnoreCase(value)) {
                return mode;
            }
        }

        throw new IllegalArgumentException("Unknown run mode: " + value);
    }
}
