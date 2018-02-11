package sidorovoleg.mojo.locale;

import java.io.IOException;
import java.nio.file.Path;
import java.text.CharacterIterator;

/**
 * Локализатор скриптов
 * @author fizikatela
 * Date: 28.01.2018
 */
public class ScriptLocalizer extends AbstractLocalizer {

    /**
     * Конструктор
     * @param dictPath путь до словаря
     * @throws IOException при ошибках ввода / вывода
     */
    public ScriptLocalizer(Path dictPath) throws IOException {
        super(dictPath);
    }

    @Override
    protected String parseString(LocalizeCharacterIterator iterator, String locale) {
        for (char c = iterator.first(); c != CharacterIterator.DONE; c = iterator.next()) {
            int beginIdx, endIdx;
            switch (c) {
                case '`' :
                    beginIdx = iterator.getIndex() + 1;
                    endIdx = iterator.findNext(c);
                    translatesString(iterator, locale, beginIdx, endIdx == -1 ? iterator.getEndIndex() : endIdx);
                    break;
                case '"':
                    if (!iterator.equalsPrevious('\\')) {
                        beginIdx = iterator.getIndex() + 1;
                        endIdx = iterator.findNextWithoutShield(c, '\\');
                        translatesString(iterator, locale, beginIdx, endIdx == -1 ? iterator.getEndIndex() : endIdx);
                    }
                    break;
            }
        }
        return iterator.getText();
    }
}
