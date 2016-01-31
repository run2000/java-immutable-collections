package net.njcull.collections;

import java.util.*;

/**
 * Builder for the {@link ImmutableSortedArraySet} class.
 *
 * @author run2000
 * @version 4/01/2016.
 */
public final class ImmutableSortedArraySetBuilder<E> {
    private Comparator<? super E> m_Comparator;
    private Object[] m_Elements = EMPTY_ELEMENTS;
    private int m_Size = 0;

    private static final Object[] EMPTY_ELEMENTS = new Object[0];
    private static final Comparator<Comparable> naturalOrder = Comparator.nullsFirst(Comparator.<Comparable>naturalOrder());

    public static <E> ImmutableSortedArraySetBuilder<E> newComparing(Comparator<? super E> cmp) {
        ImmutableSortedArraySetBuilder<E> builder = new ImmutableSortedArraySetBuilder<>();
        return builder.byComparing(cmp);
    }

    public ImmutableSortedArraySetBuilder() {
    }

    public ImmutableSortedArraySetBuilder<E> byComparing(Comparator<? super E> cmp) {
        this.m_Comparator = cmp;
        return this;
    }

    public ImmutableSortedArraySetBuilder<E> byNaturalOrder() {
        this.m_Comparator = null;
        return this;
    }

    public ImmutableSortedArraySetBuilder<E> with(Iterable<? extends E> it) {
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
    public ImmutableSortedArraySetBuilder<E> with(Collection<? extends E> coll) {
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

    public ImmutableSortedArraySetBuilder<E> with(E elem) {
        ensureCapacity(1);
        m_Elements[m_Size++] = elem;
        return this;
    }

    public ImmutableSortedArraySetBuilder<E> with(E e1, E e2) {
        ensureCapacity(2);
        m_Elements[m_Size++] = e1;
        m_Elements[m_Size++] = e2;
        return this;
    }

    public ImmutableSortedArraySetBuilder<E> with(E e1, E e2, E e3) {
        ensureCapacity(3);
        m_Elements[m_Size++] = e1;
        m_Elements[m_Size++] = e2;
        m_Elements[m_Size++] = e3;
        return this;
    }

    public ImmutableSortedArraySetBuilder<E> with(E e1, E e2, E e3, E e4) {
        ensureCapacity(4);
        m_Elements[m_Size++] = e1;
        m_Elements[m_Size++] = e2;
        m_Elements[m_Size++] = e3;
        m_Elements[m_Size++] = e4;
        return this;
    }

    @SafeVarargs
    public final ImmutableSortedArraySetBuilder<E> with(E... elements) {
        int len = elements.length;
        ensureCapacity(len);
        System.arraycopy(elements, 0, m_Elements, m_Size, len);
        m_Size += len;
        return this;
    }

    public ImmutableSortedArraySetBuilder<E> merge(ImmutableSortedArraySetBuilder<E> elements) {
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
    public ImmutableSortedArraySet<E> build() {
        if(m_Size == 0) {
            return ImmutableSortedArraySet.<E>emptySet();
        }

        Object[] elements = Arrays.copyOf(m_Elements, m_Size);
        Comparator<? super E> comparator = m_Comparator;

        if (elements.length == 1) {
            return new ImmutableSortedArraySet<E>(elements, comparator);
        }

        Comparator nullsComparator = (comparator == null) ? naturalOrder : Comparator.nullsFirst(comparator);
        Arrays.sort(elements, nullsComparator);

        // Scan for and remove any duplicates, using the comparator.
        int prev = 0;
        for (int i = 1; i < elements.length; i++) {
            Object currElem = elements[i];
            if (prev + 1 < i) {
                elements[prev + 1] = currElem;
            }
            Object prevElem = elements[prev];
            int cmp = nullsComparator.compare(currElem, prevElem);
            if (cmp > 0) {
                prev++;
            }
        }

        // Note: not strictly necessary, defensive copy made on construction
        if(prev + 1 < elements.length) {
            Arrays.fill(elements, prev + 1, elements.length, null);
        }
        return new ImmutableSortedArraySet<E>(elements, 0, prev + 1, comparator);
    }

    public ImmutableSortedArraySetBuilder<E> clear() {
        m_Comparator = null;
        m_Elements = EMPTY_ELEMENTS;
        m_Size = 0;
        return this;
    }
}
