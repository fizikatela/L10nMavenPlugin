package sidorovoleg.mojo.locale;

/**
 * Created by fizikatela
 * Email: aka.hunter@gmail.com
 * 28.01.2018.
 */
public class DictionaryItem {

    private String key;
    private String value;
    private boolean isUsed;

    public DictionaryItem(String[] pair) {
        this(pair[0], pair[1]);
    }

    public DictionaryItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}
