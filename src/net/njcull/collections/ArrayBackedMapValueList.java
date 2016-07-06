package net.njcull.collections;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * An immutable value list for an ArrayBackedMap. Values are not assumed
 * to be distinct.
 *
 * @author run2000
 * @version 11/01/2016.
 */
final class ArrayBackedMapValueList<E> extends AbstractRandomAccessList<E> {
    private final ArrayBackedMap<?, E> m_Map;

    ArrayBackedMapValueList(ArrayBackedMap<?, E> map) {
        this.m_Map = Objects.requireNonNull(map, "map must be non-null");
    }

    @Override
    public boolean isEmpty() {
        return m_Map.isEmpty();
    }

    @Override
    public int size() {
        return m_Map.size();
    }

    @Override
    public E get(int index) {
        return m_Map.valueAt(index);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.CONCURRENT);
    }
}
