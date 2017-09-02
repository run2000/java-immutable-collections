package net.njcull.collections;

import java.util.*;

/**
 * Builder for the {@link ImmutableSortedArrayMap} class.
 *
 * @author run2000
 * @version 7/01/2016.
 */
public final class ImmutableSortedArrayMapBuilder<K,V> {
    private Comparator<? super K> m_KeyComparator;
    private Comparator<? super V> m_ValueComparator;
    private Object[] m_Keys = EMPTY_ELEMENTS;
    private Object[] m_Values = EMPTY_ELEMENTS;
    private int m_Size = 0;
    private boolean m_Bimap = false;

    private static final Object[] EMPTY_ELEMENTS = new Object[0];
    private static final Comparator<Comparable> naturalOrder = Comparator.nullsFirst(Comparator.<Comparable>naturalOrder());

    public static <K,V> ImmutableSortedArrayMapBuilder<K,V> newMap() {
        ImmutableSortedArrayMapBuilder<K,V> builder = new ImmutableSortedArrayMapBuilder<>();
        return builder.asMap();
    }

    public static <K,V> ImmutableSortedArrayMapBuilder<K,V> newBiMap() {
        ImmutableSortedArrayMapBuilder<K,V> builder = new ImmutableSortedArrayMapBuilder<>();
        return builder.asBiMap();
    }

    public static <K,V> ImmutableSortedArrayMapBuilder<K,V> newMapComparingKeys(Comparator<? super K> cmp) {
        ImmutableSortedArrayMapBuilder<K,V> builder = new ImmutableSortedArrayMapBuilder<>();
        return builder.byComparingKeys(cmp);
    }

    public static <K,V> ImmutableSortedArrayMapBuilder<K,V> newBiMapComparingKeys(Comparator<? super K> cmp) {
        ImmutableSortedArrayMapBuilder<K,V> builder = new ImmutableSortedArrayMapBuilder<>();
        return builder.byComparingKeys(cmp).asBiMap();
    }

    public static <K,V> ImmutableSortedArrayMapBuilder<K,V> newMapComparingValues(Comparator<? super V> cmp) {
        ImmutableSortedArrayMapBuilder<K,V> builder = new ImmutableSortedArrayMapBuilder<>();
        return builder.byComparingValues(cmp);
    }

    public static <K,V> ImmutableSortedArrayMapBuilder<K,V> newBiMapComparingValues(Comparator<? super V> cmp) {
        ImmutableSortedArrayMapBuilder<K,V> builder = new ImmutableSortedArrayMapBuilder<>();
        return builder.byComparingValues(cmp).asBiMap();
    }

    public static <K,V> ImmutableSortedArrayMapBuilder<K,V> newMapComparing(Comparator<? super K> keyCmp, Comparator<? super V> valCmp) {
        ImmutableSortedArrayMapBuilder<K,V> builder = new ImmutableSortedArrayMapBuilder<>();
        return builder.byComparingKeys(keyCmp)
                .byComparingValues(valCmp);
    }

    public static <K,V> ImmutableSortedArrayMapBuilder<K,V> newBiMapComparing(Comparator<? super K> keyCmp, Comparator<? super V> valCmp) {
        ImmutableSortedArrayMapBuilder<K,V> builder = new ImmutableSortedArrayMapBuilder<>();
        return builder.byComparingKeys(keyCmp)
                .byComparingValues(valCmp)
                .asBiMap();
    }

    public ImmutableSortedArrayMapBuilder() {
    }

    public ImmutableSortedArrayMapBuilder<K,V> byComparingKeys(Comparator<? super K> cmp) {
        this.m_KeyComparator = cmp;
        return this;
    }

    public ImmutableSortedArrayMapBuilder<K,V> byNaturalKeyOrder() {
        this.m_KeyComparator = null;
        return this;
    }

    public ImmutableSortedArrayMapBuilder<K,V> byComparingValues(Comparator<? super V> cmp) {
        this.m_ValueComparator = cmp;
        return this;
    }

    public ImmutableSortedArrayMapBuilder<K,V> byNaturalValueOrder() {
        this.m_ValueComparator = null;
        return this;
    }

    public ImmutableSortedArrayMapBuilder<K,V> asMap() {
        m_Bimap = false;
        return this;
    }

    public ImmutableSortedArrayMapBuilder<K,V> asBiMap() {
        m_Bimap = true;
        return this;
    }

