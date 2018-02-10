package sidorovoleg.mojo.locale;

import java.io.IOException;
import java.nio.file.Path;
import java.text.CharacterIterator;

/**
 * Created by fizikatela
 * Email: aka.hunter@gmail.com
 * 28.01.2018.
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
        boolean isBackquote = false;
        int beginIdx = -1;
        for (char c = iterator.first(); c != CharacterIterator.DONE; c = iterator.next()) {
            switch (c) {
                case '`' :
                    if (beginIdx == -1) {
                        isBackquote = true;
                        beginIdx = iterator.getIndex();
                    } else  {
                        translations(locale, iterator, beginIdx + 1);
                        beginIdx = -1;
                        isBackquote = false;
                    }
                    break;
                case '"':
                    if (!isBackquote && !iterator.equalsPrevious('\\')) {
                        if (beginIdx == -1) {
                            beginIdx = iterator.getIndex();
                        } else  {
                            translations(locale, iterator, beginIdx + 1);
                            beginIdx = -1;
                        }
                    }
                    break;
            }
        }
        return iterator.getText();
    }

    private void translations(String locale, LocalizeCharacterIterator iterator, int beginIdx) {
        String translation;
        int endIdx = iterator.getIndex();
        translation = dictionary.getTranslations(iterator.getText().substring(beginIdx, endIdx), locale);
        if (translation != null) {
            iterator.overlayText(translation, beginIdx, endIdx);
        }
    }
}
