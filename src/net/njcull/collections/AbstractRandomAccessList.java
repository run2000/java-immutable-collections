package net.njcull.collections;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Extends {@link java.util.AbstractList} with a sane {@link #equals(Object)}
 * operation, and brings in the {@link RandomAccess} interface. Implementations
 * are optimized for a random access pattern, in preference to using
 * {@code Iterator} objects.
 * <p>
 * This class provides a skeletal implementation of the {@link List}
 * interface to minimize the effort required to implement this interface
 * backed by a "random access" data store (such as an array).  For sequential
 * access data (such as a linked list), {@link AbstractSequentialList} should
 * be used in preference to this class.
 * </p>
 *
 * @param <E> the type of elements maintained by this list
 * @author run2000
 * @version 8/01/2016.
 */
public abstract class AbstractRandomAccessList<E> extends AbstractList<E> implements RandomAccess {

    /**
     * Sole constructor.  For invocation by subclass constructors, typically
     * implicit.
     */
    protected AbstractRandomAccessList() {
    }

    /**
     * Returns {@code true} if this list contains the specified element.
     * More formally, returns {@code true} if and only if this list
     * contains at least one element {@code e} such that
     * {@code (o==null ? e==null : o.equals(e))}.
     *
     * @param o element whose presence in this list is to be tested
     * @return <tt>true</tt> if this list contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *
     * <p>This implementation performs a linear search, and indexes directly
     * using the getter method.</p>
     *
     * @param o element to search for
     * @return the index of the first occurrence of the specified element in
     *         this list, or -1 if this list does not contain the element
     * @throws ClassCastException if the type of the specified element
     *         is incompatible with this list
     * @throws NullPointerException if the specified element is null and this
     *         list does not permit null elements
     */
    @Override
    public int indexOf(Object o) {
        final int size = size();
        if (o == null) {
            for(int i = 0; i < size; i++) {
                if(o == get(i)) {
                    return i;
                }
            }
        } else {
            for(int i = 0; i < size; i++) {
                if (o.equals(get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * <p>This implementation returns a straightforward implementation of the
     * iterator interface, relying on the backing list's {@code size()},
     * {@code get(int)}, and {@code remove(int)} methods.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new RandomAccessIterator<E>(this);
    }

    /**
     * Removes a single instance of the specified element from this
     * list, if it is present.
     *
     * @param o element to search for
     * @return the index of the first occurrence of the specified element in
     *         this list, or -1 if this list does not contain the element
     */
    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx >= 0) {
            remove(idx);
            return true;
        }
        return false;
    }

    /**
     * Removes from this list all of its elements that are contained in the
     * specified collection.
     *
     * @param c collection containing elements to be removed from this list
     * @return {@code true} if this list changed as a result of the call
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;

        if((c instanceof RandomAccess) && (c instanceof List)) {
            List<?> list = (List) c;
            for(int i = 0; i < list.size(); i++) {
                modified |= remove(list.get(i));
            }
        } else {
            for (Object o : c) {
                modified |= remove(o);
            }
        }

        return modified;
    }

    /**
     * Retains only the elements in this list that are contained in the
     * specified collection (optional operation).  In other words, removes
     * from this list all of its elements that are not contained in the
     * specified collection.
     *
     * @param c collection containing elements to be retained in this list
     * @return {@code true} if this list changed as a result of the call
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        for(int i = size() - 1; i >= 0; i--) {
            if (!c.contains(get(i))) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Removes all of the elements of this list that satisfy the given
     * predicate.  Errors or runtime exceptions thrown during iteration or by
     * the predicate are relayed to the caller.
     *
     * @param filter a predicate which returns {@code true} for elements to be
     *        removed
     * @return {@code true} if any elements were removed, otherwise {@code false}
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException elements cannot be removed
     *         from this list.
     */
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean modified = false;

        for (int i = size() - 1; i >= 0; i--) {
            if (filter.test(get(i))) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Performs the given action for each element of the {@code List}
     * until all elements have been processed or the action throws an
     * exception. Actions are performed in the order of the {@code get(int)}
     * method. Exceptions thrown by the action are relayed to the caller.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        final int size = size();

        for (int i = 0; i < size; i++) {
            action.accept(get(i));
        }
    }

    /**
     * <p>Compares the specified object with this list for equality.  Returns
     * {@code true} if and only if the specified object is also a list, both
     * lists have the same size, and all corresponding pairs of elements in
     * the two lists are <em>equal</em>. (Two elements {@code e1} and
     * {@code e2} are <em>equal</em> if {@code (e1==null ? e2==null :
     * e1.equals(e2))}.)  In other words, two lists are defined to be
     * equal if they contain the same elements in the same order.</p>
     *
     * <p>This implementation takes advantage of the fact this is a subclass
     * of a {@code RandomAccess} list. If the other list is a random access
     * list, it walks both lists using the index method rather than a
     * list iterator. It also checks whether the size of each list is
     * identical before checking each element.</p>
     *
     * @param o the object to be compared for equality with this list
     * @return {@code true} if the specified object is equal to this list
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }

        final List<?> other = (List<?>) o;
        final int size = size();
        if(size != other.size()) {
            return false;
        }

        // Note: size() must report MAX_VALUE if the size of the list is
        // greater than MAX_VALUE, but there is no direct way to refer to
        // those overflow elements by index.
        if((other instanceof RandomAccess) && (size < Integer.MAX_VALUE)) {
            for(int i = 0; i < size; i++) {
                E o1 = get(i);
                Object o2 = other.get(i);
                if(!Objects.equals(o1, o2)) {
                    return false;
                }
            }
            return true;
        } else {
            Iterator<E> e1 = this.iterator();
            Iterator<?> e2 = other.iterator();
            while (e1.hasNext() && e2.hasNext()) {
                E o1 = e1.next();
                Object o2 = e2.next();
                if(!Objects.equals(o1, o2)) {
                    return false;
                }
            }
            return !e1.hasNext() && !e2.hasNext();
        }
    }

    /**
     * Returns the hash code value for this list.
     *
     * <p>This implementation calculates the list hash function as required
     * in the documentation for the {@link List#hashCode} method by using
     * an indexer rather than an iterator.</p>
     *
     * @return the hash code value for this list
     */
    @Override
    public int hashCode() {
        final int sz = this.size();
        int hashCode = 1;

        for (int i = 0; i < sz; i++) {
            E e = this.get(i);
            hashCode = 31 * hashCode + Objects.hashCode(e);
        }
        return hashCode;
    }

    /**
     * Returns a string representation of this list.  The string
     * representation consists of a sequence of the list's elements in the
     * order they are returned by its {@code get(int)} method, enclosed in
     * square brackets ({@code "[]"}).  Adjacent elements are separated by
     * the characters {@code ", "} (comma and space).  Elements are converted
     * to strings as by {@link String#valueOf(Object)}.
     *
     * @return a string representation of this list
     */
    @Override
    public String toString() {
        final int sz = this.size();
        if (sz == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < sz; ) {
            E e = this.get(i++);
            sb.append(e == this ? "(this Collection)" : e);
            if (i < sz) {
                sb.append(',').append(' ');
            }
        }
        return sb.append(']').toString();
    }

    /**
     * Returns an array containing all of the elements in the given collection.
     * This method returns the elements in the same order as this list.
     *
     * <p>The returned array will be "safe" in that no references to it
     * are maintained by this list.  In other words, this method
     * allocates a new array even if this list is backed by an array.
     * The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all the elements in this set
     */
    @Override
    public Object[] toArray() {
        final int sz = this.size();
        Object[] arr = new Object[sz];

        for(int i = 0; i < sz; i++) {
            arr[i] = this.get(i);
        }
        return arr;
    }

    /**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element); the runtime type of the returned
     * array is that of the specified array.  If the list fits in the
     * specified array, it is returned therein.  Otherwise, a new array is
     * allocated with the runtime type of the specified array and the size of
     * this list.
     *
     * <p>If the list fits in the specified array with room to spare
     * (i.e., the array has more elements than the list), the element in
     * the array immediately following the end of the list is set to
     * {@code null}.  This is useful in determining the length of the
     * list <em>only</em> if the caller knows that the list does not contain
     * any null elements.
     *
     * @param a the array into which the elements of the list are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of the list
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        final int sz = this.size();
        if (a.length < sz) {
            // Make a new array of a's runtime type, but my contents:
            a = (T[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), sz);
        }
        for(int i = 0; i < sz; i++) {
            a[i] = (T)this.get(i);
        }
        if (a.length > sz) {
            a[sz] = null;
        }
        return a;
    }

    /**
     * An iterator that takes a List, assumed to be a random access list,
     * and iterates over it by index. The size of the list is assumed to be
     * less than {@code Integer.MAX_VALUE}.
     *
     * @param <E> the type of elements in the list
     */
    private static final class RandomAccessIterator<E> implements Iterator<E> {
        private final List<E> m_List;
        private int m_EndIndex;
        private int m_Index;

        /**
         * Create a new {@code Iterator} for the supplied random access list.
         *
         * @param list the list on which the iterator indexes
         */
        RandomAccessIterator(List<E> list) {
            m_List = Objects.requireNonNull(list);
            m_EndIndex = list.size();
            m_Index = 0;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         *
         * @return {@code true} if the iteration has more elements, otherwise
         * {@code false}
         */
        @Override
        public boolean hasNext() {
            return m_Index < m_EndIndex;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public E next() {
            if((m_Index < 0) || (m_Index >= m_EndIndex)) {
                throw new NoSuchElementException("end of iterator");
            }
            return m_List.get(m_Index++);
        }

        /**
         * Removes from the underlying list the last element returned
         * by this iterator (optional operation).
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *         operation is not supported by the backing list
         * @throws IllegalStateException if the {@code next} method has not
         *         yet been called, or the {@code remove} method has already
         *         been called after the last call to the {@code next}
         *         method
         */
        @Override
        public void remove() {
            if((m_Index <= 0) || (m_Index > m_EndIndex)) {
                throw new IllegalStateException("end of iterator");
            }
            m_List.remove(--m_Index);
            m_EndIndex--;
        }
    }
}
