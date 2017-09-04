package net.njcull.collections;

import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * An immutable entry list for an ArrayBackedMap, appearing in key order.
 *
 * @author run2000
 * @version 11/01/2016
 */
final class ArrayBackedMapEntryList<K,V> extends AbstractRandomAccessList<Map.Entry<K,V>> {
    private final ArrayBackedMap<K,V> m_Map;

    /**
     * Construct a new entry list for the given array-backed map.
     *
     * @param map the map
     * @throws NullPointerException supplied map is null
     */
    ArrayBackedMapEntryList(ArrayBackedMap<K, V> map) {
        this.m_Map = Objects.requireNonNull(map, "map must be non-null");
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the value to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    @Override
    public Map.Entry<K, V> get(int index) {
        return m_Map.entryAt(index);
    }

    /**
     * Returns the number of elements in this list. Delegates to the
     * underlying map.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return m_Map.size();
    }

    /**
     * Determine whether this list is empty. Delegates to the underlying map.
     *
     * @return {@code true} if this list contains no elements, otherwise
     * {@code false}
     */
    @Override
    public boolean isEmpty() {
        return m_Map.isEmpty();
    }

    /**
     * Creates a {@link Spliterator} over the elements in this list.
     *
     * @return a {@code Spliterator} over the elements in this list
     */
    @Override
    public Spliterator<Map.Entry<K, V>> spliterator() {
        return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.CONCURRENT);
    }
}
