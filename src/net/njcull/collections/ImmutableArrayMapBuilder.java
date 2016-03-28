package net.njcull.collections;

import java.util.*;

/**
 * Builder for the {@link ImmutableArrayMap} class.
 *
 * @author run2000
 * @version 7/01/2016.
 */
public final class ImmutableArrayMapBuilder<K,V> {
    private Object[] m_Keys = EMPTY_ELEMENTS;
    private Object[] m_Values = EMPTY_ELEMENTS;
    private int m_Size = 0;
    private boolean m_Bimap = false;

    private static final Object[] EMPTY_ELEMENTS = new Object[0];

    public static <K,V> ImmutableArrayMapBuilder<K,V> newMap() {
        ImmutableArrayMapBuilder<K,V> builder = new ImmutableArrayMapBuilder<>();
        return builder.asMap();
    }

    public static <K,V> ImmutableArrayMapBuilder<K,V> newBiMap() {
        ImmutableArrayMapBuilder<K,V> builder = new ImmutableArrayMapBuilder<>();
        return builder.asBiMap();
    }

    public ImmutableArrayMapBuilder() {
    }

    public ImmutableArrayMapBuilder<K,V> asMap() {
        m_Bimap = false;
        return this;
    }

    public ImmutableArrayMapBuilder<K,V> asBiMap() {
        m_Bimap = true;
        return this;
    }

    public ImmutableArrayMapBuilder<K,V> with(Iterable<Map.Entry<? extends K,? extends V>> it) {
        int count = 0;

        for(Iterator<Map.Entry<? extends K,? extends V>> iIt = it.iterator(); iIt.hasNext(); count++) {
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
    public ImmutableArrayMapBuilder<K,V> with(Map<K, V> map) {
        Set<? extends Map.Entry<K, V>> entries = map.entrySet();
        int size = entries.size();
        ensureCapacity(size);

        for(Map.Entry<K, V> entry : entries) {
            m_Keys[m_Size] = entry.getKey();
            m_Values[m_Size++] = entry.getValue();
        }

        return this;
    }

    public ImmutableArrayMapBuilder<K,V> with(K key, V val) {
        ensureCapacity(1);
        m_Keys[m_Size] = key;
        m_Values[m_Size++] = val;
        return this;
    }

    public ImmutableArrayMapBuilder<K,V> with(K k1, V v1, K k2, V v2) {
        ensureCapacity(2);
        m_Keys[m_Size] = k1;
        m_Values[m_Size++] = v1;
        m_Keys[m_Size] = k2;
        m_Values[m_Size++] = v2;
        return this;
    }

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

    public final ImmutableArrayMapBuilder<K,V> with(Map.Entry<? extends K,? extends V> entry) {
        ensureCapacity(1);
        m_Keys[m_Size] = entry.getKey();
        m_Values[m_Size++] = entry.getValue();
        return this;
    }

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
     * For combiner.
     *
     * @param entries the entries to be merged into this builder
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

    public int size() {
        return m_Size;
    }

    /**
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
        Set<Object> dups = new HashSet<Object>(m_Size);
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

    public ImmutableArrayMapBuilder<K,V> clear() {
        m_Keys = EMPTY_ELEMENTS;
        m_Values = EMPTY_ELEMENTS;
        m_Size = 0;
        m_Bimap = false;
        return this;
    }
}
