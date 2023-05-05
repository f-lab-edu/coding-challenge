package code.dto;

import java.util.Collections;
import java.util.List;

import code.exception.InvalidTestCasesException;

public record TestCases(List<TestCase> testCases) {
    public TestCases {
        if (testCases == null || testCases.isEmpty()) {
            throw InvalidTestCasesException.invalidTestCases();
        }
    }

    @Override
    public List<TestCase> testCases() {
        return Collections.unmodifiableList(testCases);
    }
}
