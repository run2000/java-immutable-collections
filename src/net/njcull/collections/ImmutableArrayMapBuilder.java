package net.njcull.collections;

import java.util.*;

/**
 * Builder for the {@link ImmutableArrayMap} class.
 *
 * @param <K> the type of keys maintained by the resulting map
 * @param <V> the type of mapped values
 * @author run2000
 * @version 7/01/2016.
 */
public final class ImmutableArrayMapBuilder<K,V> {
    private Object[] m_Keys = EMPTY_ELEMENTS;
    private Object[] m_Values = EMPTY_ELEMENTS;
    private int m_Size = 0;
    private boolean m_Bimap = false;

    private static final Object[] EMPTY_ELEMENTS = new Object[0];

    /**
     * Create a new builder instance that builds a new immutable map.
     *
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @return a new builder for building a new map
     */
    public static <K,V> ImmutableArrayMapBuilder<K,V> newMap() {
        ImmutableArrayMapBuilder<K,V> builder = new ImmutableArrayMapBuilder<>();
        return builder.asMap();
    }

    /**
     * Create a new builder instance that builds a new immutable bi-map.
     *
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @return a new builder for building a new bi-map
     */
    public static <K,V> ImmutableArrayMapBuilder<K,V> newBiMap() {
        ImmutableArrayMapBuilder<K,V> builder = new ImmutableArrayMapBuilder<>();
        return builder.asBiMap();
    }

    /**
     * Create a new builder instance for constructing a new immutable
     * array map.
     */
    public ImmutableArrayMapBuilder() {
    }

    /**
     * The builder will build the resulting map as a map, not a bi-map.
     *
     * @return this builder, for chaining purposes
     */
    public ImmutableArrayMapBuilder<K,V> asMap() {
        m_Bimap = false;
        return this;
    }

    /**
     * The builder will build the resulting map as a bi-map.
     *
     * @return this builder, for chaining purposes
     */
    public ImmutableArrayMapBuilder<K,V> asBiMap() {
        m_Bimap = true;
        return this;
    }

    /**
     * All the map entries from the supplied iterable will be added to the
     * resulting map.
     *
     * @param it the iterable containing elements to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableArrayMapBuilder<K,V> with(Iterable<Map.Entry<K,V>> it) {
        int count = 0;

        for(Iterator<Map.Entry<K, V>> iIt = it.iterator(); iIt.hasNext(); count++) {
            if((count % 8) == 0) {
                ensureCapacity(8);
            }
            Map.Entry<K, V> entry = iIt.next();
            m_Keys[m_Size] = entry.getKey();
            m_Values[m_Size++] = entry.getValue();
        }

        return this;
    }

    /**
     * All the map entries from the supplied map will be added to the
     * resulting map.
     *
     * @param map the map containing elements to be added
     * @return this builder, for chaining purposes
     */
    @SuppressWarnings("unchecked")
    public ImmutableArrayMapBuilder<K,V> with(Map<? extends K, ? extends V> map) {
        Set<? extends Map.Entry<? extends K, ? extends V>> entries = map.entrySet();
        int size = entries.size();
        ensureCapacity(size);

        for(Map.Entry<? extends K, ? extends V> entry : entries) {
            m_Keys[m_Size] = entry.getKey();
            m_Values[m_Size++] = entry.getValue();
        }

        return this;
    }

    /**
     * Add the given key and value pair to the resulting map.
     *
     * @param key the key to be added
     * @param val the value to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableArrayMapBuilder<K,V> with(K key, V val) {
        ensureCapacity(1);
        m_Keys[m_Size] = key;
        m_Values[m_Size++] = val;
        return this;
    }

    /**
     * Add the given key and value pairs to the resulting map.
     *
     * @param k1 the first key to be added
     * @param v1 the first value to be added
     * @param k2 the second key to be added
     * @param v2 the second value to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableArrayMapBuilder<K,V> with(K k1, V v1, K k2, V v2) {
        ensureCapacity(2);
        m_Keys[m_Size] = k1;
        m_Values[m_Size++] = v1;
        m_Keys[m_Size] = k2;
        m_Values[m_Size++] = v2;
        return this;
    }

    /**
     * Add the given key and value pairs to the resulting map.
     *
     * @param k1 the first key to be added
     * @param v1 the first value to be added
     * @param k2 the second key to be added
     * @param v2 the second value to be added
     * @param k3 the third key to be added
     * @param v3 the third value to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableArrayMapBuilder<K,V> with(K k1, V v1, K k2, V v2, K k3, V v3) {
        ensureCapacity(3);
        m_Keys[m_Size] = k1;
        m_Values[m_Size++] = v1;
        m_Keys[m_Size] = k2;
        m_Values[m_Size++] = v2;
        m_Keys[m_Size] = k3;
        m_Values[m_Size++] = v3;
        return this;
    }

    /**
     * Add the given key and value pairs to the resulting map.
     *
     * @param k1 the first key to be added
     * @param v1 the first value to be added
     * @param k2 the second key to be added
     * @param v2 the second value to be added
     * @param k3 the third key to be added
     * @param v3 the third value to be added
     * @param k4 the fourth key to be added
     * @param v4 the fourth value to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableArrayMapBuilder<K,V> with(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        ensureCapacity(4);
        m_Keys[m_Size] = k1;
        m_Values[m_Size++] = v1;
        m_Keys[m_Size] = k2;
        m_Values[m_Size++] = v2;
        m_Keys[m_Size] = k3;
        m_Values[m_Size++] = v3;
        m_Keys[m_Size] = k4;
        m_Values[m_Size++] = v4;
        return this;
    }

    /**
     * Add the values from the supplied map entry to the resulting map.
     *
     * @param entry the entry containing the key and value to be added
     * @return this builder, for chaining purposes
     */
    public final ImmutableArrayMapBuilder<K,V> with(Map.Entry<? extends K,? extends V> entry) {
        ensureCapacity(1);
        m_Keys[m_Size] = entry.getKey();
        m_Values[m_Size++] = entry.getValue();
        return this;
    }

