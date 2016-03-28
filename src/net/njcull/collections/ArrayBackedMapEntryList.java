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

    ArrayBackedMapEntryList(ArrayBackedMap<K, V> map) {
        this.m_Map = Objects.requireNonNull(map, "map must be non-null");
    }

    @Override
    public Map.Entry<K, V> get(int index) {
        return m_Map.entryAt(index);
    }

    @Override
    public int size() {
        return m_Map.size();
    }

    @Override
    public boolean isEmpty() {
        return m_Map.isEmpty();
    }

    @Override
    public Spliterator<Map.Entry<K, V>> spliterator() {
        return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.CONCURRENT);
    }
}
