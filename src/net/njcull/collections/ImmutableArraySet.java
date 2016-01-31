package net.njcull.collections;

import java.util.*;

/**
 * @author run2000
 * @version 9/01/2016.
 */
public final class ImmutableArraySet<E> extends AbstractSet<E> implements ArrayBackedSet<E> {

    private final Object[] m_Elements;

    private static final ImmutableArraySet<?> EMPTY = new ImmutableArraySet<>(new Object[0]);

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

    @Override
    public boolean isEmpty() {
        return m_Elements.length == 0;
    }

    @Override
    public int size() {
        return m_Elements.length;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(m_Elements, m_Elements.length);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.CONCURRENT);
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
