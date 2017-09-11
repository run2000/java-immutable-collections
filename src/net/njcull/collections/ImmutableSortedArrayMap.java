package net.njcull.collections;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * A {@link SortedMap} backed by an array of elements. The array is the
 * exact length required to contain the keys and values. Both keys and values
 * are sorted.
 * <p>
 * Keys and values are stored together in sorted key order. In addition,
 * there is an index referring to the elements in sorted value order.
 * <p>
 * Keys and values are tested using a binary search implementation.
 * The map's keyset and entryset views may also be viewed as a {@link List}.
 * </p>
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author run2000
 * @version 7/01/2016.
 */
public final class ImmutableSortedArrayMap<K,V> extends AbstractMap<K,V> implements ArrayBackedMap<K,V>, SortedMap<K,V> {

    private final Object[] m_Map;
    private final Integer[] m_SortedValues;
    private final Comparator<? super K> m_KeyComparator;
    private final Comparator m_NullsKeyComparator;
    private final Comparator<? super V> m_ValueComparator;
    private final Comparator m_NullsValueComparator;
    private final boolean m_BiMap;

    private static final ImmutableSortedArrayMap<?,?> EMPTY = new ImmutableSortedArrayMap<>(new Object[0], new Integer[0], null, null, true);

    /**
     * Returns an immutable empty sorted array map. Each call to this method
     * will return the same empty map.
     *
     * @param <K> the type of keys maintained by this map
     * @param <V> the type of mapped values
     * @return an immutable empty sorted array map
     */
    @SuppressWarnings("unchecked")
    public static <K,V> ImmutableSortedArrayMap<K,V> emptyMap() {
        return (ImmutableSortedArrayMap<K,V>) EMPTY;
    }

