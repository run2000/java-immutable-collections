package net.njcull.collections;

import java.util.Map;

/**
 * <p>A {@code Map} implemented by a backing array, with {@link ArrayBackedSet}
 * and {@link ArrayBackedCollection} views over the data.</p>
 * {@inheritDoc}
 *
 * @author run2000
 * @version 11/01/2016.
 */
public interface ArrayBackedMap<K, V> extends Map<K, V> {

    ArrayBackedSet<Entry<K, V>> entrySet();

    ArrayBackedSet<K> keySet();

    ArrayBackedCollection<V> values();

    Entry<K, V> entryAt(int index);

    K keyAt(int index);

    V valueAt(int index);

    int indexOfKey(Object key);

    int indexOfValue(Object value);

}
