package sidorovoleg.mojo.locale;

import org.apache.commons.lang3.StringUtils;

import java.text.CharacterIterator;
import java.util.Objects;

/**
 * Created by fizikatela
 * Email: aka.hunter@gmail.com
 * 10.02.2018.
 */
public class LocalizeCharacterIterator implements CharacterIterator {

    private String text;
    private int begin;
    private int end;
    private int pos;

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

    public boolean equalsPrevious(char c) {
        if (pos > 0) {
            return text.charAt(pos - 1) == c;
        }
        return false;
    }

    public void overlayText(String overlay, int sIdx, int eIdx) {
        text = StringUtils.overlay(text, overlay, sIdx, eIdx);
        pos = sIdx + overlay.length();
        end = text.length();
    }

    public String getText() {
        return text;
    }
}
