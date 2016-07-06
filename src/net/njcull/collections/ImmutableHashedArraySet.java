package net.njcull.collections;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * A {@link Set} backed by an array of elements, and a separate int array of
 * hash codes. The arrays are the exact length required to contain the elements,
 * and their corresponding hash codes.
 *
 * <p>Elements are tested using a linear search implementation. Searches are
 * performed by testing the hash codes. If the hash codes match, the element is
 * then tested for equality. This allows for a fast, cache-friendly, linear
 * scan of elements in the set.
 * </p>
 * <p>The set may also be view as a {@link List}, which implements the
 * {@link RandomAccess} interface to indicate constant time random access.</p>
 *
 * @param <E> the type of elements maintained by this set and backing list
 * @author run2000
 * @version 3/07/2016.
 */
public final class ImmutableHashedArraySet<E> extends AbstractSet<E> implements ArrayBackedSet<E> {

    private final Object[] m_Elements;
    private final int[] m_HashCodes;

    private static final ImmutableHashedArraySet<?> EMPTY = new ImmutableHashedArraySet<>(new Object[0], new int[0]);

    /**
     * Returns an immutable empty array set. Each call to this method will return
     * the same empty set.
     *
     * @param <E> the type of elements maintained by this set and backing list
     * @return an immutable empty array set
     */
    @SuppressWarnings("unchecked")
    public static <E> ImmutableHashedArraySet<E> emptySet() {
        return (ImmutableHashedArraySet<E>) EMPTY;
    }

    ImmutableHashedArraySet(Object[] elements, int[] hashCodes) {
        Objects.requireNonNull(elements, "elements must be non-null");
        Objects.requireNonNull(hashCodes, "hash codes must be non-null");
        if(elements.length != hashCodes.length) {
            throw new IllegalArgumentException("Element size must match hash code size");
        }
        this.m_Elements = Arrays.copyOf(elements, elements.length);
        this.m_HashCodes = Arrays.copyOf(hashCodes, hashCodes.length);
    }

    ImmutableHashedArraySet(Object[] elements, int[] hashCodes, int start, int end) {
        Objects.requireNonNull(elements, "elements must be non-null");
        Objects.requireNonNull(hashCodes, "hash codes must be non-null");
        if(elements.length != hashCodes.length) {
            throw new IllegalArgumentException("Element size must match hash code size");
        }
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
        this.m_HashCodes = Arrays.copyOfRange(hashCodes, start, end);
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
        throw new UnsupportedOperationException("No removals");
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
            throw new UnsupportedOperationException("No removals");
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

    public int indexOf(Object element) {
        final int size = m_Elements.length;
        final int hc = Objects.hashCode(element);

        for(int i = 0; i < size; i++) {
            if(hc == m_HashCodes[i]) {
                if (element == m_Elements[i]) {
                    return i;
                } else if ((element != null) && (element.equals(m_Elements[i]))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int indexOfRange(E element, int fromIndex, int toIndex) {
        if(fromIndex < 0 || fromIndex >= size()) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex);
        }
        if(toIndex < fromIndex || toIndex > size()) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex);
        }

        final int hc = Objects.hashCode(element);
        for (int i = fromIndex; i < toIndex; i++) {
            if(hc == m_HashCodes[i]) {
                if (element == m_Elements[i]) {
                    return i;
                } else if ((element != null) && (element.equals(m_Elements[i]))) {
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
     * Creates a {@code Spliterator} over the elements in this set.
     *
     * <p>The {@code Spliterator} reports {@code Spliterator.DISTINCT},
     * {@code Spliterator.IMMUTABLE}, and {@code Spliterator.CONCURRENT}.
     *
     * @return a {@code Spliterator} over the elements in this set
     */
    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.CONCURRENT);
    }

    @Override
    public List<E> asList() {
        return Views.listView(this);
    }

    /**
     * Create a builder object for this immutable hashed array set.
     *
     * @param <E> the type of the resulting array set
     * @return a new builder object
     */
    public static <E> ImmutableHashedArraySetBuilder<E> builder() {
        return new ImmutableHashedArraySetBuilder<E>();
    }
}
