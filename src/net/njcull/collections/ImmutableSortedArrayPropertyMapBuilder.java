package net.njcull.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * Builder for the {@link ImmutableSortedArrayPropertyMap} class.
 *
 * @param <K> the type of keys maintained by the resulting map
 * @param <V> the type of mapped values
 * @author run2000
 * @version 1/09/2017.
 */
public final class ImmutableSortedArrayPropertyMapBuilder<K,V> {
    private java.util.function.Function<? super V, ? extends K> m_KeySupplier;
    private Comparator<? super K> m_KeyComparator;
    private Object[] m_Values = EMPTY_ELEMENTS;
    private int m_Size = 0;

    private static final Object[] EMPTY_ELEMENTS = new Object[0];
    @SuppressWarnings("unchecked")
    private static final Comparator<Comparable> naturalOrder = Comparator.nullsFirst(Comparator.<Comparable>naturalOrder());

    /**
     * Create a new builder instance that builds a new immutable sorted
     * property map.
     *
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @return a new builder for building a new map
     */
    public static <K,V> ImmutableSortedArrayPropertyMapBuilder<K,V> newMap() {
        ImmutableSortedArrayPropertyMapBuilder<K,V> builder = new ImmutableSortedArrayPropertyMapBuilder<>();
        return builder;
    }

    /**
     * Create a new builder instance that builds a new immutable sorted
     * property map, using the given function to provide the keys.
     *
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @param supplier the key supplier
     * @return a new builder for building a new bimap
     */
    public static <K,V> ImmutableSortedArrayPropertyMapBuilder<K,V> newMapWithKeys(Function<? super V, ? extends K> supplier) {
        ImmutableSortedArrayPropertyMapBuilder<K,V> builder = new ImmutableSortedArrayPropertyMapBuilder<>();
        return builder.byKeyMethod(supplier);
    }

    /**
     * Create a new builder instance that builds a new immutable sorted
     * property map, using the given function to provide the keys, and the
     * given comparator to sort the keys within the map.
     *
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @param supplier the key supplier
     * @param keyCmp the key comparator
     * @return a new builder for building a new bimap
     */
    public static <K,V> ImmutableSortedArrayPropertyMapBuilder<K,V> newMapWithKeysComparing(Function<? super V, ? extends K> supplier, Comparator<? super K> keyCmp) {
        ImmutableSortedArrayPropertyMapBuilder<K,V> builder = new ImmutableSortedArrayPropertyMapBuilder<>();
        return builder.byKeyMethod(supplier).byComparingKeys(keyCmp);
    }

    /**
     * Create a new builder instance for constructing a new immutable
     * sorted array property map.
     */
    public ImmutableSortedArrayPropertyMapBuilder() {
    }

    /**
     * Use the supplied function to provide the keys in the resulting map.
     *
     * @param supplier the function to provide the key for each value
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArrayPropertyMapBuilder<K,V> byKeyMethod(Function<? super V, ? extends K> supplier) {
        this.m_KeySupplier = supplier;
        return this;
    }

    /**
     * Use the supplied comparator to sort the keys in the resulting map.
     *
     * @param cmp the comparator for sorting the keys in the map
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArrayPropertyMapBuilder<K,V> byComparingKeys(Comparator<? super K> cmp) {
        this.m_KeyComparator = cmp;
        return this;
    }

    /**
     * Sort the keys for the resulting map using the keys' natural order.
     *
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArrayPropertyMapBuilder<K,V> byNaturalKeyOrder() {
        this.m_KeyComparator = null;
        return this;
    }

    /**
     * All the values from the supplied iterable will be added to the
     * resulting map.
     *
     * @param it the iterable containing values to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArrayPropertyMapBuilder<K,V> with(Iterable<? extends V> it) {
        int count = 0;

        for(Iterator<? extends V> iIt = it.iterator(); iIt.hasNext(); count++) {
            if((count % 8) == 0) {
                ensureCapacity(8);
            }
            V value = iIt.next();
            m_Values[m_Size++] = value;
        }

        return this;
    }

    /**
     * All the values from the supplied collection will be added to the
     * resulting map.
     *
     * @param coll the collection containing values to be added
     * @return this builder, for chaining purposes
     */
    @SuppressWarnings("unchecked")
    public ImmutableSortedArrayPropertyMapBuilder<K,V> with(Collection<? extends V> coll) {
        int size = coll.size();
        ensureCapacity(size);

        for(V entry : coll) {
            m_Values[m_Size++] = entry;
        }

        return this;
    }

