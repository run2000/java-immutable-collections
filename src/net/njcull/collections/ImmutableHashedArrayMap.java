package net.njcull.collections;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * A {@link Map} backed by an array of elements, and a separate array of
 * hash codes. The arrays are the exact length required to contain the keys and
 * values, and their corresponding hash codes. Keys and values are stored
 * together in key order. Both keys and values are indexed with their hash codes
 * in a separate array of ints.
 * <p>
 * Keys and values are tested using a linear search implementation.
 * Searches are performed by testing the hash codes. If the hash codes match,
 * the key or value is then tested for equality. This allows for a fast,
 * cache-friendly, linear scan of elements in the map.
 * </p>
 * <p>
 * The map's keyset and entryset views may also be view as a {@link List}.
 * </p>
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author run2000
 * @version 3/07/2016.
 */
public final class ImmutableHashedArrayMap<K,V> extends AbstractMap<K,V> implements ArrayBackedMap<K,V> {

    private final Object[] m_Map;
    private final int[] m_HashCodes;
    private final boolean m_BiMap;

    private static final ImmutableHashedArrayMap<?,?> EMPTY = new ImmutableHashedArrayMap<>(new Object[0], new int[0], true);

    /**
     * Returns an immutable empty array map. Each call to this method will return
     * the same empty map.
     *
     * @param <K> the type of keys maintained by this map
     * @param <V> the type of mapped values
     * @return an immutable empty array map
     */
    @SuppressWarnings("unchecked")
    public static <K,V> ImmutableHashedArrayMap<K,V> emptyMap() {
        return (ImmutableHashedArrayMap<K,V>) EMPTY;
    }

    ImmutableHashedArrayMap(Object[] map, int[] hashCodes, boolean biMap) {
        this.m_Map = Objects.requireNonNull(map, "map must not be null");
        if((map.length % 2) != 0) {
            throw new IllegalArgumentException("map must contain same number of keys and values");
        }
        this.m_HashCodes = Objects.requireNonNull(hashCodes, "hashcodes must not be null");
        if(map.length != hashCodes.length) {
            throw new IllegalArgumentException("map must contain same number of entries and hashcodes");
        }
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
        final int size = m_Map.length / 2;
        final int hc = Objects.hashCode(key);
        for(int i = 0; i < size; i++) {
            if(hc == m_HashCodes[i]) {
                if(key == m_Map[i]) {
                    return true;
                } else if ((key != null) && (key.equals(m_Map[i]))) {
                    return true;
                }
            }
        }
        return false;
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
        final int size = m_Map.length / 2;
        final int hc = Objects.hashCode(value);

        for(int i = 0; i < size; i++) {
            if(hc == m_HashCodes[size + i]) {
                if(value == m_Map[size + i]) {
                    return true;
                } else if((value != null) && (value.equals(m_Map[size + i]))) {
                    return true;
                }
            }
        }
        return false;
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
        final int size = m_Map.length / 2;
        final int hc = Objects.hashCode(key);

        for(int i = 0; i < size; i++) {
            if(hc == m_HashCodes[i]) {
                if (key == m_Map[i]) {
                    return (V) m_Map[size + i];
                } else if ((key != null) && (key.equals(m_Map[i]))) {
                    return (V) m_Map[size + i];
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Entry<K,V> entryAt(int index) {
        final int size = m_Map.length / 2;
        if((index < 0) || (index >= size)) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        K key = (K) m_Map[index];
        V value = (V) m_Map[size + index];
        return new SimpleImmutableEntry<K, V>(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public K keyAt(int index) {
        if((index < 0) || (index >= m_Map.length / 2)) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        K key = (K) m_Map[index];
        return key;
    }

    @SuppressWarnings("unchecked")
    @Override
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
        final int size = m_Map.length / 2;
        final int hc = Objects.hashCode(key);

        for(int i = 0; i < size; i++) {
            if(hc == m_HashCodes[i]) {
                if(key == m_Map[i]) {
                    return i;
                } else if ((key != null) && (key.equals(m_Map[i]))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int indexOfValue(Object value) {
        final int size = m_Map.length / 2;
        final int hc = Objects.hashCode(value);

        for(int i = 0; i < size; i++) {
            if(hc == m_HashCodes[size + i]) {
                if (value == m_Map[size + i]) {
                    return i;
                } else if ((value != null) && (value.equals(m_Map[size + i]))) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * For a BiMap, this will return the same value as
     * {@link #indexOfValue(Object)}.
     *
     * @param value the value to be found in the map
     * @return a zero or positive integer if the value is in the
     * backing array, otherwise less than zero to indicate its absence
     */
    public int lastIndexOfValue(Object value) {
        final int size = m_Map.length / 2;
        final int hc = Objects.hashCode(value);

        for(int i = size - 1; i >= 0; i--) {
            if(hc == m_HashCodes[size + i]) {
                if (value == m_Map[size + i]) {
                    return i;
                } else if ((value != null) && (value.equals(m_Map[size + i]))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ArrayBackedSet<Entry<K, V>> entrySet() {
        return Views.setView(new ImmutableEntryList<>(this));
    }

    @Override
    public ArrayBackedSet<K> keySet() {
        return Views.setView(new ArrayBackedMapKeyList<>(this));
    }

    @Override
    public ArrayBackedCollection<V> values() {
        if(m_BiMap) {
            return Views.setView(new ArrayBackedBiMapValueList<>(this));
        } else {
            return Views.collectionView(new ArrayBackedMapValueList<>(this));
        }
    }

    /**
     * Create a builder object for this immutable hashed array map.
     *
     * @param <K> the type of keys in the resulting array map
     * @param <V> the type of values in the resulting array map
     * @return a new builder object
     */
    public static <K,V> ImmutableHashedArrayMapBuilder<K,V> builder() {
        return new ImmutableHashedArrayMapBuilder<K,V>();
    }

    private static final class ImmutableEntryList<K,V> extends AbstractRandomAccessList<Entry<K,V>> {
        private final ArrayBackedMap<K,V> m_Map;

        ImmutableEntryList(ArrayBackedMap<K, V> map) {
            this.m_Map = Objects.requireNonNull(map, "map must be non-null");
        }

        @Override
        public Entry<K, V> get(int index) {
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
        public Spliterator<Entry<K, V>> spliterator() {
            return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.CONCURRENT);
        }
    }

}
