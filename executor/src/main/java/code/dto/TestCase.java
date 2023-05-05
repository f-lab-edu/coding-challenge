package code.dto;

import code.exception.InvalidTestCaseException;

public record TestCase(
        String input,
        String output
) {

    public TestCase {
        if (output == null || output.isEmpty()) {
            throw InvalidTestCaseException.invalidTestCase();
        }
    }
}
