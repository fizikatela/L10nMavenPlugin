package sidorovoleg.mojo.locale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Класс словаря
 * @author fizikatela
 * Date: 28.01.2018
 */
public class Dictionary {

    /** Словарь */
    private Map<String, List<DictionaryItem>> dictonaty;

    /**
     * Конструктор
     * @param dictPath путь до словаря
     * @throws IOException при ошибка ввода / вывода
     */
    public Dictionary(Path dictPath) throws IOException {
        dictonaty = load(dictPath);
    }

    /**
     * Возвращает локализированную строку из словаря
     * @param line   строка для поиска в словаре
     * @param locale локаль
     * @return локализированную строку из словаря или {@code null}
     */
    public String getTranslations(String line, String locale) {
        List<DictionaryItem> dict = dictonaty.get(locale);
        if (dict == null) {
            return null;
        }

        Optional<DictionaryItem> result = dict.stream()
                .filter(di -> di.getLine().equals(line))
                .findFirst();
        return result.isPresent() ? result.get().getTranslation() : null;
    }

    /**
     * Загружает словарь
     * @param dictPath путь до словаря
     * @return загруженный словарь
     * @throws IOException при ошибках ввода / вывода
     */
    private Map<String, List<DictionaryItem>> load(Path dictPath) throws IOException {
        Map<String, List<DictionaryItem>> dict = new HashMap<>();
        String locale = null;
        int countLine = 0;
        for (String line : Files.readAllLines(dictPath)) {
            countLine++;
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith("locale")) {
                locale = line.split("=")[1];
                continue;
            }
            if (locale == null) {
                throw new IOException("Не корректный словарь, отсутствует локаль. Строка: " + countLine);
            }
            if (line.startsWith("\"")) {
                String[] pair = line.substring(1, line.length() - 1).split("\"=\"");
                dict.computeIfAbsent(locale, emptyList -> new ArrayList<>()).add(new DictionaryItem(pair));
                continue;
            }
            if (line.startsWith("`")) {
                String[] pair = line.substring(1, line.length() -1).split("`=`");
                dict.computeIfAbsent(locale, emptyList -> new ArrayList<>()).add(new DictionaryItem(pair));
                continue;
            }
            throw new IOException("Не корректная строка словаря. Строка: " + countLine);
        }

        return dict;
    }
}
