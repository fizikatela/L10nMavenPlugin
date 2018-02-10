package sidorovoleg.mojo.locale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by fizikatela
 * Email: aka.hunter@gmail.com
 * 28.01.2018.
 */
public class Dictionary {

    private Map<String, List<DictionaryItem>> dictonaty;

    public Dictionary(Path dictPath) throws IOException {
        dictonaty = load(dictPath);
    }

    public String getTranslations(String key, String locale) {
        List<DictionaryItem> dict = dictonaty.get(locale);
        if (dict == null) {
            return null;
        }

        Optional<DictionaryItem> result = dict.stream()
                .filter(di -> di.getKey().equals(key))
                .findFirst();
        return result.isPresent() ? result.get().getValue() : null;
    }

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
