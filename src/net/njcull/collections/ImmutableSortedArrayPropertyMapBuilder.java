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
 * @author run2000
 * @version 1/09/2017.
 */
public final class ImmutableSortedArrayPropertyMapBuilder<K,V> {
    private java.util.function.Function<? super V, ? extends K> m_KeySupplier;
    private Comparator<? super K> m_KeyComparator;
    private Object[] m_Values = EMPTY_ELEMENTS;
    private int m_Size = 0;

    private static final Object[] EMPTY_ELEMENTS = new Object[0];
    private static final Comparator<Comparable> naturalOrder = Comparator.nullsFirst(Comparator.<Comparable>naturalOrder());

    public static <K,V> ImmutableSortedArrayPropertyMapBuilder<K,V> newMap() {
        ImmutableSortedArrayPropertyMapBuilder<K,V> builder = new ImmutableSortedArrayPropertyMapBuilder<>();
        return builder;
    }

    public static <K,V> ImmutableSortedArrayPropertyMapBuilder<K,V> newMapWithKeys(Function<? super V, ? extends K> supplier) {
        ImmutableSortedArrayPropertyMapBuilder<K,V> builder = new ImmutableSortedArrayPropertyMapBuilder<>();
        return builder.byKeyMethod(supplier);
    }

    public static <K,V> ImmutableSortedArrayPropertyMapBuilder<K,V> newMapWithKeysComparing(Function<? super V, ? extends K> supplier, Comparator<? super K> keyCmp) {
        ImmutableSortedArrayPropertyMapBuilder<K,V> builder = new ImmutableSortedArrayPropertyMapBuilder<>();
        return builder.byKeyMethod(supplier).byComparingKeys(keyCmp);
    }

    public ImmutableSortedArrayPropertyMapBuilder() {
    }

    public ImmutableSortedArrayPropertyMapBuilder<K,V> byKeyMethod(Function<? super V, ? extends K> supplier) {
        this.m_KeySupplier = supplier;
        return this;
    }

    public ImmutableSortedArrayPropertyMapBuilder<K,V> byComparingKeys(Comparator<? super K> cmp) {
        this.m_KeyComparator = cmp;
        return this;
    }

    public ImmutableSortedArrayPropertyMapBuilder<K,V> byNaturalKeyOrder() {
        this.m_KeyComparator = null;
        return this;
    }

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

    @SuppressWarnings("unchecked")
    public ImmutableSortedArrayPropertyMapBuilder<K,V> with(Collection<? extends V> coll) {
        int size = coll.size();
        ensureCapacity(size);

        for(V entry : coll) {
            m_Values[m_Size++] = entry;
        }

        return this;
    }

    public ImmutableSortedArrayPropertyMapBuilder<K,V> with(V val) {
        ensureCapacity(1);
        m_Values[m_Size++] = val;
        return this;
    }

    public ImmutableSortedArrayPropertyMapBuilder<K,V> with(V v1, V v2) {
        ensureCapacity(2);
        m_Values[m_Size++] = v1;
        m_Values[m_Size++] = v2;
        return this;
    }

    public ImmutableSortedArrayPropertyMapBuilder<K,V> with(V v1, V v2, V v3) {
        ensureCapacity(3);
        m_Values[m_Size++] = v1;
        m_Values[m_Size++] = v2;
        m_Values[m_Size++] = v3;
        return this;
    }

    public ImmutableSortedArrayPropertyMapBuilder<K,V> with(V v1, V v2, V v3, V v4) {
        ensureCapacity(4);
        m_Values[m_Size++] = v1;
        m_Values[m_Size++] = v2;
        m_Values[m_Size++] = v3;
        m_Values[m_Size++] = v4;
        return this;
    }

    @SafeVarargs
    public final ImmutableSortedArrayPropertyMapBuilder<K,V> with(V... values) {
        int len = values.length;
        ensureCapacity(len);
        for(int i = 0; i < len; i++) {
            m_Values[m_Size++] = values[i];
        }
        return this;
    }

    /**
     * For combiner.
     * <p>
     * Note that we make no assumptions about the key, the key extractor,
     * or the comparator, on the incoming builder.
     *
     * @param entries the entries to be merged into this builder
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

    public int size() {
        return m_Size;
    }

    /**
     *
     * @return an ImmutableArrayMap containing the elements in the builder
     * @throws IllegalStateException there was a duplicate key or value
     * specified in the builder
     */
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

        Object prev = elements[0];
        for (int i = 1; i < m_Size; i++) {
            Object o = elements[i];
            int cmp = nullsKeyComparator.compare(keySupplier.apply((V)o), keySupplier.apply((V)prev));
            if(cmp == 0) {
                throw new IllegalStateException("duplicate key " + keySupplier.apply((V)o));
            }
            prev = o;
        }

        return new ImmutableSortedArrayPropertyMap<>(elements, keyComparator, keySupplier);
    }

    public ImmutableSortedArrayPropertyMapBuilder<K,V> clear() {
        m_Values = EMPTY_ELEMENTS;
        m_KeyComparator = null;
        m_KeySupplier = null;
        m_Size = 0;
        return this;
    }

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
