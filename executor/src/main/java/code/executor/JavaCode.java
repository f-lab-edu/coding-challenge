package code.executor;

import java.nio.file.Path;

public final class JavaCode extends AbstractCode {
    public JavaCode(String uuid, String code) {
        super(uuid, code);
    }

    @Override
    protected String getCommand(Path path) {
        return String.format("java %s", path.toAbsolutePath());
    }

    @Override
    protected String getFilename(String uuid) {
        return uuid + ".java";
    }
}
