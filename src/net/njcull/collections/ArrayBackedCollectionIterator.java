package net.njcull.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An immutable iterator for an ArrayBackedCollection.
 *
 * @author run2000
 * @version 10/01/2016.
 */
final class ArrayBackedCollectionIterator<E> implements Iterator<E> {
    private final ArrayBackedCollection<E> m_Set;
    private final int m_EndIndex;
    private int m_Index;

    ArrayBackedCollectionIterator(ArrayBackedCollection<E> set) {
        m_Set = Objects.requireNonNull(set);
        m_EndIndex = set.size();
        m_Index = 0;
    }

    ArrayBackedCollectionIterator(ArrayBackedCollection<E> set, int startIndex, int endIndex) {
        m_Set = Objects.requireNonNull(set);
        m_EndIndex = endIndex;
        m_Index = startIndex;

        if ((startIndex < 0) || (startIndex >= set.size())) {
            throw new IllegalArgumentException("start index: " + startIndex);
        }
        if ((endIndex < startIndex) || (endIndex > set.size())) {
            throw new IllegalArgumentException("end index: " + endIndex);
        }
    }

    @Override
    public boolean hasNext() {
        return m_Index < m_EndIndex;
    }

    @Override
    public E next() {
        if ((m_Index < 0) || (m_Index >= m_EndIndex)) {
            throw new NoSuchElementException("end of iterator");
        }
        return m_Set.getAtIndex(m_Index++);
    }
}
