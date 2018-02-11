package sidorovoleg.mojo.locale;

import java.io.IOException;
import java.nio.file.Path;
import java.text.CharacterIterator;

/**
 * @author fizikatela
 * Date: 28.01.2018
 */
public class Localizer {

    private Dictionary dictionary;

    public Localizer(Path dictPath) throws IOException {
        dictionary = new Dictionary(dictPath);
    }

    public String localizeLine(String line, String locale) {
        String translation = dictionary.getTranslations(line, locale);
        if (translation != null) {
            return translation;
        }

        return parse(line, locale);
    }

    private String parse(String line, String locale) {
        LocalizeCharacterIterator iterator = new LocalizeCharacterIterator(line);
        for (char c = iterator.first(); c != CharacterIterator.DONE; c = iterator.next()) {
            int beginIdx, endIdx;
            switch (c) {
                case '`' :
                    beginIdx = iterator.getIndex();
                    endIdx = iterator.findNext(c);
                    if (endIdx != -1) {
                        translations(locale, iterator, beginIdx + 1, endIdx);
                    }
                    break;
                case '"':
                    if (!iterator.equalsPrevious('\\')) {
                        beginIdx = iterator.getIndex();
                        endIdx = iterator.findNext(c, '\\');
                        if (endIdx != -1) {
                            translations(locale, iterator, beginIdx + 1, endIdx);
                        }
                    }
                    break;
            }
        }
        return iterator.getText();
    }

    private void translations(String locale, LocalizeCharacterIterator iterator, int beginIdx, int endIdx) {
        String translation = dictionary.getTranslations(iterator.getText().substring(beginIdx, endIdx), locale);
        if (translation != null) {
            iterator.overlayText(translation, beginIdx, endIdx);
        }
    }
}