    /**
     * Add the values from the supplied map entries to the resulting map.
     *
     * @param elements the entries containing the keys and values to be added
     * @return this builder, for chaining purposes
     */
    @SafeVarargs
    public final ImmutableArrayMapBuilder<K,V> with(Map.Entry<? extends K,? extends V>... elements) {
        int len = elements.length;
        ensureCapacity(len);
        for(int i = 0; i < len; i++) {
            m_Keys[m_Size] = elements[i].getKey();
            m_Values[m_Size++] = elements[i].getValue();
        }
        return this;
    }

    /**
     * For the stream combiner, merge the entries from the supplied builder
     * to this builder. The elements from the supplied builder are appended
     * to the end of the elements in this builder.
     *
     * @param entries the builder containing the entries to be merged into
     * this builder
     * @return this builder containing the merged items
     */
    public ImmutableArrayMapBuilder<K,V> merge(ImmutableArrayMapBuilder<? extends K, ? extends V> entries) {
        int len = entries.m_Size;
        ensureCapacity(len);
        System.arraycopy(entries.m_Keys, 0, m_Keys, m_Size, len);
        System.arraycopy(entries.m_Values, 0, m_Values, m_Size, len);
        m_Size += len;
        return this;
    }

    private void ensureCapacity(int capacity) {
        if(m_Keys.length - m_Size < capacity) {
            int newLength = m_Size + capacity;
            // round up to divisible by 8
            newLength += (8 - (newLength % 8)) % 8;
            m_Keys = Arrays.copyOf(m_Keys, newLength);
            m_Values = Arrays.copyOf(m_Values, newLength);
        }
    }

    /**
     * Returns the number of entries in this builder.
     *
     * @return the number of entries in this builder
     */
    public int size() {
        return m_Size;
    }

    /**
     * Build the immutable map. Validates all keys and values added, including
     * checking for duplicate keys and values as necessary.
     * <p>
     * Repeated calls to a builder containing a non-zero number of entries
     * will return distinct map instances.
     *
     * @return an ImmutableArrayMap containing the elements in the builder
     * @throws IllegalStateException there was a duplicate key or value
     * specified in the builder
     */
    public ImmutableArrayMap<K,V> build() {
        if(m_Size == 0) {
            return ImmutableArrayMap.<K,V>emptyMap();
        }

        Object[] elements = new Object[m_Size * 2];
        Set<Object> dups = new HashSet<>(m_Size);
        for(int i = 0; i < m_Size; i++) {
            Object o = m_Keys[i];
            if(dups.contains(o)) {
                throw new IllegalStateException("duplicate key");
            }
            dups.add(o);
            elements[i] = o;
        }
        dups.clear();
        for (int i = 0; i < m_Size; i++) {
            Object o = m_Values[i];
            if(m_Bimap) {
                if(dups.contains(o)) {
                    throw new IllegalStateException("duplicate value");
                }
                dups.add(o);
            }
            elements[m_Size + i] = o;
        }
        dups.clear();

        return new ImmutableArrayMap<>(elements, m_Bimap);
    }

    /**
     * Reset this builder to its initial state.
     *
     * @return this builder, for chaining purposes
     */
    public ImmutableArrayMapBuilder<K,V> clear() {
        m_Keys = EMPTY_ELEMENTS;
        m_Values = EMPTY_ELEMENTS;
        m_Size = 0;
        m_Bimap = false;
        return this;
    }
}
