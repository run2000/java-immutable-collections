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

    @Override
    public int size() {
        return m_Map.length / 2;
    }

    @Override
    public boolean isEmpty() {
        return m_Map.length == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int idx = indexOfKey(key);
        return idx >= 0;
    }

    @Override
    public boolean containsValue(Object value) {
        int idx = indexOfValueSorted(value); // avoid one indirection...
        return idx >= 0;
    }

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

    @Override
    @SuppressWarnings("unchecked")
    public K keyAt(int index) {
        if((index < 0) || (index >= m_Map.length / 2)) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        K key = (K) m_Map[index];
        return key;
    }

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
     * <p>Entry set ordered by value.</p>
     */
    public Entry<K,V> sortedValueEntryAt(int index) {
        return this.entryAt(this.sortedValueIndex(index));
    }

    @Override
    public int indexOfKey(Object key) {
        int idx = indexOfKeyInternal(key);
        return idx >= 0 ? idx : -1;
    }

    private int indexOfKeyInternal(Object key) {
        return BinarySearchUtils.indexedSearch(this::keyAt, m_Map.length / 2, key, m_NullsKeyComparator);
    }

    /**
     * If the Map is not a BiMap, and there are multiple values that match
     * the given value, there is no guarantee which index is returned.
     */
    @Override
    public int indexOfValue(Object value) {
        int idx = BinarySearchUtils.indexedSearch(this::sortedValueAt, m_Map.length / 2, value, m_NullsValueComparator);
        return idx >= 0 ? m_SortedValues[idx] : -1;
    }

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
     * <p>Entry set ordered by key.</p>
     * {@inheritDoc}
     */
    @Override
    public ArrayBackedSet<Entry<K, V>> entrySet() {
        return Views.setView(
                new ArrayBackedImmutableList<>(this::entryAt, size(),
                        Spliterator.DISTINCT | Spliterator.NONNULL));
    }

    /**
     * <p>Entry set ordered by value.</p>
     * <p>The set's iterator returns the entries in ascending value order.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.
     * </p>
     *
     * @return a set view of the mappings contained in this map,
     *         sorted in ascending value order
     */
    public ArrayBackedSet<Entry<K,V>> entrySetByValue() {
        return Views.setView(
                new ArrayBackedImmutableList<>(this::sortedValueEntryAt, size(),
                        Spliterator.DISTINCT | Spliterator.NONNULL));
    }

    @Override
    public ArrayBackedSet<K> keySet() {
        return Views.setView(
                new ArrayBackedImmutableList<K>(this::keyAt, size(),
                        Spliterator.DISTINCT | Spliterator.SORTED,
                        m_NullsKeyComparator));
    }

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
    @Override
    public Comparator<? super K> comparator() {
        return m_KeyComparator;
    }

    public Comparator<? super V> valueComparator() {
        return m_ValueComparator;
    }

    @Override
    public ImmutableSortedArrayMap<K, V> subMap(K fromKey, K toKey) {
        int fromIndex = insertionPointToIndex(indexOfKeyInternal(fromKey));
        int toIndex = insertionPointToIndex(indexOfKeyInternal(toKey));
        return subMapByIndex(fromIndex, toIndex);
    }

    @Override
    public ImmutableSortedArrayMap<K, V> headMap(K toKey) {
        int fromIndex = 0;
        int toIndex = insertionPointToIndex(indexOfKeyInternal(toKey));
        return subMapByIndex(fromIndex, toIndex);
    }

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

    @SuppressWarnings("unchecked")
    @Override
    public K firstKey() {
        if(m_Map.length == 0) {
            throw new NoSuchElementException();
        }
        return (K)m_Map[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public K lastKey() {
        int size = m_Map.length / 2;
        if(size == 0) {
            throw new NoSuchElementException();
        }
        return (K)m_Map[size - 1];
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return ArrayBackedMap.getOrDefault(this, key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        ArrayBackedMap.forEach(this, action);
    }

    @Override
    public String toString() {
        return ArrayBackedMap.toString(this);
    }

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
