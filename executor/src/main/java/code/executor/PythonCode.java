package code.executor;

import java.nio.file.Path;

public final class PythonCode extends AbstractCode {

    public PythonCode(String uuid, String code) {
        super(uuid, code);
    }

    @Override
    protected String getCommand(Path path) {
        return String.format("python3 %s", path.toAbsolutePath());
    }

    @Override
    protected String getFilename(String uuid) {
        return uuid + ".py";
    }
}
