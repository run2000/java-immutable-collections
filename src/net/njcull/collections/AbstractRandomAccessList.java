package net.njcull.collections;

import java.util.*;

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
 * @author run2000
 * @version 8/01/2016.
 */
public abstract class AbstractRandomAccessList<E> extends AbstractList<E> implements RandomAccess {

    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected AbstractRandomAccessList() {
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation performs a linear search, and indexes directly
     * using the getter method.</p>
     *
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
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

    @Override
    public Iterator<E> iterator() {
        return new RandomAccessIterator<E>(this);
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx >= 0) {
            remove(idx);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;

        for (Object o : c) {
            modified |= remove(o);
        }

        return modified;
    }

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
     * <p>Compares the specified object with this list for equality.  Returns
     * {@code true} if and only if the specified object is also a list, both
     * lists have the same size, and all corresponding pairs of elements in
     * the two lists are <em>equal</em>. (Two elements {@code e1} and
     * {@code e2} are <em>equal</em> if {@code (e1==null ? e2==null :
     * e1.equals(e2))}.)  In other words, two lists are defined to be
     * equal if they contain the same elements in the same order.</p>
     *
     * <p>This implementation takes advantage of the fact this is a superclass
     * of a {@code RandomAccess} list. If the other list is a random access
     * list, it walker both lists using the index method rather than a
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

    private static final class RandomAccessIterator<E> implements Iterator<E> {
        private final List<E> m_List;
        private final int m_EndIndex;
        private int m_Index;

        public RandomAccessIterator(AbstractRandomAccessList<E> list) {
            m_List = Objects.requireNonNull(list);
            m_EndIndex = list.size();
            m_Index = 0;
        }

        @Override
        public boolean hasNext() {
            return m_Index < m_EndIndex;
        }

        @Override
        public E next() {
            if((m_Index < 0) || (m_Index >= m_EndIndex)) {
                throw new NoSuchElementException("end of iterator");
            }
            return m_List.get(m_Index++);
        }

        @Override
        public void remove() {
            if((m_Index <= 0) || (m_Index > m_EndIndex)) {
                throw new NoSuchElementException("end of iterator");
            }
            m_List.remove(--m_Index);
        }
    }
}
