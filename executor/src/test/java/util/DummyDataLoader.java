package util;

import java.util.Map;

public interface DummyDataLoader {
    Map<String, String> loadData();

    void cleanUp();
}
