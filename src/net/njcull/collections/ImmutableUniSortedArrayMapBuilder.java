package net.njcull.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Builder for the {@link ImmutableUniSortedArrayMap} class.
 *
 * @param <K> the type of keys maintained by the resulting map
 * @param <V> the type of mapped values
 * @author run2000
 * @version 7/01/2016.
 */
public final class ImmutableUniSortedArrayMapBuilder<K,V> {
    private Comparator<? super K> m_KeyComparator;
    private Object[] m_Keys = EMPTY_ELEMENTS;
    private Object[] m_Values = EMPTY_ELEMENTS;
    private int m_Size = 0;
    private boolean m_Bimap = false;

    private static final Object[] EMPTY_ELEMENTS = new Object[0];
    @SuppressWarnings("unchecked")
    private static final Comparator<Comparable> naturalOrder = Comparator.nullsFirst(Comparator.<Comparable>naturalOrder());

    /**
     * Create a new builder instance that builds a new immutable uni-sorted map.
     *
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @return a new builder for building a new map
     */
    public static <K,V> ImmutableUniSortedArrayMapBuilder<K,V> newMap() {
        ImmutableUniSortedArrayMapBuilder<K,V> builder = new ImmutableUniSortedArrayMapBuilder<>();
        return builder.asMap();
    }

    /**
     * Create a new builder instance that builds a new immutable uni-sorted bimap.
     *
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @return a new builder for building a new bimap
     */
    public static <K,V> ImmutableUniSortedArrayMapBuilder<K,V> newBiMap() {
        ImmutableUniSortedArrayMapBuilder<K,V> builder = new ImmutableUniSortedArrayMapBuilder<>();
        return builder.asBiMap();
    }

    /**
     * Create a new builder instance that builds a new immutable uni-sorted map
     * using the supplied comparator for sorting the keys.
     *
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @param cmp the key comparator for sorting the map
     * @return a new builder for building a new map
     */
    public static <K,V> ImmutableUniSortedArrayMapBuilder<K,V> newMapComparing(Comparator<? super K> cmp) {
        ImmutableUniSortedArrayMapBuilder<K,V> builder = new ImmutableUniSortedArrayMapBuilder<>();
        return builder.byComparing(cmp);
    }

    /**
     * Create a new builder instance that builds a new immutable uni-sorted bimap
     * using the supplied comparator for sorting the keys.
     *
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @param cmp the key comparator for sorting the map
     * @return a new builder for building a new map
     */
    public static <K,V> ImmutableUniSortedArrayMapBuilder<K,V> newBiMapComparing(Comparator<? super K> cmp) {
        ImmutableUniSortedArrayMapBuilder<K,V> builder = new ImmutableUniSortedArrayMapBuilder<>();
        return builder.byComparing(cmp).asBiMap();
    }

    /**
     * Create a new builder instance for constructing a new immutable
     * uni-sorted array map.
     */
    public ImmutableUniSortedArrayMapBuilder() {
    }

    /**
     * Use the supplied comparator to sort the keys in the resulting map.
     *
     * @param cmp the comparator for sorting the keys in the map
     * @return this builder, for chaining purposes
     */
    public ImmutableUniSortedArrayMapBuilder<K,V> byComparing(Comparator<? super K> cmp) {
        this.m_KeyComparator = cmp;
        return this;
    }

    /**
     * Sort the keys for the resulting map using the keys' natural order.
     *
     * @return this builder, for chaining purposes
     */
    public ImmutableUniSortedArrayMapBuilder<K,V> byNaturalOrder() {
        this.m_KeyComparator = null;
        return this;
    }

    /**
     * The builder will build the resulting map as a map, not a bi-map.
     *
     * @return this builder, for chaining purposes
     */
    public ImmutableUniSortedArrayMapBuilder<K,V> asMap() {
        m_Bimap = false;
        return this;
    }