    public ImmutableSortedArrayMapBuilder<K,V> with(Iterable<Map.Entry<K,V>> it) {
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

    @SuppressWarnings("unchecked")
    public ImmutableSortedArrayMapBuilder<K,V> with(Map<? extends K, ? extends V> map) {
        Set<? extends Map.Entry<? extends K, ? extends V>> entries = map.entrySet();
        int size = entries.size();
        ensureCapacity(size);

        for(Map.Entry<? extends K,? extends V> entry : entries) {
            m_Keys[m_Size] = entry.getKey();
            m_Values[m_Size++] = entry.getValue();
        }

        return this;
    }

    public ImmutableSortedArrayMapBuilder<K,V> with(K key, V val) {
        ensureCapacity(1);
        m_Keys[m_Size] = key;
        m_Values[m_Size++] = val;
        return this;
    }

    public ImmutableSortedArrayMapBuilder<K,V> with(K k1, V v1, K k2, V v2) {
        ensureCapacity(2);
        m_Keys[m_Size] = k1;
        m_Values[m_Size++] = v1;
        m_Keys[m_Size] = k2;
        m_Values[m_Size++] = v2;
        return this;
    }

    public ImmutableSortedArrayMapBuilder<K,V> with(K k1, V v1, K k2, V v2, K k3, V v3) {
        ensureCapacity(3);
        m_Keys[m_Size] = k1;
        m_Values[m_Size++] = v1;
        m_Keys[m_Size] = k2;
        m_Values[m_Size++] = v2;
        m_Keys[m_Size] = k3;
        m_Values[m_Size++] = v3;
        return this;
    }

    public ImmutableSortedArrayMapBuilder<K,V> with(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
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

    public final ImmutableSortedArrayMapBuilder<K,V> with(Map.Entry<? extends K,? extends V> entry) {
        ensureCapacity(1);
        m_Keys[m_Size] = entry.getKey();
        m_Values[m_Size++] = entry.getValue();
        return this;
    }

    @SafeVarargs
    public final ImmutableSortedArrayMapBuilder<K,V> with(Map.Entry<? extends K,? extends V>... entries) {
        int len = entries.length;
        ensureCapacity(len);
        for(int i = 0; i < len; i++) {
            m_Keys[m_Size] = entries[i].getKey();
            m_Values[m_Size++] = entries[i].getValue();
        }
        return this;
    }

    /**
     * For combiner.
     *
     * @param entries the entries to be merged into this builder
     * @return this builder containing the merged items
     */
    public ImmutableSortedArrayMapBuilder<K,V> merge(ImmutableSortedArrayMapBuilder<? extends K, ? extends V> entries) {
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

    public int size() {
        return m_Size;
    }

    /**
     *
     * @return an ImmutableArrayMap containing the elements in the builder
     * @throws IllegalStateException there was a duplicate key or value
     * specified in the builder
     */
    public ImmutableSortedArrayMap<K,V> build() {
        if(m_Size == 0) {
            return ImmutableSortedArrayMap.<K,V>emptyMap();
        }

        Comparator<? super K> keyComparator = m_KeyComparator;
        Comparator nullsKeyComparator = (keyComparator == null) ? naturalOrder : Comparator.nullsFirst(keyComparator);

        Integer[] sortedKeys = new Integer[m_Size];
        Integer[] sortedValues = new Integer[m_Size];
        Object[] keys = Arrays.copyOf(m_Keys, m_Size);

        for(int i = 0; i < m_Size; i++) {
            Integer iVal = i;
            sortedKeys[i] = iVal;
            sortedValues[i] = iVal;
        }

        // Sort keys by index, insert sorted keys and values into result array
        Arrays.sort(sortedKeys, 0, m_Size, new ArrayComparator(keys, 0, nullsKeyComparator));

        Object[] elements = new Object[m_Size * 2];
        for(int i = 0; i < sortedKeys.length; i++) {
            elements[i] = keys[sortedKeys[i]];
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
        Comparator<? super V> valueComparator = m_ValueComparator;
        Comparator nullsValueComparator = (valueComparator == null) ? naturalOrder : Comparator.nullsFirst(valueComparator);
        Arrays.sort(sortedValues, 0, m_Size, new ArrayComparator(elements, m_Size, nullsValueComparator));

        if(m_Bimap) {
            prev = elements[m_Size + sortedValues[0]];
            for(int i = 1; i < m_Size; i++) {
                Object o = elements[m_Size + sortedValues[i]];
                int cmp = nullsValueComparator.compare(o, prev);
                if(cmp == 0) {
                    throw new IllegalStateException("duplicate value");
                }
                prev = o;
            }
        }

        return new ImmutableSortedArrayMap<>(elements, sortedValues, keyComparator, valueComparator, m_Bimap);
    }

    public ImmutableSortedArrayMapBuilder<K,V> clear() {
        m_Keys = EMPTY_ELEMENTS;
        m_Values = EMPTY_ELEMENTS;
        m_KeyComparator = null;
        m_ValueComparator = null;
        m_Size = 0;
        m_Bimap = false;
        return this;
    }

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
