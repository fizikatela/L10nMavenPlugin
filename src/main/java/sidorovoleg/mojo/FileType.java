package sidorovoleg.mojo;

import java.nio.file.Path;
import java.text.MessageFormat;

/**
 * Перечисление типов файлов для локализации
 * @author fizikatela
 * Date: 11.02.2018
 */
public enum FileType {
    /** TypeScript */
    TS(".ts"),
    /** JavaScript */
    JS(".js");

    /** Расширение файла */
    private final String ext;

    /**
     * Конструктор
     * @param ext расширение файла
     */
    FileType(String ext) {
        this.ext = ext;
    }

    /**
     * Возвращает тип файла по его пути
     * @param file путь до файла
     * @return тип файла
     */
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
        throw new IllegalArgumentException("Unknown file type: " + ext);
    }

    /**
     * Возвращает расширение файла по его пути
     * @param file путь до файла
     * @return расширение файла или null
     */
    public static String getExtFile(Path file) {
        String fileName = file.getFileName().toString();
        int pointIdx = fileName.lastIndexOf('.');
        return pointIdx == -1 ? null : fileName.substring(pointIdx);
    }
}
