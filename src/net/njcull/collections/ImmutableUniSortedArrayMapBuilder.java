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
    private static final Comparator<Comparable> naturalOrder = Comparator.nullsFirst(Comparator.<Comparable>naturalOrder());

    public static <K,V> ImmutableUniSortedArrayMapBuilder<K,V> newMap() {
        ImmutableUniSortedArrayMapBuilder<K,V> builder = new ImmutableUniSortedArrayMapBuilder<>();
        return builder.asMap();
    }

    public static <K,V> ImmutableUniSortedArrayMapBuilder<K,V> newBiMap() {
        ImmutableUniSortedArrayMapBuilder<K,V> builder = new ImmutableUniSortedArrayMapBuilder<>();
        return builder.asBiMap();
    }

    public static <K,V> ImmutableUniSortedArrayMapBuilder<K,V> newMapComparing(Comparator<? super K> cmp) {
        ImmutableUniSortedArrayMapBuilder<K,V> builder = new ImmutableUniSortedArrayMapBuilder<>();
        return builder.byComparing(cmp);
    }

    public static <K,V> ImmutableUniSortedArrayMapBuilder<K,V> newBiMapComparing(Comparator<? super K> cmp) {
        ImmutableUniSortedArrayMapBuilder<K,V> builder = new ImmutableUniSortedArrayMapBuilder<>();
        return builder.byComparing(cmp).asBiMap();
    }

    public ImmutableUniSortedArrayMapBuilder() {
    }

    public ImmutableUniSortedArrayMapBuilder<K,V> byComparing(Comparator<? super K> cmp) {
        this.m_KeyComparator = cmp;
        return this;
    }

    public ImmutableUniSortedArrayMapBuilder<K,V> byNaturalOrder() {
        this.m_KeyComparator = null;
        return this;
    }

    public ImmutableUniSortedArrayMapBuilder<K,V> asMap() {
        m_Bimap = false;
        return this;
    }

    public ImmutableUniSortedArrayMapBuilder<K,V> asBiMap() {
        m_Bimap = true;
        return this;
    }

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

    public ImmutableUniSortedArrayMapBuilder<K,V> with(K key, V val) {
        ensureCapacity(1);
        m_Keys[m_Size] = key;
        m_Values[m_Size++] = val;
        return this;
    }

    public ImmutableUniSortedArrayMapBuilder<K,V> with(K k1, V v1, K k2, V v2) {
        ensureCapacity(2);
        m_Keys[m_Size] = k1;
        m_Values[m_Size++] = v1;
        m_Keys[m_Size] = k2;
        m_Values[m_Size++] = v2;
        return this;
    }

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

    public final ImmutableUniSortedArrayMapBuilder<K,V> with(Map.Entry<? extends K,? extends V> entry) {
        ensureCapacity(1);
        m_Keys[m_Size] = entry.getKey();
        m_Values[m_Size++] = entry.getValue();
        return this;
    }

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
     * For combiner.
     *
     * @param entries the entries to be merged into this builder
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

    public int size() {
        return m_Size;
    }

    /**
     *
     * @return an ImmutableArrayMap containing the elements in the builder
     * @throws IllegalStateException there was a duplicate key or value
     * specified in the builder
     */
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

    public ImmutableUniSortedArrayMapBuilder<K,V> clear() {
        m_Keys = EMPTY_ELEMENTS;
        m_Values = EMPTY_ELEMENTS;
        m_KeyComparator = null;
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
