package sidorovoleg.mojo.locale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс словаря
 * @author fizikatela
 * Date: 28.01.2018
 */
public class Dictionary {

    /** Словарь */
    private final Map<String, List<DictionaryItem>> dictonaty;
    /** Имя файла со словарем */
    private final String fileName;

    /**
     * Конструктор
     * @param dictPath путь до словаря
     * @throws IOException при ошибка ввода / вывода
     */
    public Dictionary(Path dictPath) throws IOException {
        fileName = dictPath.getFileName().toString();
        dictonaty = load(dictPath);
        checkDuplicates();
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

        DictionaryItem item = dict.stream()
                .filter(di -> line.equals(di.getLine()))
                .findFirst().orElse(null);
        if (item == null) {
            return null;
        }

        item.setUsed(true);
        return item.getTranslation();
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
                throw new IllegalArgumentException("Not correct dictionary, there is no locale. Line: " + countLine);
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
            throw new IllegalArgumentException("Incorrect dictionary string. The line must start with ` or \". Line:" + countLine);
        }
        return dict;
    }

    /**
     * Проверяет словарь на дубликаты
     */
    private void checkDuplicates() {
        Map<String, Set<String>> duplicates = dictonaty.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey(),
                        entry -> entry.getValue().stream()
                                .filter(item -> Collections.frequency(entry.getValue(), item) > 1)
                                .map(item -> item.getLine()).collect(Collectors.toSet())));
        boolean isError = duplicates.values().stream().anyMatch(value -> value.size() > 0);
        if (isError) {
            throw new IllegalArgumentException("Found duplicates in dictionary " + fileName + getDetailErrorMsg(duplicates));
        }
    }

    /**
     * Возвращает детальное сообщение об ошибке
     * @param duplicates карта с дубликатами словаря
     * @return детальное сообщение об ошибке
     */
    private String getDetailErrorMsg(Map<String, Set<String>> duplicates) {
        StringBuilder sb = new StringBuilder("\n");
        duplicates.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 0)
                .forEach(entry -> {
                    sb.append("Locale: ").append(entry.getKey()).append("\n");
                    entry.getValue().stream().forEach(line -> sb.append(line).append("\n"));
                });
        return sb.toString();
    }
}
