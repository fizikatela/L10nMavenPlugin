package sidorovoleg.mojo.locale;

/**
 * Класс элемента словаря
 * @author fizikatela
 * Date: 28.01.2018
 */
public class DictionaryItem {

    /** Строка */
    private String line;
    /** Перевод */
    private String translation;
    /** Признак использования при локализации */
    private boolean isUsed;

    /**
     * Конструктор
     * @param pair пара [строка - перевод]
     */
    public DictionaryItem(String[] pair) {
        this(pair[0], pair[1]);
    }

    /**
     * Конструктор
     * @param line        строка
     * @param translation перевод
     */
    public DictionaryItem(String line, String translation) {
        this.line = line;
        this.translation = translation;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}
