package sidorovoleg.mojo.locale;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author fizikatela
 * Date: 11.02.2018
 */
public abstract class AbstractLocalizer {

    private Dictionary dictionary;

    public AbstractLocalizer(Path dictPath) throws IOException {
        dictionary = new Dictionary(dictPath);
    }

    protected abstract String parseLine(LocalizeCharacterIterator iterator, String locale);

    public String localize(String line, String locale) {
        String translation = dictionary.getTranslations(line, locale);
        if (translation != null) {
            return translation;
        }
        return parseLine(new LocalizeCharacterIterator(line), locale);
    }

    protected void translations(String locale, LocalizeCharacterIterator iterator, int beginIdx, int endIdx) {
        String translation = dictionary.getTranslations(iterator.getText().substring(beginIdx, endIdx), locale);
        if (translation != null) {
            iterator.overlayText(translation, beginIdx, endIdx);
        }
    }
}