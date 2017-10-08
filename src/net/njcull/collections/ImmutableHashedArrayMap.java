package net.njcull.collections;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiConsumer;

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
 * The map's keyset and entryset views may also be viewed as a {@link List}.
 * </p>
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author run2000
 * @version 3/07/2016.
 */
public final class ImmutableHashedArrayMap<K,V> extends AbstractMap<K,V>
        implements ArrayBackedMap<K,V>, Serializable {

    private final Object[] m_Map;
    private transient int[] m_HashCodes;
    private final boolean m_BiMap;

    // Singleton, as an optimization only
    private static final ImmutableHashedArrayMap<?,?> EMPTY = new ImmutableHashedArrayMap<>(new Object[0], new int[0], true);

    // Serializable
    private static final long serialVersionUID = -964316096581791217L;

    /**
     * Returns an immutable empty hashed array map. Each call to this method
     * will return the same empty map.
     *
     * @param <K> the type of keys maintained by this map
     * @param <V> the type of mapped values
     * @return an immutable empty hashed array map
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
                if(Objects.equals(key, m_Map[i])) {
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
                if(Objects.equals(value, m_Map[size + i])) {
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

    /**
     * Returns an {@code Entry} object for the map entry at the given array
     * index.
     *
     * @param index the index in the array of the entry to be retrieved
     * @return an {@code Entry} object corresponding to the given array index
     * @throws IndexOutOfBoundsException if the index is less than zero or
     * index is greater than or equal to the map size
     */
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

    /**
     * Returns the key of the map entry at the given array index.
     *
     * @param index the index in the array of the key to be retrieved
     * @return the key at the given array index
     * @throws IndexOutOfBoundsException if the index is less than zero or
     * index is greater than or equal to the map size
     */
    @SuppressWarnings("unchecked")
    @Override
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

    /**
     * Returns the array index of the given key in the map.
     *
     * @param key the key to be found in the map
     * @return a zero or positive integer if the key is in the
     * backing array, otherwise less than zero to indicate its absence
     */
    @Override
    public int indexOfKey(Object key) {
        final int size = m_Map.length / 2;
        final int hc = Objects.hashCode(key);

        for(int i = 0; i < size; i++) {
            if(hc == m_HashCodes[i]) {
                if(Objects.equals(key, m_Map[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Returns the array index of the given value in the map. If there is
     * more than one value, the first index is returned.
     *
     * @param value the value to be found in the map
     * @return a zero or positive integer if the value is in the
     * backing array, otherwise less than zero to indicate its absence
     */
    @Override
    public int indexOfValue(Object value) {
        final int size = m_Map.length / 2;
        final int hc = Objects.hashCode(value);

        for(int i = 0; i < size; i++) {
            if(hc == m_HashCodes[size + i]) {
                if (Objects.equals(value, m_Map[size + i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Returns the last array index of the given value in the map. For a bi-map,
     * this will return the same value as {@link #indexOfValue(Object)}.
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
                if (Objects.equals(value, m_Map[size + i])) {
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
    @Override
    public ArrayBackedSet<K> keySet() {
        return Views.setView(
                new ArrayBackedImmutableList<>(
                        Views.mapKeyIndexer(this), size(), Spliterator.DISTINCT));
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
                    new ArrayBackedImmutableList<>(
                            Views.mapValueIndexer(this), size(), Spliterator.DISTINCT));
        } else {
            return Views.collectionView(
                    new ArrayBackedImmutableList<>(
                            Views.mapValueIndexer(this), size()));
        }
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
     * Create a builder object for this immutable hashed array map.
     *
     * @param <K> the type of keys in the resulting array map
     * @param <V> the type of values in the resulting array map
     * @return a new builder object
     */
    public static <K,V> ImmutableHashedArrayMapBuilder<K,V> builder() {
        return new ImmutableHashedArrayMapBuilder<K,V>();
    }

    /**
     * Returns an {@code ImmutableHashedArrayMap} that contains the data
     * supplied by the given map. If the supplier map is itself an
     * {@code ImmutableHashedArrayMap}, it will be returned.
     *
     * @param map the map to be copied
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @return an {@code ImmutableHashedArrayMap} containing the data from the
     * given map
     */
    @SuppressWarnings("unchecked")
    public static <K,V> ImmutableHashedArrayMap<K,V> copyOf(Map<? extends K, ? extends V> map) {
        if(map instanceof ImmutableHashedArrayMap) {
            return (ImmutableHashedArrayMap<K,V>)map;
        }
        return ImmutableHashedArrayMapBuilder.<K,V>newMap().with(map).build();
    }

    /**
     * Returns an {@code ImmutableHashedArrayMap}, as a bi-map, that contains
     * the data supplied by the given map. If the supplier map is itself an
     * {@code ImmutableHashedArrayMap} that is a bi-map, it will be returned.
     *
     * @param map the map to be copied
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @return an {@code ImmutableHashedArrayMap} containing the data from the
     * given map
     */
    @SuppressWarnings("unchecked")
    public static <K,V> ImmutableHashedArrayMap<K,V> copyOfBiMap(Map<? extends K, ? extends V> map) {
        if(map instanceof ImmutableHashedArrayMap) {
            ImmutableHashedArrayMap<K, V> arrayMap = (ImmutableHashedArrayMap<K, V>) map;
            if(arrayMap.m_BiMap) {
                // guarantee that this is a bi-map
                return arrayMap;
            }
        }
        return ImmutableHashedArrayMapBuilder.<K,V>newBiMap().with(map).build();
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
        if ((m_Map == null) || ((m_Map.length % 2) != 0)) {
            throw new InvalidObjectException("map must be an equal number of keys and values");
        }

        // Regenerate hashcodes
        m_HashCodes = new int[m_Map.length];
        final int sz = m_Map.length / 2;

        for(int i = 0; i < sz; i++) {
            m_HashCodes[i] = Objects.hashCode((K)m_Map[i]);
            m_HashCodes[sz + i] = Objects.hashCode((V)m_Map[sz + i]);
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
