package sidorovoleg.mojo.locale;

import org.apache.commons.lang3.StringUtils;

import java.text.CharacterIterator;
import java.util.Objects;

/**
 * Итератор по символам строки
 * Базовый класс {@link java.text.StringCharacterIterator}
 * @author fizikatela
 * Date: 10.02.2018
 */
public class LocalizeCharacterIterator implements CharacterIterator {

    /** Строка итерирования */
    private String text;
    /** Индекс начало итерирования */
    private int begin;
    /** Индекс конца итерирования */
    private int end;
    /** Текущая позиция итератора */
    private int pos;

    /**
     * Конструктор
     * @param text строка итерирования
     */
    public LocalizeCharacterIterator(String text) {
        this.text = text;
        this.begin = 0;
        this.end = text.length();
        this.pos = 0;
    }

    @Override
    public char first() {
        pos = begin;
        return current();
    }

    @Override
    public char last() {
        if (end != begin) {
            pos = end - 1;
        } else {
            pos = end;
        }
        return current();
    }

    @Override
    public char setIndex(int p) {
        if (p < begin || p > end)
            throw new IllegalArgumentException("Invalid index");
        pos = p;
        return current();
    }

    @Override
    public char current() {
        if (pos >= begin && pos < end) {
            return text.charAt(pos);
        } else {
            return DONE;
        }
    }

    @Override
    public char next() {
        if (pos < end - 1) {
            pos++;
            return text.charAt(pos);
        } else {
            pos = end;
            return DONE;
        }
    }

    @Override
    public char previous() {
        if (pos > begin) {
            pos--;
            return text.charAt(pos);
        } else {
            return DONE;
        }
    }

    @Override
    public int getBeginIndex() {
        return begin;
    }

    @Override
    public int getEndIndex() {
        return end;
    }

    @Override
    public int getIndex() {
        return pos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalizeCharacterIterator that = (LocalizeCharacterIterator) o;
        return begin == that.begin &&
                end == that.end &&
                pos == that.pos &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, begin, end, pos);
    }

    @Override
    public Object clone() {
        try {
            LocalizeCharacterIterator other
                    = (LocalizeCharacterIterator) super.clone();
            return other;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    /**
     * Сравнивает предыдущий символ итератора с символом для сравнения
     * @param ch символ для сравнения
     * @return {@code true}, если символы совпадают, иначе {@code false}
     */
    public boolean equalsPrevious(char ch) {
        return equalsPrevious(ch, pos - 1);
    }

    /**
     * Сравнивает символ в строке итератора по индексу с символом для сравнени
     * @param ch  символ для сравнения
     * @param pos индекс символа в строке итератора
     * @return @return {@code true}, если символы совпадают, иначе {@code false}
     */
    public boolean equalsPrevious(char ch, int pos) {
        if (pos > 0 && pos < end) {
            return text.charAt(pos) == ch;
        }
        return false;
    }

    /**
     * Заменяет текст в строке итератора по заданным идексам и обновляет позицию итератора
     * @param overlay текст для замены
     * @param sIdx    индекс начала заменяемого текста
     * @param eIdx    индекс конца заменяемого текста
     */
    public void overlayText(String overlay, int sIdx, int eIdx) {
        text = StringUtils.overlay(text, overlay, sIdx, eIdx);
        pos = sIdx + overlay.length();
        end = text.length();
    }

    /**
     * Возвращает индекс следующего символа
     * @param ch символ для поиска
     * @return индекс следующего символа или {@code -1}
     */
    public int findNext(char ch) {
        return findNext(ch, pos + 1);
    }

    /**
     * Возвращает индекс следующего символа
     * @param ch  символ для поиска
     * @param idx индекс на для начала поиска
     * @return индекс символа или {@code -1}
     */
    public int findNext(char ch, int idx) {
        return text.indexOf(ch, idx);
    }

    /**
     * Возвращает индекс следующего символа без экранирования
     * @param ch          символ для поиска
     * @param chShielding символ экранирования
     * @return индекс символа или {@code -1}
     */
    public int findNextWithoutShield(char ch, char chShielding) {
        int findIdx = pos;
        do {
            findIdx++;
            findIdx = findNext(ch, findIdx);
        } while (findIdx != -1 && equalsPrevious(chShielding, findIdx - 1));
        return  findIdx;
    }

    /**
     * Возвращает строку итерирования
     * @return строку итерирования
     */
    public String getText() {
        return text;
    }
}
