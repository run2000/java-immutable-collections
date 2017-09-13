package net.njcull.collections;

import java.util.*;
import java.util.function.Predicate;

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

    /**
     * Adds all of the elements in the specified collection to this collection.
     *
     * @param c collection containing elements to be added to this collection
     * @return {@code false} this collection is not changed as a result of the call
     * @throws UnsupportedOperationException the {@code addAll} operation
     *         is not supported by this collection
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(!c.isEmpty()) {
            // Note: this is the same behaviour as AbstractCollection and
            // AbstractSet. Even if all elements already exist in the set,
            // and false would be returned, an exception is thrown anyway.
            throw new UnsupportedOperationException("No adding");
        }
        return false;
    }

    /**
     * Retains only the elements in this set that are contained in the
     * specified collection (optional operation).
     *
     * @param  c collection containing elements to be retained in this set
     * @return {@code false} this set is not changed as a result of the call
     * @throws UnsupportedOperationException the retainAll operation
     *         is not supported by this set
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        for(int i = 0; i < m_Elements.length; i++) {
            if(!c.contains(m_Elements[i])) {
                throw new UnsupportedOperationException("No removals");
            }
        }
        return false;
    }

    /**
     * Removes from this set all of its elements that are contained in the
     * specified collection (optional operation).  If the specified
     * collection is also a set, this operation effectively modifies this
     * set so that its value is the <i>asymmetric set difference</i> of
     * the two sets.
     *
     * @param  c collection containing elements to be removed from this set
     * @return {@code false} this set is not changed as a result of the call
     * @throws UnsupportedOperationException the {@code removeAll} operation
     *         is not supported by this set
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        if(!c.isEmpty()) {
            for(int i = 0; i < m_Elements.length; i++) {
                if(c.contains(m_Elements[i])) {
                    throw new UnsupportedOperationException("No removals");
                }
            }
        }
        return false;
    }

    /**
     * Removes all of the elements of this collection that satisfy the given
     * predicate.  Errors or runtime exceptions thrown during iteration or by
     * the predicate are relayed to the caller.
     *
     * @param filter a predicate which returns {@code true} for elements to be
     *        removed
     * @return {@code false} no elements were removed
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException elements cannot be removed
     *         from this collection.
     */
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);

        for (int i = m_Elements.length - 1; i >= 0; i--) {
            if (filter.test(getAtIndex(i))) {
                throw new UnsupportedOperationException("No removals");
            }
        }
        return false;
    }

    /**
     * Removes all of the elements from this set (optional operation).
     * The set will be empty after this call returns.
     *
     * @throws UnsupportedOperationException the {@code clear} method
     *         is not supported by this set
     */
    @Override
    public void clear() {
        if(m_Elements.length > 0) {
            throw new UnsupportedOperationException("No removals");
        }
    }

    /**
     * Returns the number of elements in this set (its cardinality).
     *
     * @return the number of elements in this set (its cardinality)
     */
    @Override
    public int size() {
        return m_Elements.length;
    }

    /**
     * Returns an iterator over the elements in this set.  The elements are
     * returned in the order in which they were added.
     *
     * @return an iterator over the elements in this set
     */
    @Override
    public boolean isEmpty() {
        return m_Elements.length == 0;
    }

    /**
     * Returns {@code true} if this set contains the specified element.
     * More formally, returns {@code true} if and only if this set
     * contains an element {@code e} such that
     * {@code (o==null ? e==null : o.equals(e))}.
     *
     * @param element the element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element,
     * otherwise {@code false}
     */
    @Override
    public boolean contains(Object element) {
        int idx = indexOf(element);
        return idx >= 0;
    }

    /**
     * Get the element at the specified array index.
     *
     * @param index the index of the item to be retrieved
     * @return the item at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index &lt; 0 || index &gt;= size()})
     */
    @SuppressWarnings("unchecked")
    public E getAtIndex(int index) {
        if(index < 0 || index > m_Elements.length) {
            throw new IndexOutOfBoundsException("index out of bounds: " + index);
        }
        return (E)m_Elements[index];
    }

    /**
     * Determine the index of the given element, if it exists in this
     * collection.
     *
     * @param element the element to be found
     * @return a zero or positive integer if the element is in the
     * backing array, otherwise less than zero to indicate its absence
     */
    @SuppressWarnings("unchecked")
    public int indexOf(Object element) {
        try {
            int idx = indexOfInternal((E)element, 0, m_Elements.length);
            return idx >= 0 ? idx : -1;
        } catch (ClassCastException e) {
            return -1;
        }
    }

    /**
     * Determine the index of the given element, if it exists within the
     * specified range in this set.
     *
     * @param element the element to be found
     * @param fromIndex the start index, must be zero or greater
     * @param toIndex the exclusive end index, must be greater than or equal to
     *                the start index
     * @return a zero or positive integer if the element is in the specified
     * range of the backing array, otherwise less than zero to indicate
     * its absence
     * @throws IndexOutOfBoundsException if fromIndex or toIndex is out of range
     *         ({@code index < 0 || index >= size()}), or toIndex
     *         is less than fromIndex
     */
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

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayBackedCollectionIterator<>(this);
    }

    /**
     * Returns an array containing all of the elements in this set.
     * If this set makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the
     * elements in the same order.
     *
     * <p>The returned array will be "safe" in that no references to it
     * are maintained by this set.  In other words, this method must
     * allocate a new array even if this set is backed by an array.
     * The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all the elements in this set
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(m_Elements, m_Elements.length);
    }

    /**
     * Returns an array containing all of the elements in this set
     * in proper sequence (from first to last element); the runtime type of the
     * returned array is that of the specified array.  If the set fits
     * in the specified array, it is returned therein.  Otherwise, a new array
     * is allocated with the runtime type of the specified array and the size
     * of this set.
     *
     * <p>If the set fits in the specified array with room to spare
     * (i.e., the array has more elements than the set), the element in
     * the array immediately following the end of the set is set to
     * {@code null}.  This is useful in determining the length of the
     * set <em>only</em> if the caller knows that the set does
     * not contain any null elements.
     *
     * <p>This implementation is handled as a static method rather than a
     * default method because a default method cannot override any method
     * of {@code Object}.
     *
     * @param a the array into which the elements of this set are to be
     *        stored, if it is big enough; otherwise, a new array of the same
     *        runtime type is allocated for this purpose.
     * @return an array containing all the elements in this set
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in this
     *         set
     * @throws NullPointerException if the specified array is null
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        final int sz = m_Elements.length;
        if (a.length < sz) {
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(m_Elements, sz, a.getClass());
        }
        System.arraycopy(m_Elements, 0, a, 0, sz);
        if (a.length > sz) {
            a[sz] = null;
        }
        return a;
    }

    /**
     * Returns the comparator used to order the elements in this set,
     * or {@code null} if this set uses the Comparable natural ordering}
     * of its elements.
     *
     * @return the comparator used to order the elements in this set,
     *         or {@code null} if this set uses the natural ordering
     *         of its elements
     */
    @Override
    public Comparator<? super E> comparator() {
        return m_Comparator;
    }

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    @SuppressWarnings("unchecked")
    public E first() {
        if(m_Elements.length == 0) {
            throw new NoSuchElementException("Empty set");
        }
        return (E)m_Elements[0];
    }

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    @SuppressWarnings("unchecked")
    public E last() {
        if(m_Elements.length == 0) {
            throw new NoSuchElementException("Empty set");
        }
        return (E)m_Elements[m_Elements.length - 1];
    }

    /**
     * Returns a view of the portion of this set whose elements are
     * strictly less than {@code toElement}.  The returned set is
     * backed by this set.  The returned set supports all optional
     * set operations that this set supports.
     *
     * @param toElement high endpoint (exclusive) of the returned set
     * @return a view of the portion of this set whose elements are strictly
     *         less than {@code toElement}
     * @throws ClassCastException if {@code toElement} is not compatible
     *         with this set's comparator (or, if the set has no comparator,
     *         if {@code toElement} does not implement {@code Comparable}).
     *         Implementations may, but are not required to, throw this
     *         exception if {@code toElement} cannot be compared to elements
     *         currently in the set.
     * @throws NullPointerException if {@code toElement} is null and
     *         this set does not permit null elements
     * @throws IllegalArgumentException if this set itself has a
     *         restricted range, and {@code toElement} lies outside the
     *         bounds of the range
     */
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

    /**
     * Returns a view of the portion of this set whose elements are
     * greater than or equal to {@code fromElement}.  The returned
     * set is backed by this set.  The returned set supports all optional
     * set operations that this set supports.
     *
     * @param fromElement low endpoint (inclusive) of the returned set
     * @return a view of the portion of this set whose elements are greater
     *         than or equal to {@code fromElement}
     * @throws ClassCastException if {@code fromElement} is not compatible
     *         with this set's comparator (or, if the set has no comparator,
     *         if {@code fromElement} does not implement {@code Comparable}).
     *         Implementations may, but are not required to, throw this
     *         exception if {@code fromElement} cannot be compared to elements
     *         currently in the set.
     * @throws NullPointerException if {@code fromElement} is null
     *         and this set does not permit null elements
     * @throws IllegalArgumentException if this set itself has a
     *         restricted range, and {@code fromElement} lies outside the
     *         bounds of the range
     */
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

    /**
     * Returns a view of the portion of this set whose elements range
     * from {@code fromElement}, inclusive, to {@code toElement},
     * exclusive.  If {@code fromElement} and {@code toElement} are
     * equal, the returned set is empty.  The returned set is backed
     * by this set.  The returned set supports all optional set operations
     * that this set supports.
     *
     * @param fromElement low endpoint (inclusive) of the returned set
     * @param toElement high endpoint (exclusive) of the returned set
     * @return a view of the portion of this set whose elements range from
     *         <tt>fromElement</tt>, inclusive, to <tt>toElement</tt>, exclusive
     * @throws ClassCastException if <tt>fromElement</tt> and
     *         <tt>toElement</tt> cannot be compared to one another using this
     *         set's comparator (or, if the set has no comparator, using
     *         natural ordering).  Implementations may, but are not required
     *         to, throw this exception if <tt>fromElement</tt> or
     *         <tt>toElement</tt> cannot be compared to elements currently in
     *         the set.
     * @throws NullPointerException if <tt>fromElement</tt> or
     *         <tt>toElement</tt> is null and this set does not permit null
     *         elements
     * @throws IllegalArgumentException if <tt>fromElement</tt> is
     *         greater than <tt>toElement</tt>; or if this set itself
     *         has a restricted range, and <tt>fromElement</tt> or
     *         <tt>toElement</tt> lies outside the bounds of the range
     */
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

    /**
     * Creates a {@code Spliterator} over the elements in this set.
     *
     * <p>The {@code Spliterator} reports {@code Spliterator.DISTINCT},
     * {@code Spliterator.ORDERED}, {@code Spliterator.IMMUTABLE},
     * {@code Spliterator.SIZED}, and {@code Spliterator.SUBSIZED},
     * and {@code Spliterator.SORTED}.
     *
     * @return a {@code Spliterator} over the elements in this set
     */
    @SuppressWarnings("unchecked")
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

    /**
     * Returns the hash code value for the given set. The hash code of a set is
     * defined to be the sum of the hash codes of the elements in the set,
     * where the hash code of a {@code null} element is defined to be zero.
     * This ensures that {@code s1.equals(s2)} implies that
     * {@code s1.hashCode()==s2.hashCode()} for any two sets {@code s1}
     * and {@code s2}, as required by the general contract of
     * {@link Object#hashCode}.
     * <p>
     * This implementation is handled as a static method rather than a
     * default method because a default method cannot override any methods
     * of {@code Object}.
     *
     * @return the hash code value for the given set
     */
    @Override
    public int hashCode() {
        return ArrayBackedSet.hashCode(this);
    }

    /**
     * Return a backing list view for this collection.
     *
     * @return a list view containing all the elements of this {@code Set}
     */
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
