package code.dto;

import java.util.ArrayList;
import java.util.List;

public record TestCases(List<TestCase> testCases) {

    @Override
    public List<TestCase> testCases() {
        return new ArrayList<>(testCases);
    }
}
