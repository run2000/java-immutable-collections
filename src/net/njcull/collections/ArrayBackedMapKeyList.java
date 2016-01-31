package net.njcull.collections;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * An immutable key list for an ArrayBackedMap, appearing in key order.
 *
 * @author run2000
 * @version 11/01/2016.
 */
final class ArrayBackedMapKeyList<E> extends AbstractRandomAccessList<E> {
    private final ArrayBackedMap<E, ?> m_Map;

    public ArrayBackedMapKeyList(ArrayBackedMap<E, ?> map) {
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
        return m_Map.keyAt(index);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.CONCURRENT);
    }
}
