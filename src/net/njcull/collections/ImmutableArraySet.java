package net.njcull.collections;

import java.util.*;

/**
 * A {@link Set} backed by an array of elements. The array is the
 * exact length required to contain the elements. Elements are tested using
 * a linear search implementation. The set may also be view as a {@link List},
 * which implements the {@link RandomAccess} interface to indicate constant time
 * random access.
 *
 * @param <E> the type of elements maintained by this set and backing list
 * @author run2000
 * @version 9/01/2016.
 */
public final class ImmutableArraySet<E> extends AbstractSet<E> implements ArrayBackedSet<E> {

    private final Object[] m_Elements;

    private static final ImmutableArraySet<?> EMPTY = new ImmutableArraySet<>(new Object[0]);

    /**
     * Returns an immutable empty array set. Each call to this method will return
     * the same empty set.
     *
     * @param <E> the type of elements maintained by this set and backing list
     * @return an immutable empty array set
     */
    @SuppressWarnings("unchecked")
    public static <E> ImmutableArraySet<E> emptySet() {
        return (ImmutableArraySet<E>) EMPTY;
    }

    ImmutableArraySet(Object[] elements) {
        Objects.requireNonNull(elements, "elements must be non-null");
        this.m_Elements = Arrays.copyOf(elements, elements.length);
    }

    ImmutableArraySet(Object[] elements, int start, int end) {
        Objects.requireNonNull(elements, "elements must be non-null");
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
    }

    /**
     * Adds all of the elements in the specified collection to this collection.
     * (optional operation).
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
     * Returns {@code true} if this set contains the specified element.
     * More formally, returns {@code true} if and only if this set
     * contains an element {@code e} such that
     * {@code (o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))}.
     *
     * @param element the element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element,
     * otherwise {@code false}
     */
    @Override
    public boolean contains(Object element) {
        return indexOf(element) >= 0;
    }

    @SuppressWarnings("unchecked")
    public E getAtIndex(int index) {
        if((index < 0) || (index >= m_Elements.length)) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        return (E)m_Elements[index];
    }

    @Override
    public int indexOf(Object element) {
        final int size = m_Elements.length;
        if(element == null) {
            for(int i = 0; i < size; i++) {
                if(element == m_Elements[i]) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element.equals(m_Elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int indexOfRange(E element, int fromIndex, int toIndex) {
        if(fromIndex < 0 || fromIndex >= size()) {
            throw new IndexOutOfBoundsException("fromIndex: "+ fromIndex);
        }
        if(toIndex < fromIndex || toIndex > size()) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex);
        }
        if(element == null) {
            for (int i = fromIndex; i < toIndex; i++) {
                if(element == m_Elements[i]) {
                    return i;
                }
            }
        } else {
            for (int i = fromIndex; i < toIndex; i++) {
                if(element.equals(m_Elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayBackedCollectionIterator<>(this);
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
     * Returns the number of elements in this set (its cardinality).
     *
     * @return the number of elements in this set (its cardinality)
     */
    @Override
    public int size() {
        return m_Elements.length;
    }

    /**
     * Returns an array containing all of the elements in this set.
     * If this set makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the
     * elements in the same order.
     *
     * <p>The returned array will be "safe" in that no references to it
     * are maintained by this set.  (In other words, this method must
     * allocate a new array even if this set is backed by an array).
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

    /**
     * Creates a {@code Spliterator} over the elements in this set.
     *
     * <p>The {@code Spliterator} reports {@code Spliterator.DISTINCT},
     * {@code Spliterator.ORDERED}, {@code Spliterator.IMMUTABLE},
     * {@code Spliterator.SIZED}, and {@code Spliterator.SUBSIZED}.
     *
     * @return a {@code Spliterator} over the elements in this set
     */
    @Override
    public Spliterator<E> spliterator() {
        return new ImmutableIndexerSpliterator<E>(this::getAtIndex, size(), Spliterator.DISTINCT);
    }

    @Override
    public List<E> asList() {
        return Views.listView(this);
    }

    /**
     * Create a builder object for this immutable array set.
     *
     * @param <E> the type of the resulting array set
     * @return a new builder object
     */
    public static <E> ImmutableArraySetBuilder<E> builder() {
        return new ImmutableArraySetBuilder<E>();
    }
}
