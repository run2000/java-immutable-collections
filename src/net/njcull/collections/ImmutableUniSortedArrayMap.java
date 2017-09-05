package net.njcull.collections;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.Spliterator;

/**
 * A {@link SortedMap} backed by an array of elements. The array is the
 * exact length required to contain the keys and values. Only keys are sorted.
 * <p>
 * Keys and values are stored together in sorted key order.
 * <p>
 * Keys are tested using a binary search implementation.
 * The map's keyset and entryset views may also be viewed as a {@link List}.
 * </p>
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author run2000
 * @version 7/01/2016.
 */
public final class ImmutableUniSortedArrayMap<K,V> extends AbstractMap<K,V> implements ArrayBackedMap<K,V>, SortedMap<K,V> {

    private final Object[] m_Map;
    private final Comparator<? super K> m_KeyComparator;
    private final Comparator m_NullsKeyComparator;
    private final boolean m_BiMap;

    private static final ImmutableUniSortedArrayMap<?,?> EMPTY = new ImmutableUniSortedArrayMap<>(new Object[0], null, true);

    @SuppressWarnings("unchecked")
    public static <K,V> ImmutableUniSortedArrayMap<K,V> emptyMap() {
        return (ImmutableUniSortedArrayMap<K,V>) EMPTY;
    }

    ImmutableUniSortedArrayMap(Object[] map,
                Comparator<? super K> keyComparator, boolean biMap) {
        this.m_Map = Objects.requireNonNull(map, "map must not be null");
        if((map.length % 2) != 0) {
            throw new IllegalArgumentException("map must contain same number of keys and values");
        }
        this.m_KeyComparator = keyComparator;
        this.m_NullsKeyComparator = (keyComparator == null) ?
                Comparator.nullsFirst(Comparator.naturalOrder()) :
                Comparator.nullsFirst(keyComparator);
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
        int idx = indexOfValue(value); // avoid one indirection...
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
     * the given value, the first matching index is returned.
     */
    @Override
    public int indexOfValue(Object value) {
        final int size = m_Map.length / 2;
        if(value == null) {
            for(int i = 0; i < size; i++) {
                if(value == m_Map[size + i]) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (value.equals(m_Map[size + i])) {
                    return i;
                }
            }
        }
        return -1;
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
                    new ArrayBackedImmutableList<V>(this::valueAt, size(),
                            Spliterator.DISTINCT));
        } else {
            return Views.collectionView(
                    new ArrayBackedImmutableList<>(this::valueAt, size()));
        }
    }

    // Implement SortedMap
    @Override
    public Comparator<? super K> comparator() {
        return m_KeyComparator;
    }

    @Override
    public ImmutableUniSortedArrayMap<K, V> subMap(K fromKey, K toKey) {
        int fromIndex = insertionPointToIndex(indexOfKeyInternal(fromKey));
        int toIndex = insertionPointToIndex(indexOfKeyInternal(toKey));
        return subMapByIndex(fromIndex, toIndex);
    }

    @Override
    public ImmutableUniSortedArrayMap<K, V> headMap(K toKey) {
        int fromIndex = 0;
        int toIndex = insertionPointToIndex(indexOfKeyInternal(toKey));
        return subMapByIndex(fromIndex, toIndex);
    }

    @Override
    public ImmutableUniSortedArrayMap<K, V> tailMap(K fromKey) {
        int fromIndex = insertionPointToIndex(indexOfKeyInternal(fromKey));
        int toIndex = m_Map.length / 2;
        return subMapByIndex(fromIndex, toIndex);
    }

    private ImmutableUniSortedArrayMap<K, V> subMapByIndex(int fromIndex, int toIndex) {
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

        // copy keys
        System.arraycopy(m_Map, fromIndex, subMap, 0, subSize);
        // copy values
        System.arraycopy(m_Map, size, subMap, subSize, subSize);

        return new ImmutableUniSortedArrayMap<K,V>(subMap, m_KeyComparator, m_BiMap);
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
    public static <K,V> ImmutableUniSortedArrayMapBuilder<K,V> builder() {
        return new ImmutableUniSortedArrayMapBuilder<K,V>();
    }
}
