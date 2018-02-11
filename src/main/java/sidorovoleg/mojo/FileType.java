package sidorovoleg.mojo;

import java.nio.file.Path;
import java.text.MessageFormat;

/**
 * @author fizikatela
 * Date: 11.02.2018
 */
public enum FileType {
    TS(".ts"),
    JS(".js");

    private final String ext;

    FileType(String ext) {
        this.ext = ext;
    }

    public static FileType valueOf(Path file) {
        String ext = getExtFile(file);
        if (ext == null) {
            throw new IllegalArgumentException(MessageFormat.format("File '{0}' does not contain the extension", file.getFileName()));
        }
        for (FileType type : values()) {
            if (ext.equals(type.ext)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported file type: " + ext);
    }

    public static String getExtFile(Path file) {
        String fileName = file.getFileName().toString();
        int pointIdx = fileName.lastIndexOf('.');
        return pointIdx == -1 ? null : fileName.substring(pointIdx);
    }
}
