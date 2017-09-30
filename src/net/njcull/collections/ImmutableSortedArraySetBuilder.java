package net.njcull.collections;

import java.util.*;

/**
 * Builder for the {@link ImmutableSortedArraySet} class.
 *
 * @param <E> the type of elements maintained by the resulting set
 * @author run2000
 * @version 4/01/2016.
 */
public final class ImmutableSortedArraySetBuilder<E> {
    private Comparator<? super E> m_Comparator;
    private Object[] m_Elements = EMPTY_ELEMENTS;
    private int m_Size = 0;

    private static final Object[] EMPTY_ELEMENTS = new Object[0];
    @SuppressWarnings("unchecked")
    private static final Comparator<Comparable> naturalOrder = Comparator.nullsFirst(Comparator.<Comparable>naturalOrder());

    /**
     * Create a new builder instance that builds a new immutable sorted set
     * using the supplied comparator for sorting the elements.
     *
     * @param <E> the element type of the set
     * @param cmp the comparator for sorting the set
     * @return a new builder for building a new set
     */
    public static <E> ImmutableSortedArraySetBuilder<E> newComparing(Comparator<? super E> cmp) {
        ImmutableSortedArraySetBuilder<E> builder = new ImmutableSortedArraySetBuilder<>();
        return builder.byComparing(cmp);
    }

    /**
     * Create a new builder instance for constructing a new immutable
     * sorted array set.
     */
    public ImmutableSortedArraySetBuilder() {
    }

    /**
     * Use the supplied comparator to sort the elements in the resulting set.
     *
     * @param cmp the comparator for sorting the elements in the set
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArraySetBuilder<E> byComparing(Comparator<? super E> cmp) {
        this.m_Comparator = cmp;
        return this;
    }

    /**
     * Sort the elements for the resulting set using the elements' natural order.
     *
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArraySetBuilder<E> byNaturalOrder() {
        this.m_Comparator = null;
        return this;
    }

    /**
     * All the elements from the supplied iterable will be added to the
     * resulting set.
     *
     * @param it the iterable containing elements to be added
     * @return this builder, for chaining purposes
     */
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

    /**
     * All the elements from the supplied collection will be added to the
     * resulting set.
     *
     * @param coll the collection containing elements to be added
     * @return this builder, for chaining purposes
     */
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

    /**
     * Add the given element to the resulting set.
     *
     * @param elem the element to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArraySetBuilder<E> with(E elem) {
        ensureCapacity(1);
        m_Elements[m_Size++] = elem;
        return this;
    }

    /**
     * Add the given elements to the resulting set.
     *
     * @param e1 the first element to be added
     * @param e2 the second element to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArraySetBuilder<E> with(E e1, E e2) {
        ensureCapacity(2);
        m_Elements[m_Size++] = e1;
        m_Elements[m_Size++] = e2;
        return this;
    }

    /**
     * Add the given elements to the resulting set.
     *
     * @param e1 the first element to be added
     * @param e2 the second element to be added
     * @param e3 the third element to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArraySetBuilder<E> with(E e1, E e2, E e3) {
        ensureCapacity(3);
        m_Elements[m_Size++] = e1;
        m_Elements[m_Size++] = e2;
        m_Elements[m_Size++] = e3;
        return this;
    }

    /**
     * Add the given elements to the resulting set.
     *
     * @param e1 the first element to be added
     * @param e2 the second element to be added
     * @param e3 the third element to be added
     * @param e4 the fourth element to be added
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArraySetBuilder<E> with(E e1, E e2, E e3, E e4) {
        ensureCapacity(4);
        m_Elements[m_Size++] = e1;
        m_Elements[m_Size++] = e2;
        m_Elements[m_Size++] = e3;
        m_Elements[m_Size++] = e4;
        return this;
    }

    /**
     * Add the given elements to the resulting set.
     *
     * @param elements the elements to be added
     * @return this builder, for chaining purposes
     */
    @SafeVarargs
    public final ImmutableSortedArraySetBuilder<E> with(E... elements) {
        int len = elements.length;
        ensureCapacity(len);
        System.arraycopy(elements, 0, m_Elements, m_Size, len);
        m_Size += len;
        return this;
    }

    /**
     * For the stream combiner, merge the elements from the supplied builder
     * to this builder.
     *
     * @param elements the builder containing the elements to be merged into
     * this builder
     * @return this builder containing the merged items
     */
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

    /**
     * Returns the number of elements in this builder.
     *
     * @return the number of elements in this builder
     */
    public int size() {
        return m_Size;
    }

    /**
     * Build the immutable set. Validates all elements added, including
     * sorting the elements, and checking and removing duplicate elements as
     * necessary.
     * <p>
     * Repeated calls to a builder containing a non-zero number of elements
     * will return distinct set instances.
     *
     * @return an ImmutableSortedArraySet containing the elements in the builder
     */
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

    /**
     * Reset this builder to its initial state.
     *
     * @return this builder, for chaining purposes
     */
    public ImmutableSortedArraySetBuilder<E> clear() {
        m_Comparator = null;
        m_Elements = EMPTY_ELEMENTS;
        m_Size = 0;
        return this;
    }
}