    /**
     * Add the given value to the resulting map.
     *
     * @param val the value to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArrayPropertyMapBuilder<K,V> with(V val) {
        ensureCapacity(1);
        m_Values[m_Size++] = val;
        return this;
    }

    /**
     * Add the given values to the resulting map.
     *
     * @param v1 the first value to be added
     * @param v2 the second value to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArrayPropertyMapBuilder<K,V> with(V v1, V v2) {
        ensureCapacity(2);
        m_Values[m_Size++] = v1;
        m_Values[m_Size++] = v2;
        return this;
    }

    /**
     * Add the given values to the resulting map.
     *
     * @param v1 the first value to be added
     * @param v2 the second value to be added
     * @param v3 the third value to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArrayPropertyMapBuilder<K,V> with(V v1, V v2, V v3) {
        ensureCapacity(3);
        m_Values[m_Size++] = v1;
        m_Values[m_Size++] = v2;
        m_Values[m_Size++] = v3;
        return this;
    }

    /**
     * Add the given values to the resulting map.
     *
     * @param v1 the first value to be added
     * @param v2 the second value to be added
     * @param v3 the third value to be added
     * @param v4 the fourth value to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArrayPropertyMapBuilder<K,V> with(V v1, V v2, V v3, V v4) {
        ensureCapacity(4);
        m_Values[m_Size++] = v1;
        m_Values[m_Size++] = v2;
        m_Values[m_Size++] = v3;
        m_Values[m_Size++] = v4;
        return this;
    }

    /**
     * Add the given values to the resulting map.
     *
     * @param values the values to be added
     * @return this builder, for chaining purposes
     */
    @SafeVarargs
    public final ImmutableSortedArrayPropertyMapBuilder<K,V> with(V... values) {
        int len = values.length;
        ensureCapacity(len);
        System.arraycopy(values, 0, m_Values, m_Size, len);
        m_Size += len;
        return this;
    }

    /**
     * For the stream combiner, merge the entries from the supplied builder
     * to this builder.
     * <p>
     * Note that we make no assumptions about the key, the key extractor,
     * or the comparator, on the incoming builder.
     *
     * @param entries the builder containing the entries to be merged into
     * this builder
     * @return this builder containing the merged items
     */
    public ImmutableSortedArrayPropertyMapBuilder<K,V> merge(ImmutableSortedArrayPropertyMapBuilder<?, ? extends V> entries) {
        int len = entries.m_Size;
        ensureCapacity(len);
        System.arraycopy(entries.m_Values, 0, m_Values, m_Size, len);
        m_Size += len;
        return this;
    }

    private void ensureCapacity(int capacity) {
        if(m_Values.length - m_Size < capacity) {
            int newLength = m_Size + capacity;
            // round up to divisible by 8
            newLength += (8 - (newLength % 8)) % 8;
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
     *
     * @return an ImmutableSortedArrayPropertyMap containing the elements in
     * the builder
     * @throws IllegalStateException there was a duplicate key or value
     * specified in the builder
     */
    @SuppressWarnings("unchecked")
    public ImmutableSortedArrayPropertyMap<K,V> build() {
        if(m_Size == 0) {
            return ImmutableSortedArrayPropertyMap.<K,V>emptyMap();
        }

        Function<? super V, ? extends K> keySupplier = m_KeySupplier;
        Comparator<? super K> keyComparator = m_KeyComparator;
        Comparator nullsKeyComparator = (keyComparator == null) ? naturalOrder : Comparator.nullsFirst(keyComparator);

        Integer[] sortedKeys = new Integer[m_Size];
        Object[] values = Arrays.copyOf(m_Values, m_Size);

        for(int i = 0; i < m_Size; i++) {
            Integer iVal = i;
            sortedKeys[i] = iVal;
        }

        // Sort keys by index, insert sorted values into result array
        ArrayComparator<? extends K, ? super V> arrayComparator = new ArrayComparator<>(values, keySupplier, nullsKeyComparator);
        Arrays.sort(sortedKeys, 0, m_Size, arrayComparator);

        Object[] elements = new Object[m_Size];
        for(int i = 0; i < sortedKeys.length; i++) {
            elements[i] = m_Values[sortedKeys[i]];
        }

        Object prev = keySupplier.apply((V)elements[0]);
        for (int i = 1; i < m_Size; i++) {
            Object o = keySupplier.apply((V)elements[i]);
            int cmp = nullsKeyComparator.compare(o, prev);
            if(cmp == 0) {
                throw new IllegalStateException("duplicate key " + o);
            }
            prev = o;
        }

        return new ImmutableSortedArrayPropertyMap<>(elements, keyComparator, keySupplier);
    }

    /**
     * Reset this builder to its initial state.
     *
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArrayPropertyMapBuilder<K,V> clear() {
        m_Values = EMPTY_ELEMENTS;
        m_KeyComparator = null;
        m_KeySupplier = null;
        m_Size = 0;
        return this;
    }

    /**
     * Comparator for creating sorted indexes into the key array.
     */
    private static final class ArrayComparator <K,V> implements Comparator<Integer> {
        private final Object[] m_Values;
        private final Function<V, K> m_Supplier;
        private final Comparator<K> m_Delegate;

        public ArrayComparator(Object[] values, Function<V, K> supplier, Comparator<K> delegate) {
            this.m_Values = Objects.requireNonNull(values, "values cannot be null");
            this.m_Supplier = Objects.requireNonNull(supplier, "key supplier cannot be null");
            this.m_Delegate = Objects.requireNonNull(delegate, "delegate cannot be null");
        }

        @SuppressWarnings("unchecked")
        @Override
        public int compare(Integer o1, Integer o2) {
            return m_Delegate.compare(m_Supplier.apply((V)m_Values[o1]), m_Supplier.apply((V)m_Values[o2]));
        }
    }
}
