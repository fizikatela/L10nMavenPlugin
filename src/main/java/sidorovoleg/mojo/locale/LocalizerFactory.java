package sidorovoleg.mojo.locale;

import sidorovoleg.mojo.FileType;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Фабрика для получения экземпляров классов локализаторов по типу файла
 * @author fizikatela
 * Date: 11.02.2018
 */
public final class LocalizerFactory {

    /**
     * Приватный конструктор
     */
    private LocalizerFactory() {}

    /**
     * Возвращает экземпляр класса локализатора по типу файла
     * @param type     тип файла
     * @param dictPath путь до словаря
     * @return экземпляр класса локализатора
     * @throws IOException при ошибках ввода / вывода
     */
    public static AbstractLocalizer getLocalizer(FileType type, Path dictPath) throws IOException {
        switch (type) {
            case JS:
            case TS:
                return new ScriptLocalizer(dictPath);
            default:
                throw new IllegalArgumentException("There is no localizer for the file type" + type);
        }
    }
}