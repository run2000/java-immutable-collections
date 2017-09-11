package net.njcull.collections;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.IntFunction;

/**
 * An immutable list for a list-like structure, where values may be
 * characterised as distinct.
 *
 * @author run2000
 * @version 5/09/2017 9:52 AM.
 */
final class ArrayBackedImmutableList<E> extends AbstractRandomAccessList<E>
        implements ArrayBackedCollection<E> {
    private final IntFunction<E> m_Indexer;
    private final Comparator<? super E> m_Comparator;
    private final int m_Size;
    private final int m_Characteristics;

    private static final int DEFAULT_FLAGS = 0;

    /**
     * Create a new list view for the given fixed size indexer. The indexer
     * and size are provided, along with a flag to indicate whether the list
     * is distinct. Elements of the list are assumed to count from {@code 0}
     * to {@code size - 1}.
     *
     * @param indexer the index function for getting elements of the list
     * @param size the total size of the list
     * @throws NullPointerException the indexer is not provided
     * @throws IllegalArgumentException the size is less than zero
     */
    public ArrayBackedImmutableList(IntFunction<E> indexer, int size) {
        this.m_Indexer = Objects.requireNonNull(indexer, "indexed must be provided");
        this.m_Size = size;
        m_Characteristics = DEFAULT_FLAGS;
        m_Comparator = null;
        if(size < 0) {
            throw new IllegalArgumentException("list size must be >= 0");
        }
    }

    /**
     * Create a new list view for the given fixed size indexer. The indexer
     * and size are provided, along with a flag to indicate whether the list
     * is distinct. Elements of the list are assumed to count from {@code 0}
     * to {@code size - 1}.
     *
     * @param indexer the index function for getting elements of the list
     * @param size the total size of the list
     * @param moreFlags flags from the {@code Spliterator} class to be ORed
     * when creating a spliterator
     * @throws NullPointerException the indexer is not provided
     * @throws IllegalArgumentException the size is less than zero
     */
    public ArrayBackedImmutableList(IntFunction<E> indexer, int size, int moreFlags) {
        this.m_Indexer = Objects.requireNonNull(indexer, "indexed must be provided");
        this.m_Size = size;
        m_Characteristics = moreFlags;
        m_Comparator = null;
        if(size < 0) {
            throw new IllegalArgumentException("list size must be >= 0");
        }
    }

    /**
     * Create a new list view for the given fixed size indexer. The indexer
     * and size are provided, along with a flag to indicate whether the list
     * is distinct. Elements of the list are assumed to count from {@code 0}
     * to {@code size - 1}.
     *
     * @param indexer the index function for getting elements of the list
     * @param size the total size of the list
     * @param moreFlags flags from the {@code Spliterator} class to be ORed
     * when creating a spliterator
     * @param comparator the comparator used for sorting the elements from
     * the indexer
     * @throws NullPointerException the indexer is not provided
     * @throws IllegalArgumentException the size is less than zero
     */
    public ArrayBackedImmutableList(IntFunction<E> indexer, int size, int moreFlags, Comparator<? super E> comparator) {
        this.m_Indexer = Objects.requireNonNull(indexer, "indexed must be provided");
        this.m_Size = size;
        m_Characteristics = moreFlags;
        m_Comparator = comparator;
        if(size < 0) {
            throw new IllegalArgumentException("list size must be >= 0");
        }
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the value to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    @Override
    public E get(int index) {
        if((index < 0) || (index >= m_Size)) {
            throw new IndexOutOfBoundsException("index out of range for the list");
        }
        return m_Indexer.apply(index);
    }

    /**
     * Returns the number of elements in this list. Delegates to the
     * underlying map.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return m_Size;
    }

    /**
     * Determine whether this list is empty. Delegates to the underlying map.
     *
     * @return {@code true} if this list contains no elements, otherwise
     * {@code false}
     */
    @Override
    public boolean isEmpty() {
        return m_Size == 0;
    }

    /**
     * Creates a {@link Spliterator} over the elements in this list.
     *
     * @return a {@code Spliterator} over the elements in this list
     */
    @Override
    public Spliterator<E> spliterator() {
        return new ImmutableIndexerSpliterator<>(m_Indexer, m_Size, m_Comparator, m_Characteristics);
    }

    /**
     * Get the element at the specified array index. Equivalent to
     * {@link #get(int)} for list implementations.
     *
     * @param index the index of the item to be retrieved
     * @return the item at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    @Override
    public E getAtIndex(int index) {
        if((index < 0) || (index >= m_Size)) {
            throw new IndexOutOfBoundsException("index out of list range");
        }
        return m_Indexer.apply(index);
    }

    /**
     * Determine the index of the given element, if it exists within the
     * specified range in this collection.
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
    @Override
    public int indexOfRange(E element, int fromIndex, int toIndex) {
        if(fromIndex < 0 || fromIndex >= m_Size) {
            throw new IndexOutOfBoundsException("fromIndex: "+ fromIndex);
        }
        if(toIndex < fromIndex || toIndex > m_Size) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex);
        }
        if(element == null) {
            for (int i = fromIndex; i < toIndex; i++) {
                if(element == m_Indexer.apply(i)) {
                    return i;
                }
            }
        } else {
            for (int i = fromIndex; i < toIndex; i++) {
                if(element.equals(m_Indexer.apply(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Return a backing list view for this collection. For this implementation,
     * this object is the same as the backing list.
     *
     * @return this list
     */
    @Override
    public List<E> asList() {
        return this;
    }
}
