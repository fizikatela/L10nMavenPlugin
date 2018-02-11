package sidorovoleg.mojo.locale;

import sidorovoleg.mojo.FileType;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author fizikatela
 * Date: 11.02.2018
 */
public final class LocalizerFactory {

    private LocalizerFactory() {}

    public static AbstractLocalizer getLocalizer(Path file, Path dict) throws IOException {
        FileType type = FileType.valueOf(file);
        switch (type) {
            case JS:
            case TS:
                return new ScriptLocalizer(dict);
            default:
                throw new IllegalArgumentException("There is no localizer for the file type" + type);
        }
    }
}