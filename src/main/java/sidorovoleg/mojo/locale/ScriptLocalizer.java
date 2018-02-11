package sidorovoleg.mojo.locale;

import java.io.IOException;
import java.nio.file.Path;
import java.text.CharacterIterator;

/**
 * @author fizikatela
 * Date: 28.01.2018
 */
public class ScriptLocalizer extends AbstractLocalizer {

    public ScriptLocalizer(Path dictPath) throws IOException {
        super(dictPath);
    }

    @Override
    protected String parseLine(LocalizeCharacterIterator iterator, String locale) {
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
}
