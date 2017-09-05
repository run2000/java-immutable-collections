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

    /**
     * Returns an {@link ArrayBackedSet} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.
     *
     * @return an array-backed set view of the mappings contained in this map
     */
    ArrayBackedSet<Entry<K, V>> entrySet();

    /**
     * Returns an {@link ArrayBackedSet} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.
     *
     * @return an array-backed set view of the keys contained in this map
     */
    ArrayBackedSet<K> keySet();

    /**
     * Returns an {@link ArrayBackedCollection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.
     *
     * @return an array-backed collection view of the values contained in this map
     */
    ArrayBackedCollection<V> values();

    /**
     * Returns an Entry object for the map entry at the given array index.
     *
     * @param index the index in the array of the entry to be retrieved
     * @return an Entry object corresponding to the given array index
     * @throws IndexOutOfBoundsException if the index is less than zero or
     * index is greater than or equal to the map size
     */
    Entry<K, V> entryAt(int index);

    /**
     * Returns the key of the map entry at the given array index.
     *
     * @param index the index in the array of the key to be retrieved
     * @return the key at the given array index
     * @throws IndexOutOfBoundsException if the index is less than zero or
     * index is greater than or equal to the map size
     */
    K keyAt(int index);

    /**
     * Returns the value of the map entry at the given array index.
     *
     * @param index the index in the array of the value to be retrieved
     * @return the value at the given array index
     * @throws IndexOutOfBoundsException if the index is less than zero or
     * index is greater than or equal to the map size
     */
    V valueAt(int index);

    /**
     * Returns the array index of the given key in the array-backed map.
     *
     * @param key the key to be found in the map
     * @return a zero or positive integer if the key is in the
     * backing array, otherwise less than zero to indicate its absence
     */
    default int indexOfKey(Object key) {
        final int size = size();
        if(key == null) {
            for(int i = 0; i < size; i++) {
                if(key == keyAt(i)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (key.equals(keyAt(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Returns the array index of the given value in the array-backed map.
     * If there is more than one value, the index returned is not specified.
     *
     * @param value the value to be found in the map
     * @return a zero or positive integer if the value is in the
     * backing array, otherwise less than zero to indicate its absence
     */
    default int indexOfValue(Object value) {
        final int size = size();
        if(value == null) {
            for(int i = 0; i < size; i++) {
                if(value == valueAt(i)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (value.equals(valueAt(i))) {
                    return i;
                }
            }
        }
        return -1;
    }
}