    /**
     * The builder will build the resulting map as a bi-map.
     *
     * @return this builder, for chaining purposes
     */
    public ImmutableUniSortedArrayMapBuilder<K,V> asBiMap() {
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
    public ImmutableUniSortedArrayMapBuilder<K,V> with(Iterable<Map.Entry<K,V>> it) {
        int count = 0;

        for(Iterator<Map.Entry<K,V>> iIt = it.iterator(); iIt.hasNext(); count++) {
            if((count % 8) == 0) {
                ensureCapacity(8);
            }
            Map.Entry<? extends K, ? extends V> entry = iIt.next();
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
    public ImmutableUniSortedArrayMapBuilder<K,V> with(Map<? extends K, ? extends V> map) {
        Set<? extends Map.Entry<? extends K, ? extends V>> entries = map.entrySet();
        int size = entries.size();
        ensureCapacity(size);

        for(Map.Entry<? extends K,? extends V> entry : entries) {
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
    public ImmutableUniSortedArrayMapBuilder<K,V> with(K key, V val) {
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
    public ImmutableUniSortedArrayMapBuilder<K,V> with(K k1, V v1, K k2, V v2) {
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
    public ImmutableUniSortedArrayMapBuilder<K,V> with(K k1, V v1, K k2, V v2, K k3, V v3) {
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
    public ImmutableUniSortedArrayMapBuilder<K,V> with(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
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
    public final ImmutableUniSortedArrayMapBuilder<K,V> with(Map.Entry<? extends K,? extends V> entry) {
        ensureCapacity(1);
        m_Keys[m_Size] = entry.getKey();
        m_Values[m_Size++] = entry.getValue();
        return this;
    }

    /**
     * Add the values from the supplied map entries to the resulting map.
     *
     * @param entries the entries containing the keys and values to be added
     * @return this builder, for chaining purposes
     */
    @SafeVarargs
    public final ImmutableUniSortedArrayMapBuilder<K,V> with(Map.Entry<? extends K,? extends V>... entries) {
        int len = entries.length;
        ensureCapacity(len);
        for(int i = 0; i < len; i++) {
            m_Keys[m_Size] = entries[i].getKey();
            m_Values[m_Size++] = entries[i].getValue();
        }
        return this;
    }

    /**
     * For the stream combiner, merge the entries from the supplied builder
     * to this builder.
     *
     * @param entries the builder containing the entries to be merged into
     * this builder
     * @return this builder containing the merged items
     */
    public ImmutableUniSortedArrayMapBuilder<K,V> merge(ImmutableUniSortedArrayMapBuilder<? extends K, ? extends V> entries) {
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
     * sorting the keys and values, and checking for duplicate keys and values
     * as necessary.
     * <p>
     * Repeated calls to a builder containing a non-zero number of entries
     * will return distinct map instances.
     *
     * @return an ImmutableUniSortedArrayMap containing the elements in the
     * builder
     * @throws IllegalStateException there was a duplicate key or value
     * specified in the builder
     */
    @SuppressWarnings("unchecked")
    public ImmutableUniSortedArrayMap<K,V> build() {
        if(m_Size == 0) {
            return ImmutableUniSortedArrayMap.<K,V>emptyMap();
        }

        Comparator<? super K> keyComparator = m_KeyComparator;
        Comparator nullsKeyComparator = (keyComparator == null) ? naturalOrder : Comparator.nullsFirst(keyComparator);

        Integer[] sortedKeys = new Integer[m_Size];

        for(int i = 0; i < m_Size; i++) {
            Integer iVal = i;
            sortedKeys[i] = iVal;
        }

        // Sort keys by index, insert sorted keys and values into result array
        Arrays.sort(sortedKeys, 0, m_Size, new ArrayComparator(m_Keys, 0, nullsKeyComparator));

        Object[] elements = new Object[m_Size * 2];
        for(int i = 0; i < sortedKeys.length; i++) {
            elements[i] = m_Keys[sortedKeys[i]];
            elements[m_Size + i] = m_Values[sortedKeys[i]];
        }

        Object prev = elements[0];
        for (int i = 1; i < m_Size; i++) {
            Object o = elements[i];
            int cmp = nullsKeyComparator.compare(o, prev);
            if(cmp == 0) {
                throw new IllegalStateException("duplicate key");
            }
            prev = o;
        }

        // Sort keys by index, insert sorted keys and values into result array
        return new ImmutableUniSortedArrayMap<>(elements, keyComparator, m_Bimap);
    }

    /**
     * Reset this builder to its initial state.
     *
     * @return this builder, for chaining purposes
     */
    public ImmutableUniSortedArrayMapBuilder<K,V> clear() {
        m_Keys = EMPTY_ELEMENTS;
        m_Values = EMPTY_ELEMENTS;
        m_KeyComparator = null;
        m_Size = 0;
        m_Bimap = false;
        return this;
    }

    /**
     * Comparator for creating sorted indexes into the key array.
     */
    private static final class ArrayComparator implements Comparator<Integer> {
        private final Object[] m_Values;
        private final int m_Offset;
        private final Comparator m_Delegate;

        public ArrayComparator(Object[] values, int offset, Comparator delegate) {
            this.m_Values = Objects.requireNonNull(values, "values cannot be null");
            this.m_Offset = offset;
            this.m_Delegate = Objects.requireNonNull(delegate, "delegate cannot be null");

            if(offset < 0 || offset >= values.length) {
                throw new IndexOutOfBoundsException("offset: " + offset);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public int compare(Integer o1, Integer o2) {
            return m_Delegate.compare(m_Values[o1 + m_Offset], m_Values[o2 + m_Offset]);
        }
    }
}
