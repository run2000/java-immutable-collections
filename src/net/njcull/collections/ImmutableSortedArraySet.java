package net.njcull.collections;

import java.util.*;

/**
 * A {@link SortedSet} backed by an array of elements. The array is the
 * exact length required to contain the elements. Elements are tested using
 * a binary search implementation. The set may also be view as a {@link List},
 * which implements the {@link RandomAccess} interface to indicate constant time
 * random access.
 *
 * @param <E> the type of elements maintained by this set
 * @author run2000
 * @version 4/01/2016.
 */
public final class ImmutableSortedArraySet<E> extends AbstractSet<E> implements SortedSet<E>, ArrayBackedSet<E> {
    private final Object[] m_Elements;
    private final Comparator<? super E> m_Comparator;
    private final Comparator m_NullsComparator;

    private static final ImmutableSortedArraySet<?> EMPTY = new ImmutableSortedArraySet<>(new Object[0], null);

    @SuppressWarnings("unchecked")
    public static <E> ImmutableSortedArraySet<E> emptySet() {
        return (ImmutableSortedArraySet<E>) EMPTY;
    }

    ImmutableSortedArraySet(Object[] elements, Comparator<? super E> comparator) {
        Objects.requireNonNull(elements, "Elements cannot be null");
        this.m_Elements = Arrays.copyOf(elements, elements.length);
        this.m_Comparator = comparator;
        this.m_NullsComparator = (comparator == null) ?
                Comparator.nullsFirst(Comparator.naturalOrder()) :
                Comparator.nullsFirst(comparator);
    }

    ImmutableSortedArraySet(Object[] elements, int start, int end, Comparator<? super E> comparator) {
        Objects.requireNonNull(elements, "Elements cannot be null");
        if(start < 0) {
            throw new IllegalArgumentException("Start should be 0 or positive");
        }
        if(end < 0) {
            throw new IllegalArgumentException("End should be 0 or positive");
        }
        if(end > elements.length) {
            throw new IllegalArgumentException("End is greater than array length");
        }
        if(start > end) {
            throw new IllegalArgumentException("Start is greater than end");
        }
        this.m_Elements = Arrays.copyOfRange(elements, start, end);
        this.m_Comparator = comparator;
        this.m_NullsComparator = (comparator == null) ?
                Comparator.nullsFirst(Comparator.naturalOrder()) :
                Comparator.nullsFirst(comparator);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(!c.isEmpty()) {
            throw new UnsupportedOperationException("No adding");
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("No removals");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if(!c.isEmpty()) {
            throw new UnsupportedOperationException("No removals");
        }
        return false;
    }

    @Override
    public void clear() {
        if(m_Elements.length > 0) {
            throw new UnsupportedOperationException("No removals");
        }
    }

    @Override
    public int size() {
        return m_Elements.length;
    }

    @Override
    public boolean isEmpty() {
        return m_Elements.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        int idx = indexOf(o);
        return idx >= 0;
    }

    @SuppressWarnings("unchecked")
    public E getAtIndex(int index) {
        if(index < 0 || index > m_Elements.length) {
            throw new IndexOutOfBoundsException("index out of bounds: " + index);
        }
        return (E)m_Elements[index];
    }

    @SuppressWarnings("unchecked")
    public int indexOf(Object o) {
        try {
            int idx = indexOfInternal((E)o, 0, m_Elements.length);
            return idx >= 0 ? idx : -1;
        } catch (ClassCastException e) {
            return -1;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public int indexOfRange(E element, int fromIndex, int toIndex) {
        if(fromIndex < 0 || fromIndex >= size()) {
            throw new IndexOutOfBoundsException("fromIndex: "+ fromIndex);
        }
        if(toIndex < fromIndex || toIndex > size()) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex);
        }
        int idx = indexOfInternal(element, fromIndex, toIndex);
        return idx >= 0 ? idx : -1;
    }

    @SuppressWarnings("unchecked")
    private int indexOfInternal(E element, int fromIndex, int toIndex) {
        int idx = BinarySearchUtils.indexedSearch(this::getAtIndex, fromIndex, toIndex, element, m_NullsComparator);
        return idx;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayBackedCollectionIterator<>(this);
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(m_Elements, m_Elements.length);
    }

    @Override
    public Comparator<? super E> comparator() {
        return m_Comparator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E first() {
        if(m_Elements.length == 0) {
            throw new NoSuchElementException("Empty set");
        }
        return (E)m_Elements[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E last() {
        if(m_Elements.length == 0) {
            throw new NoSuchElementException("Empty set");
        }
        return (E)m_Elements[m_Elements.length - 1];
    }

    @Override
    public ImmutableSortedArraySet<E> headSet(E toElement) {
        int lastIndex = indexOfInternal(toElement, 0, m_Elements.length);
        if(lastIndex < 0) {
            lastIndex = 0 - (lastIndex + 1);
        }
        if(lastIndex == 0) {
            return emptySet();
        }
        if(lastIndex == m_Elements.length) {
            return this;
        }
        return new ImmutableSortedArraySet<E>(m_Elements, 0, lastIndex, m_Comparator);
    }

    @Override
    public ImmutableSortedArraySet<E> tailSet(E fromElement) {
        int firstIndex = indexOfInternal(fromElement, 0, m_Elements.length);
        if(firstIndex < 0) {
            firstIndex = 0 - (firstIndex + 1);
        }
        if(firstIndex == m_Elements.length) {
            return emptySet();
        }
        if(firstIndex == 0) {
            return this;
        }
        return new ImmutableSortedArraySet<E>(m_Elements, firstIndex, m_Elements.length, m_Comparator);
    }

    @Override
    public ImmutableSortedArraySet<E> subSet(E fromElement, E toElement) {
        int firstIndex = indexOfInternal(fromElement, 0, m_Elements.length);
        if(firstIndex < 0) {
            firstIndex = 0 - (firstIndex + 1);
        }
        int lastIndex = indexOfInternal(toElement, 0, m_Elements.length);
        if(lastIndex < 0) {
            lastIndex = 0 - (lastIndex + 1);
        }
        if(firstIndex >= lastIndex) {
            return emptySet();
        }
        if((firstIndex == 0) && (lastIndex == m_Elements.length)) {
            return this;
        }
        return new ImmutableSortedArraySet<E>(m_Elements, firstIndex, lastIndex, m_Comparator);
    }

    @Override
    public Spliterator<E> spliterator() {
        return new ImmutableIndexerSpliterator<E>(this::getAtIndex, 0, size(),
                m_NullsComparator,
                Spliterator.DISTINCT | Spliterator.SORTED);
    }

    /**
     * Returns a string representation of this set.  The string
     * representation consists of a list of the set's elements in the
     * order they are returned by its indexer, enclosed in square brackets
     * (<tt>"[]"</tt>).  Adjacent elements are separated by the characters
     * <tt>", "</tt> (comma and space).  Elements are converted to strings as
     * by {@link String#valueOf(Object)}.
     *
     * @return a string representation of this set
     */
    @Override
    public String toString() {
        return ArrayBackedCollection.toString(this);
    }

    @Override
    public int hashCode() {
        return ArrayBackedSet.hashCode(this);
    }

    public List<E> asList() {
        return Views.listView(this);
    }

    /**
     * Create a builder object for this immutable sorted array set.
     *
     * @param <E> the type of the resulting array set
     * @return a new builder object
     */
    public static <E> ImmutableSortedArraySetBuilder<E> builder() {
        return new ImmutableSortedArraySetBuilder<E>();
    }
}
