package net.njcull.collections;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Predicate;

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
public final class ImmutableHashedArraySet<E> extends AbstractSet<E>
        implements ArrayBackedSet<E>, Serializable {

    private final Object[] m_Elements;
    private transient int[] m_HashCodes;

    // Singleton, as an optimization only
    private static final ImmutableHashedArraySet<?> EMPTY = new ImmutableHashedArraySet<>(new Object[0], new int[0]);

    /**
     * Returns an immutable empty array set. Each call to this method will return
     * the same empty set.
     *
     * @param <E> the type of elements maintained by this set and backing list
     * @return an immutable empty hashed array set
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
        return indexOf(element) >= 0;
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
        if((index < 0) || (index >= m_Elements.length)) {
            throw new IndexOutOfBoundsException("index: " + index);
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
    @Override
    public int indexOf(Object element) {
        final int size = m_Elements.length;
        final int hc = Objects.hashCode(element);

        for(int i = 0; i < size; i++) {
            if(hc == m_HashCodes[i]) {
                if (Objects.equals(element, m_Elements[i])) {
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
                if (Objects.equals(element, m_Elements[i])) {
                    return i;
                }
            }
        }
        return -1;
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

    /**
     * Deserialization.
     *
     * @param stream the object stream to be deserialized
     * @throws ClassNotFoundException the class or descendants could not be found
     * @throws IOException there was a problem reading the object stream
     */
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        stream.defaultReadObject();

        // Perform validation
        if (m_Elements == null) {
            throw new InvalidObjectException("array set must have elements");
        }
        final int sz = m_Elements.length;

        // Regenerate hashcodes
        m_HashCodes = new int[sz];
        for(int i = 0; i < sz; i++) {
            m_HashCodes[i] = Objects.hashCode((E) m_Elements[i]);
        }
    }

    /**
     * Deserialization.
     *
     * @return the resolved object
     */
    private Object readResolve() {
        if(m_Elements.length == 0) {
            // optimization only
            return EMPTY;
        }
        return this;
    }
}
