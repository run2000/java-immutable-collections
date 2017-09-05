package net.njcull.collections;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.Spliterator;
import java.util.function.Function;

/**
 * A {@link SortedMap} backed by an array of elements. The array is the
 * exact length required to contain the values. Values are stored together
 * in sorted key order.
 * <p>
 * Keys are calculated on demand by the given key supplier function.
 * <p>
 * Keys are tested using a binary search implementation. The map's keyset
 * and entryset views may also be viewed as a {@link List}.
 * </p>
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author run2000
 * @version 1/09/2017.
 */
public final class ImmutableSortedArrayPropertyMap<K,V> extends AbstractMap<K,V> implements ArrayBackedMap<K,V>, SortedMap<K,V> {

    private final Object[] m_Map;
    private final Comparator<? super K> m_KeyComparator;
    private final Comparator m_NullsKeyComparator;
    private final Function<? super V, ? extends K> m_KeySupplier;

    private static final ImmutableSortedArrayPropertyMap<?,?> EMPTY = new ImmutableSortedArrayPropertyMap<>(new Object[0], null, null);

    @SuppressWarnings("unchecked")
    public static <K,V> ImmutableSortedArrayPropertyMap<K,V> emptyMap() {
        return (ImmutableSortedArrayPropertyMap<K,V>) EMPTY;
    }

    ImmutableSortedArrayPropertyMap(Object[] map, Comparator<? super K> keyComparator, Function<? super V, ? extends K> keySupplier) {
        this.m_Map = Objects.requireNonNull(map, "map must not be null");
        this.m_KeyComparator = keyComparator;
        this.m_NullsKeyComparator = (keyComparator == null) ?
                Comparator.nullsFirst(Comparator.naturalOrder()) :
                Comparator.nullsFirst(keyComparator);
        this.m_KeySupplier = keySupplier;
    }

    @Override
    public int size() {
        return m_Map.length;
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
            return(V) m_Map[idx];
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Entry<K,V> entryAt(int index) {
        if((index < 0) || (index >= m_Map.length)) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        K key = m_KeySupplier.apply((V) m_Map[index]);
        V value = (V) m_Map[index];
        return new SimpleImmutableEntry<K, V>(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public K keyAt(int index) {
        if((index < 0) || (index >= m_Map.length)) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        K key = m_KeySupplier.apply((V) m_Map[index]);
        return key;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V valueAt(int index) {
        if((index < 0) || (index >= m_Map.length)) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        V value = (V) m_Map[index];
        return value;
    }

    @Override
    public int indexOfKey(Object key) {
        int idx = indexOfKeyInternal(key);
        return idx >= 0 ? idx : -1;
    }

    private int indexOfKeyInternal(Object key) {
        return BinarySearchUtils.indexedSearch(this::keyAt, m_Map.length, key, m_NullsKeyComparator);
    }

    /**
     * If there are multiple values that match the given value, the first
     * index is returned.
     */
    @Override
    public int indexOfValue(Object value) {
        final int size = m_Map.length;
        if(value == null) {
            for(int i = 0; i < size; i++) {
                if(m_Map[i] == null) {
                    return i;
                }
            }
        } else {
            for(int i = 0; i < size; i++) {
                if(value.equals(m_Map[i])) {
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
        return Views.collectionView(
                new ArrayBackedImmutableList<>(this::valueAt, size()));
    }

    // Implement SortedMap
    @Override
    public Comparator<? super K> comparator() {
        return m_KeyComparator;
    }

    @Override
    public ImmutableSortedArrayPropertyMap<K, V> subMap(K fromKey, K toKey) {
        int fromIndex = insertionPointToIndex(indexOfKeyInternal(fromKey));
        int toIndex = insertionPointToIndex(indexOfKeyInternal(toKey));
        return subMapByIndex(fromIndex, toIndex);
    }

    @Override
    public ImmutableSortedArrayPropertyMap<K, V> headMap(K toKey) {
        int fromIndex = 0;
        int toIndex = insertionPointToIndex(indexOfKeyInternal(toKey));
        return subMapByIndex(fromIndex, toIndex);
    }

    @Override
    public ImmutableSortedArrayPropertyMap<K, V> tailMap(K fromKey) {
        int fromIndex = insertionPointToIndex(indexOfKeyInternal(fromKey));
        int toIndex = m_Map.length;
        return subMapByIndex(fromIndex, toIndex);
    }

    private ImmutableSortedArrayPropertyMap<K, V> subMapByIndex(int fromIndex, int toIndex) {
        if(toIndex < fromIndex) {
            throw new IllegalArgumentException("toKey is less than fromKey");
        }

        if((fromIndex == 0) && (toIndex == m_Map.length)) {
            return this;
        } else if(fromIndex == toIndex) {
            return emptyMap();
        }

        int subSize = toIndex - fromIndex;
        Object[] subMap = new Object[subSize];

        // copy values
        System.arraycopy(m_Map, fromIndex, subMap, 0, subSize);

        return new ImmutableSortedArrayPropertyMap<K,V>(subMap, m_KeyComparator, m_KeySupplier);
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
        return m_KeySupplier.apply((V) m_Map[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public K lastKey() {
        int size = m_Map.length;
        if(size == 0) {
            throw new NoSuchElementException();
        }
        return m_KeySupplier.apply((V) m_Map[size - 1]);
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
    public static <K,V> ImmutableSortedArrayPropertyMapBuilder<K,V> builder() {
        return new ImmutableSortedArrayPropertyMapBuilder<K,V>();
    }
}