    ImmutableSortedArrayMap(Object[] map, Integer[] sortedValues,
                Comparator<? super K> keyComparator, Comparator<? super V> valueComparator,
                boolean biMap) {
        this.m_Map = Objects.requireNonNull(map, "map must not be null");
        this.m_SortedValues = Objects.requireNonNull(sortedValues, "sorted values must not be null");
        if((map.length % 2) != 0) {
            throw new IllegalArgumentException("map must contain same number of keys and values");
        }
        if((map.length / 2) != sortedValues.length) {
            throw new IllegalArgumentException("sorted values must be half the map");
        }
        this.m_KeyComparator = keyComparator;
        this.m_ValueComparator = valueComparator;
        this.m_NullsKeyComparator = (keyComparator == null) ?
                Comparator.nullsFirst(Comparator.naturalOrder()) :
                Comparator.nullsFirst(keyComparator);
        this.m_NullsValueComparator = (valueComparator == null) ?
                Comparator.nullsFirst(Comparator.naturalOrder()) :
                Comparator.nullsFirst(valueComparator);
        this.m_BiMap = biMap;
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return m_Map.length / 2;
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return m_Map.length == 0;
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified
     * key.  More formally, returns {@code true} if and only if
     * this map contains a mapping for a key {@code k} such that
     * {@code (key==null ? k==null : key.equals(k))}.  This operation
     * requires time linear in the map size for this implementation.
     *
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified
     *         key, otherwise {@code false}
     */
    @Override
    public boolean containsKey(Object key) {
        int idx = indexOfKey(key);
        return idx >= 0;
    }

    /**
     * Returns {@code true} if this map maps one or more keys to the
     * specified value.  More formally, returns {@code true} if and only if
     * this map contains at least one mapping to a value {@code v} such that
     * {@code (value==null ? v==null : value.equals(v))}.  This operation
     * requires time linear in the map size for this implementation.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the
     *         specified value, otherwise {@code false}
     */
    @Override
    public boolean containsValue(Object value) {
        int idx = indexOfValueSorted(value); // avoid one indirection...
        return idx >= 0;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     */
    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        int idx = indexOfKey(key);
        if(idx >= 0) {
            final int size = m_Map.length / 2;
            return(V) m_Map[size + idx];
        }
        return null;
    }

    /**
     * Returns an Entry object for the map entry at the given array index.
     *
     * @param index the index in the array of the entry to be retrieved
     * @return an Entry object corresponding to the given array index
     * @throws IndexOutOfBoundsException if the index is less than zero or
     * index is greater than or equal to the map size
     */
    @Override
    @SuppressWarnings("unchecked")
    public Entry<K,V> entryAt(int index) {
        final int size = m_Map.length / 2;
        if((index < 0) || (index >= size)) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        K key = (K) m_Map[index];
        V value = (V) m_Map[size + index];
        return new SimpleImmutableEntry<K, V>(key, value);
    }

    /**
     * Returns the key of the map entry at the given array index.
     *
     * @param index the index in the array of the key to be retrieved
     * @return the key at the given array index
     * @throws IndexOutOfBoundsException if the index is less than zero or
     * index is greater than or equal to the map size
     */
    @Override
    @SuppressWarnings("unchecked")
    public K keyAt(int index) {
        if((index < 0) || (index >= m_Map.length / 2)) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        K key = (K) m_Map[index];
        return key;
    }

    /**
     * Returns the value of the map entry at the given array index.
     *
     * @param index the index in the array of the value to be retrieved
     * @return the value at the given array index
     * @throws IndexOutOfBoundsException if the index is less than zero or
     * index is greater than or equal to the map size
     */
    @Override
    @SuppressWarnings("unchecked")
    public V valueAt(int index) {
        final int size = m_Map.length / 2;
        if((index < 0) || (index >= size)) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        V value = (V) m_Map[size + index];
        return value;
    }

    /**
     * Returns the value of the map entry at the given array index, where
     * items are ordered by value.
     *
     * @param index the index in the array of the value to be retrieved
     * @return the value at the given array index
     * @throws IndexOutOfBoundsException if the index is less than zero or
     * index is greater than or equal to the map size
     */
    @SuppressWarnings("unchecked")
    public V sortedValueAt(int index) {
        final int size = m_Map.length / 2;
        if((index < 0) || (index >= size)) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        V value = (V) m_Map[size + m_SortedValues[index]];
        return value;
    }

    /**
     * Returns an Entry object for the map entry at the given array index,
     * where items are ordered by value.
     *
     * @param index the index in the array of the value to be retrieved
     * @return an Entry object at the given array index
     * @throws IndexOutOfBoundsException if the index is less than zero or
     * index is greater than or equal to the map size
     */
    public Entry<K,V> sortedValueEntryAt(int index) {
        return this.entryAt(this.sortedValueIndex(index));
    }

    /**
     * Returns the array index of the given key in the map.
     *
     * @param key the key to be found in the map
     * @return a zero or positive integer if the key is in the
     * backing array, otherwise less than zero to indicate its absence
     */
    @Override
    public int indexOfKey(Object key) {
        int idx = indexOfKeyInternal(key);
        return idx >= 0 ? idx : -1;
    }

    private int indexOfKeyInternal(Object key) {
        return BinarySearchUtils.indexedSearch(this::keyAt, m_Map.length / 2, key, m_NullsKeyComparator);
    }

    /**
     * Returns the array index of the given value in the map. If the Map is
     * not a BiMap, and there are multiple values that match the given value,
     * there is no guarantee which index is returned.
     *
     * @param value the value to be found in the map
     * @return a zero or positive integer if the value is in the
     * backing array, otherwise less than zero to indicate its absence
     */
    @Override
    public int indexOfValue(Object value) {
        int idx = BinarySearchUtils.indexedSearch(this::sortedValueAt, m_Map.length / 2, value, m_NullsValueComparator);
        return idx >= 0 ? m_SortedValues[idx] : -1;
    }

    /**
     * Returns the array index of the given value in the map,where items are
     * ordered by value. If the Map is not a BiMap, and there are multiple
     * values that match the given value, there is no guarantee which index
     * is returned.
     *
     * @param value the value to be found in the map
     * @return a zero or positive integer if the value is in the
     * backing array, otherwise less than zero to indicate its absence
     */
    public int indexOfValueSorted(Object value) {
        int idx = BinarySearchUtils.indexedSearch(this::sortedValueAt, m_Map.length / 2, value, m_NullsValueComparator);
        return idx >= 0 ? idx : -1;
    }

    int sortedValueIndex(int idx) {
        if(idx < 0 || idx > m_SortedValues.length) {
            throw new IndexOutOfBoundsException("index: " + idx);
        }
        return m_SortedValues[idx];
    }

    /**
     * Returns an {@link ArrayBackedSet} view of the mappings contained in this
     * map. The set is backed by the map.
     *
     * @return an array-backed set view of the mappings contained in this map
     */
    @Override
    public ArrayBackedSet<Entry<K, V>> entrySet() {
        return Views.setView(
                new ArrayBackedImmutableList<>(this::entryAt, size(),
                        Spliterator.DISTINCT | Spliterator.NONNULL));
    }

    /**
     * Returns an {@link ArrayBackedSet} view of the mappings contained in this
     * map, ordered by value. The set is backed by the map.
     *
     * @return an array-backed set view of the mappings contained in this map,
     * sorted in value order
     */
    public ArrayBackedSet<Entry<K,V>> entrySetByValue() {
        return Views.setView(
                new ArrayBackedImmutableList<>(this::sortedValueEntryAt, size(),
                        Spliterator.DISTINCT | Spliterator.NONNULL));
    }

    /**
     * Returns an {@link ArrayBackedSet} view of the keys contained in this
     * map. The set is backed by the map.
     *
     * @return an array-backed set view of the keys contained in this map
     */
    @Override
    public ArrayBackedSet<K> keySet() {
        return Views.setView(
                new ArrayBackedImmutableList<K>(this::keyAt, size(),
                        Spliterator.DISTINCT | Spliterator.SORTED,
                        m_NullsKeyComparator));
    }

    /**
     * Returns an {@link ArrayBackedCollection} view of the values contained
     * in this map. The collection is backed by the map.
     *
     * @return an array-backed collection view of the values contained in this
     * map
     */
    @Override
    public ArrayBackedCollection<V> values() {
        if(m_BiMap) {
            return Views.setView(
                    new ArrayBackedImmutableList<V>(this::sortedValueAt, size(),
                            Spliterator.DISTINCT | Spliterator.SORTED,
                            m_NullsValueComparator));
        } else {
            return Views.collectionView(
                    new ArrayBackedImmutableList<V>(this::sortedValueAt, size(),
                            Spliterator.SORTED, m_NullsValueComparator));
        }
    }

    // Implement SortedMap

    /**
     * Returns the comparator used to order the keys in this map, or
     * {@code null} if this map uses the Comparable natural ordering
     * of its keys.
     *
     * @return the comparator used to order the keys in this map,
     * or {@code null} if this map uses the natural ordering of its keys
     */
    @Override
    public Comparator<? super K> comparator() {
        return m_KeyComparator;
    }

    /**
     * Returns the comparator used to order the values in this map, or
     * {@code null} if this map uses the Comparable natural ordering
     * of its values.
     *
     * @return the comparator used to order the values in this map,
     * or {@code null} if this map uses the natural ordering of its values
     */
    public Comparator<? super V> valueComparator() {
        return m_ValueComparator;
    }

    /**
     * Returns a view of the portion of this map whose keys range from
     * {@code fromKey}, inclusive, to {@code toKey}, exclusive.  If
     * {@code fromKey} and {@code toKey} are equal, the returned map
     * is empty.  The returned map is backed by this map. The returned map
     * supports all optional map operations that this map supports.
     *
     * @param fromKey low endpoint (inclusive) of the keys in the returned map
     * @param toKey high endpoint (exclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys range from
     *         {@code fromKey}, inclusive, to {@code toKey}, exclusive
     * @throws ClassCastException if {@code fromKey} and {@code toKey}
     *         cannot be compared to one another using this map's comparator
     *         (or, if the map has no comparator, using natural ordering).
     * @throws NullPointerException if {@code fromKey} or {@code toKey}
     *         is null and this map does not permit null keys
     * @throws IllegalArgumentException if {@code fromKey} is greater than
     *         {@code toKey}; or if this map itself has a restricted
     *         range, and {@code fromKey} or {@code toKey} lies
     *         outside the bounds of the range
     */
    @Override
    public ImmutableSortedArrayMap<K, V> subMap(K fromKey, K toKey) {
        int fromIndex = insertionPointToIndex(indexOfKeyInternal(fromKey));
        int toIndex = insertionPointToIndex(indexOfKeyInternal(toKey));
        return subMapByIndex(fromIndex, toIndex);
    }

    /**
     * Returns a view of the portion of this map whose keys are
     * strictly less than {@code toKey}.  The returned map is backed
     * by this map. The returned map supports all optional map operations
     * that this map supports.
     *
     * @param toKey high endpoint (exclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys are strictly
     *         less than {@code toKey}
     * @throws ClassCastException if {@code toKey} is not compatible
     *         with this map's comparator (or, if the map has no comparator,
     *         if {@code toKey} does not implement {@link Comparable}).
     * @throws NullPointerException if {@code toKey} is null and
     *         this map does not permit null keys
     * @throws IllegalArgumentException if this map itself has a
     *         restricted range, and {@code toKey} lies outside the
     *         bounds of the range
     */
    @Override
    public ImmutableSortedArrayMap<K, V> headMap(K toKey) {
        int fromIndex = 0;
        int toIndex = insertionPointToIndex(indexOfKeyInternal(toKey));
        return subMapByIndex(fromIndex, toIndex);
    }

    /**
     * Returns a view of the portion of this map whose keys are
     * greater than or equal to {@code fromKey}.  The returned map is
     * backed by this map.  The returned map supports all optional map
     * operations that this map supports.
     *
     * @param fromKey low endpoint (inclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys are greater
     *         than or equal to {@code fromKey}
     * @throws ClassCastException if {@code fromKey} is not compatible
     *         with this map's comparator (or, if the map has no comparator,
     *         if {@code fromKey} does not implement {@link Comparable}).
     * @throws NullPointerException if {@code fromKey} is null and
     *         this map does not permit null keys
     * @throws IllegalArgumentException if this map itself has a
     *         restricted range, and {@code fromKey} lies outside the
     *         bounds of the range
     */
    @Override
    public ImmutableSortedArrayMap<K, V> tailMap(K fromKey) {
        int fromIndex = insertionPointToIndex(indexOfKeyInternal(fromKey));
        int toIndex = m_Map.length / 2;
        return subMapByIndex(fromIndex, toIndex);
    }

    private ImmutableSortedArrayMap<K, V> subMapByIndex(int fromIndex, int toIndex) {
        if(toIndex < fromIndex) {
            throw new IllegalArgumentException("toKey is less than fromKey");
        }

        final int size = m_Map.length / 2;
        if((fromIndex == 0) && (toIndex == size)) {
            return this;
        } else if(fromIndex == toIndex) {
            return emptyMap();
        }

        int subSize = toIndex - fromIndex;
        Object[] subMap = new Object[subSize * 2];
        Integer[] subSortedValues = new Integer[subSize];

        // copy keys
        System.arraycopy(m_Map, fromIndex, subMap, 0, subSize);
        // copy values
        System.arraycopy(m_Map, size, subMap, subSize, subSize);

        // copy and reindex sorted value indexes
        int valIndex = 0;
        for(int i = 0; i < size; i++) {
            int idx = m_SortedValues[i];
            if((idx >= fromIndex) && (idx < toIndex)) {
                subSortedValues[valIndex++] = (idx - fromIndex);
            }
        }

        if(valIndex != subSize) {
            throw new IllegalStateException("sorted value index does not compute!");
        }

        return new ImmutableSortedArrayMap<K,V>(subMap, subSortedValues, m_KeyComparator, m_ValueComparator, m_BiMap);
    }

    private static int insertionPointToIndex(int idx) {
        return (idx >= 0) ? idx : 0 - (idx + 1);
    }

    /**
     * Returns the first (lowest) key currently in this map.
     *
     * @return the first (lowest) key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    @SuppressWarnings("unchecked")
    @Override
    public K firstKey() {
        if(m_Map.length == 0) {
            throw new NoSuchElementException();
        }
        return (K)m_Map[0];
    }

    /**
     * Returns the last (highest) key currently in this map.
     *
     * @return the last (highest) key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    @SuppressWarnings("unchecked")
    @Override
    public K lastKey() {
        int size = m_Map.length / 2;
        if(size == 0) {
            throw new NoSuchElementException();
        }
        return (K)m_Map[size - 1];
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key
     */
    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return ArrayBackedMap.getOrDefault(this, key, defaultValue);
    }

