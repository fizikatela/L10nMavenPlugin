package sidorovoleg.mojo.locale;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Абстрактный класс локализатора
 * @author fizikatela
 * Date: 11.02.2018
 */
public abstract class AbstractLocalizer {

    /** Словарь */
    private Dictionary dictionary;

    /**
     * Конструктор
     * @param dictPath пусть до словаря
     * @throws IOException при ошибках ввода / вывода
     */
    public AbstractLocalizer(Path dictPath) throws IOException {
        dictionary = new Dictionary(dictPath);
    }

    /**
     * Разбирает и локализирует строку по символьно
     * @param iterator итератор по символам строки
     * @param locale   локаль
     * @return локализированную строку
     */
    protected abstract String parseString(LocalizeCharacterIterator iterator, String locale);

    /**
     * Локализирует строку
     * @param line   строка
     * @param locale локаль
     * @return локализированную строку
     */
    public String localizeString(String line, String locale) {
        String translation = dictionary.getTranslations(line, locale);
        if (translation != null) {
            return translation;
        }
        return parseString(new LocalizeCharacterIterator(line), locale);
    }

    /**
     * Переводит часть строки
     * @param iterator итератор по символам строки
     * @param locale   локаль
     * @param beginIdx индекс начало части строки для локализации
     * @param endIdx   индекс конца части строки для локализации
     */
    protected void translatesString(LocalizeCharacterIterator iterator, String locale, int beginIdx, int endIdx) {
        String text = iterator.getText();
        if (beginIdx >= endIdx || endIdx > text.length()) {
            return;
        }

        String translation = dictionary.getTranslations(text.substring(beginIdx, endIdx), locale);
        if (translation != null) {
            iterator.overlayText(translation, beginIdx, endIdx);
        }
    }
}