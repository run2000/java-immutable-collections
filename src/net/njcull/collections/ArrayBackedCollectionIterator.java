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
    private final ArrayBackedCollection<E> m_Coll;
    private final int m_EndIndex;
    private int m_Index;

    /**
     * Create a new {@code Iterator} for the supplied array-backed collection.
     *
     * @param coll the array-backed set on which the iterator indexes
     */
    ArrayBackedCollectionIterator(ArrayBackedCollection<E> coll) {
        m_Coll = Objects.requireNonNull(coll);
        m_EndIndex = coll.size();
        m_Index = 0;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     *
     * @return {@code true} if the iteration has more elements, otherwise
     * {@code false}
     */
    @Override
    public boolean hasNext() {
        return m_Index < m_EndIndex;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public E next() {
        if ((m_Index < 0) || (m_Index >= m_EndIndex)) {
            throw new NoSuchElementException("end of iterator");
        }
        return m_Coll.getAtIndex(m_Index++);
    }
}
