package net.njcull.collections;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.Spliterator;
import java.util.function.BiConsumer;

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
public final class ImmutableUniSortedArrayMap<K,V> extends AbstractMap<K,V>
        implements ArrayBackedMap<K,V>, SortedMap<K,V>, Serializable {

    private final Object[] m_Map;
    private final Comparator<? super K> m_KeyComparator;
    private transient Comparator m_NullsKeyComparator;
    private final boolean m_BiMap;

    // Singleton, as an optimization only
    private static final ImmutableUniSortedArrayMap<?,?> EMPTY = new ImmutableUniSortedArrayMap<>(new Object[0], null, true);

    // Serializable
    private static final long serialVersionUID = -2129447832014465908L;

    /**
     * Returns an immutable empty uni-sorted array map. Each call to this method
     * will return the same empty map.
     *
     * @param <K> the type of keys maintained by this map
     * @param <V> the type of mapped values
     * @return an immutable empty uni-sorted array map
     */
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
     * requires time log(n) in the map size for this implementation.
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
        int idx = indexOfValue(value); // avoid one indirection...
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
     * Returns an {@code Entry} object for the map entry at the given array
     * index.
     *
     * @param index the index in the array of the entry to be retrieved
     * @return an {@code Entry} object corresponding to the given array index
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

    @SuppressWarnings("unchecked")
    private int indexOfKeyInternal(Object key) {
        return BinarySearchUtils.indexedSearch(this::keyAt, m_Map.length / 2, key, m_NullsKeyComparator);
    }

    /**
     * Returns the array index of the given value in the map. If there are
     * multiple values that match the given value, the first index is returned.
     *
     * @param value the value to be found in the map
     * @return a zero or positive integer if the value is in the
     * backing array, otherwise less than zero to indicate its absence
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
     * Returns an {@link ArrayBackedSet} view of the mappings contained in this
     * map. The set is backed by the map.
     *
     * @return an array-backed set view of the mappings contained in this map
     */
    @Override
    public ArrayBackedSet<Entry<K, V>> entrySet() {
        return Views.setView(
                new ArrayBackedImmutableList<>(
                        Views.mapEntryIndexer(this), size(),
                        Spliterator.DISTINCT | Spliterator.NONNULL));
    }

    /**
     * Returns an {@link ArrayBackedSet} view of the keys contained in this
     * map. The set is backed by the map.
     *
     * @return an array-backed set view of the keys contained in this map
     */
    @SuppressWarnings("unchecked")
    @Override
    public ArrayBackedSet<K> keySet() {
        return Views.setView(
                new ArrayBackedImmutableList<K>(
                        Views.mapKeyIndexer(this), size(),
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
                    new ArrayBackedImmutableList<V>(
                            Views.mapValueIndexer(this), size(),
                            Spliterator.DISTINCT));
        } else {
            return Views.collectionView(
                    new ArrayBackedImmutableList<>(
                            Views.mapValueIndexer(this), size()));
        }
    }

    // Implement SortedMap

    /**
     * Returns the comparator used to order the keys in this map, or
     * {@code null} if this map uses the {@code Comparable} natural ordering
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
    public ImmutableUniSortedArrayMap<K, V> subMap(K fromKey, K toKey) {
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
    public ImmutableUniSortedArrayMap<K, V> headMap(K toKey) {
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
     * performed in the order of its {@code entryAt(int)} method.
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
     * map's {@code entryAt(int)} method, enclosed in braces ({@code "{}"}).
     * Adjacent mappings are separated by the characters
     * {@code ", "} (comma and space).  Each key-value mapping is rendered as
     * the key followed by an equals sign ({@code "="}) followed by the
     * associated value.  Keys and values are converted to strings as by
     * {@link String#valueOf(Object)}.
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
    public static <K,V> ImmutableUniSortedArrayMapBuilder<K,V> builder() {
        return new ImmutableUniSortedArrayMapBuilder<K,V>();
    }

    /**
     * Returns an {@code ImmutableUniSortedArrayMap} that contains the data
     * supplied by the given map. If the supplier map is itself an
     * {@code ImmutableUniSortedArrayMap}, it will be returned.
     * <p>
     * If the given map is itself a sorted map, the returned map will be
     * sorted by the same key comparator.
     * <p>
     * Otherwise, the returned map will have keys sorted by their natural order.
     *
     * @param map the map to be copied
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @return an {@code ImmutableUniSortedArrayMap} containing the data from
     * the given map
     */
    @SuppressWarnings("unchecked")
    public static <K,V> ImmutableUniSortedArrayMap<K,V> copyOf(Map<? extends K, ? extends V> map) {
        if(map instanceof ImmutableUniSortedArrayMap) {
            return (ImmutableUniSortedArrayMap<K,V>)map;
        }
        if(map instanceof SortedMap) {
            return ImmutableUniSortedArrayMapBuilder.<K,V>newMap().with(map).byComparing(((SortedMap)map).comparator()).build();
        }

        return ImmutableUniSortedArrayMapBuilder.<K,V>newMap().with(map).build();
    }

    /**
     * Returns an {@code ImmutableUniSortedArrayMap} that contains the data
     * supplied by the given map, with keys ordered by the given comparator.
     * If the supplier map is itself an {@code ImmutableUniSortedArrayMap}, and
     * identical key comparator, it will be returned.
     * <p>
     * Otherwise, the returned map will have the keys sorted by the given
     * comparator.
     *
     * @param map the map to be copied
     * @param cmp the key comparator
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @return an {@code ImmutableUniSortedArrayMap} containing the data from
     * the given map
     */
    @SuppressWarnings("unchecked")
    public static <K,V> ImmutableUniSortedArrayMap<K,V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> cmp) {
        if(map instanceof ImmutableUniSortedArrayMap) {
            Comparator<? super K> other = ((ImmutableUniSortedArrayMap<K, V>) map).comparator();
            if (other == cmp) {
                return (ImmutableUniSortedArrayMap<K, V>) map;
            }
        }
        return ImmutableUniSortedArrayMapBuilder.<K,V>newMap().with(map).byComparing(cmp).build();
    }

    /**
     * Returns an {@code ImmutableUniSortedArrayMap} that contains the data
     * supplied by the given map. If the supplier map is itself an
     * {@code ImmutableUniSortedArrayMap}, as a bi-map, it will be returned.
     * <p>
     * If the given map is itself a sorted map, the returned map will be
     * sorted by the same key comparator.
     * <p>
     * Otherwise, the returned bi-map will have keys sorted by their natural
     * order.
     *
     * @param map the map to be copied
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @return an {@code ImmutableUniSortedArrayMap} containing the data from
     * the given map
     */
    @SuppressWarnings("unchecked")
    public static <K,V> ImmutableUniSortedArrayMap<K,V> copyOfBiMap(Map<? extends K, ? extends V> map) {
        if(map instanceof ImmutableUniSortedArrayMap) {
            ImmutableUniSortedArrayMap<K, V> sortedMap = (ImmutableUniSortedArrayMap<K, V>) map;
            if(sortedMap.m_BiMap) {
                // guarantee that this is a bi-map
                return sortedMap;
            }
        }
        if(map instanceof SortedMap) {
            return ImmutableUniSortedArrayMapBuilder.<K,V>newMap().with(map).byComparing(((SortedMap)map).comparator()).build();
        }
        return ImmutableUniSortedArrayMapBuilder.<K,V>newBiMap().with(map).build();
    }

    /**
     * Returns an {@code ImmutableUniSortedArrayMap} that contains the data
     * supplied by the given map, with keys ordered by the given comparator.
     * If the supplier map is itself an {@code ImmutableUniSortedArrayMap}, as
     * a bi-map, and identical key comparator, it will be returned.
     * <p>
     * Otherwise, the returned bi-map will have the keys sorted by the given
     * comparator.
     *
     * @param map the map to be copied
     * @param cmp the key comparator
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @return an {@code ImmutableUniSortedArrayMap} containing the data from
     * the given map
     */
    @SuppressWarnings("unchecked")
    public static <K,V> ImmutableUniSortedArrayMap<K,V> copyOfBiMap(Map<? extends K, ? extends V> map, Comparator<? super K> cmp) {
        if(map instanceof ImmutableUniSortedArrayMap) {
            ImmutableUniSortedArrayMap<K, V> sortedMap = (ImmutableUniSortedArrayMap<K, V>) map;
            if ((sortedMap.m_BiMap) && (sortedMap.comparator() == cmp)) {
                // guarantee that this is a bi-map
                return sortedMap;
            }
        }
        return ImmutableUniSortedArrayMapBuilder.<K,V>newBiMap().with(map).byComparing(cmp).build();
    }

    /**
     * Deserialization.
     *
     * @param stream the object stream to be deserialized
     * @throws ClassNotFoundException the class or descendants could not be found
     * @throws IOException there was a problem reading the object stream
     */
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        stream.defaultReadObject();

        // Perform validation
        if (m_Map == null) {
            throw new InvalidObjectException("map must have elements");
        }

        // Regenerate the nulls comparator
        this.m_NullsKeyComparator = (m_KeyComparator == null) ?
                Comparator.nullsFirst(Comparator.naturalOrder()) :
                Comparator.nullsFirst(m_KeyComparator);

        // Scan keys to ensure ordering is consistent, using the key comparator
        final int sz = m_Map.length / 2;

        if(sz > 0) {
            K prevElem = (K) m_Map[0];

            for (int i = 1; i < sz; i++) {
                K currElem = (K) m_Map[i];
                int cmp = m_NullsKeyComparator.compare(currElem, prevElem);
                if (cmp < 0) {
                    throw new InvalidObjectException("map is not ordered by the comparator");
                }
                prevElem = currElem;
            }

            for (int i = 0; i < sz; i++) {
                V val = (V) m_Map[sz + i];
            }
        }
    }

    /**
     * Deserialization.
     *
     * @return the resolved object
     */
    private Object readResolve() {
        if(m_Map.length == 0) {
            // optimization only
            return EMPTY;
        }
        return this;
    }
}
