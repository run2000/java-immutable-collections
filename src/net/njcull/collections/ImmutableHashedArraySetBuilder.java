package net.njcull.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;

/**
 * Builder for the {@link ImmutableHashedArraySet} class.
 *
 * @author run2000
 * @version 3/07/2016.
 */
public final class ImmutableHashedArraySetBuilder<E> {
    private Object[] m_Elements = EMPTY_ELEMENTS;
    private int m_Size = 0;

    private static final Object[] EMPTY_ELEMENTS = new Object[0];

    public ImmutableHashedArraySetBuilder() {
    }

    public ImmutableHashedArraySetBuilder<E> with(Iterable<? extends E> it) {
        int count = 0;

        for(Iterator<? extends E> iIt = it.iterator(); iIt.hasNext(); count++) {
            if((count % 8) == 0) {
                ensureCapacity(8);
            }
            m_Elements[m_Size++] = iIt.next();
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    public ImmutableHashedArraySetBuilder<E> with(Collection<? extends E> coll) {
        int size = coll.size();
        ensureCapacity(size);
        if((coll instanceof List) && (coll instanceof RandomAccess) && (size < Integer.MAX_VALUE)) {
            List<? extends E> list = (List<? extends E>) coll;
            for(int i = 0; i < size; i++) {
                m_Elements[m_Size++] = list.get(i);
            }
        } else {
            int count = 0;
            for(Iterator<? extends E> iColl = coll.iterator(); iColl.hasNext() && count < size; count++) {
                m_Elements[m_Size++] = iColl.next();
            }
        }
        return this;
    }

    public ImmutableHashedArraySetBuilder<E> with(E elem) {
        ensureCapacity(1);
        m_Elements[m_Size++] = elem;
        return this;
    }

    public ImmutableHashedArraySetBuilder<E> with(E e1, E e2) {
        ensureCapacity(2);
        m_Elements[m_Size++] = e1;
        m_Elements[m_Size++] = e2;
        return this;
    }

    public ImmutableHashedArraySetBuilder<E> with(E e1, E e2, E e3) {
        ensureCapacity(3);
        m_Elements[m_Size++] = e1;
        m_Elements[m_Size++] = e2;
        m_Elements[m_Size++] = e3;
        return this;
    }

    public ImmutableHashedArraySetBuilder<E> with(E e1, E e2, E e3, E e4) {
        ensureCapacity(4);
        m_Elements[m_Size++] = e1;
        m_Elements[m_Size++] = e2;
        m_Elements[m_Size++] = e3;
        m_Elements[m_Size++] = e4;
        return this;
    }

    @SafeVarargs
    public final ImmutableHashedArraySetBuilder<E> with(E... elements) {
        int len = elements.length;
        ensureCapacity(len);
        System.arraycopy(elements, 0, m_Elements, m_Size, len);
        m_Size += len;
        return this;
    }

    public ImmutableHashedArraySetBuilder<E> merge(ImmutableHashedArraySetBuilder<E> elements) {
        int len = elements.m_Size;
        ensureCapacity(len);
        System.arraycopy(elements.m_Elements, 0, m_Elements, m_Size, len);
        m_Size += len;
        return this;
    }

    private void ensureCapacity(int capacity) {
        if(m_Elements.length - m_Size < capacity) {
            int newLength = m_Size + capacity;
            // round up to divisible by 8
            newLength += (8 - (newLength % 8)) % 8;
            m_Elements = Arrays.copyOf(m_Elements, newLength);
        }
    }

    public int size() {
        return m_Size;
    }

    @SuppressWarnings("unchecked")
    public ImmutableHashedArraySet<E> build() {
        if(m_Size == 0) {
            return ImmutableHashedArraySet.<E>emptySet();
        }

        Object[] elements = Arrays.copyOf(m_Elements, m_Size);
        int[] hashCodes = new int[elements.length];
        for(int i = 0; i < elements.length; i++) {
            hashCodes[i] = Objects.hashCode(elements[i]);
        }

        if (elements.length == 1) {
            return new ImmutableHashedArraySet<E>(elements, hashCodes);
        }

        Set<Object> dups = new HashSet<Object>(m_Size);
        // Scan for and remove any duplicates, using a set to detect duplicates.
        int prev = 0;
        dups.add(elements[0]);

        for (int i = 1; i < elements.length; i++) {
            Object currElem = elements[i];
            if (prev + 1 < i) {
                elements[prev + 1] = currElem;
                hashCodes[prev + 1] = hashCodes[i];
            }
            if (!dups.contains(currElem)) {
                dups.add(currElem);
                prev++;
            }
        }

        // Note: not strictly necessary, defensive copy made on construction
        if(prev + 1 < elements.length) {
            Arrays.fill(elements, prev + 1, elements.length, null);
            Arrays.fill(hashCodes, prev + 1, elements.length, 0);
        }
        return new ImmutableHashedArraySet<E>(elements, hashCodes, 0, prev + 1);
    }

    public ImmutableHashedArraySetBuilder<E> clear() {
        m_Elements = EMPTY_ELEMENTS;
        m_Size = 0;
        return this;
    }
}