    /**
     * Performs the given action for each entry in this map until all entries
     * have been processed or the action throws an exception. Actions are
     * performed in the order of entry set iteration.
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @param action The action to be performed for each entry
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        ArrayBackedMap.forEach(this, action);
    }

    /**
     * Returns a string representation of this map.  The string representation
     * consists of a list of key-value mappings in the order returned by the
     * map's {@code entryAt} indexer, enclosed in braces ({@code "{}"}).
     * Adjacent mappings are separated by the characters
     * {@code ", "} (comma and space).  Each key-value mapping is rendered as
     * the key followed by an equals sign ({@code "="}) followed by the
     * associated value.  Keys and values are converted to strings as by
     * {@link String#valueOf(Object)}.
     * <p>
     * This implementation is handled as a static method rather than a
     * default method because a default method cannot override any methods
     * of {@code Object}.
     *
     * @return a string representation of this map
     */
    @Override
    public String toString() {
        return ArrayBackedMap.toString(this);
    }

    /**
     * Returns the hash code value for this map.  The hash code of a map is
     * defined to be the sum of the hash codes of each entry in the map's
     * {@code entrySet()} view.  This ensures that {@code m1.equals(m2)}
     * implies that {@code m1.hashCode()==m2.hashCode()} for any two maps
     * {@code m1} and {@code m2}, as required by the general contract of
     * {@link Object#hashCode}.
     * <p>
     * This implementation is handled as a static method rather than a
     * default method because a default method cannot override any methods
     * of {@code Object}.
     *
     * @return the hash code value for this map
     */
    @Override
    public int hashCode() {
        return ArrayBackedMap.hashCode(this);
    }

    /**
     * Create a builder object for this immutable sorted array map.
     *
     * @param <K> the type of keys in the resulting array map
     * @param <V> the type of values in the resulting array map
     * @return a new builder object
     */
    public static <K,V> ImmutableSortedArrayMapBuilder<K,V> builder() {
        return new ImmutableSortedArrayMapBuilder<K,V>();
    }
}
