package code.dto;

public class TestResult {
    private final Boolean isSucceeded;

    TestResult(Boolean isSucceeded) {
        this.isSucceeded = isSucceeded;
    }

    public Boolean isSucceeded() {
        return isSucceeded;
    }
}
