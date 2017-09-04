package net.njcull.collections;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * An immutable value list for an ArrayBackedMap. Values are not assumed
 * to be distinct.
 *
 * @author run2000
 * @version 11/01/2016.
 */
final class ArrayBackedMapValueList<E> extends AbstractRandomAccessList<E> {
    private final ArrayBackedMap<?, E> m_Map;

    /**
     * Construct a new value list for the given array-backed map.
     *
     * @param map the map
     * @throws NullPointerException supplied map is null
     */
    ArrayBackedMapValueList(ArrayBackedMap<?, E> map) {
        this.m_Map = Objects.requireNonNull(map, "map must be non-null");
    }

    /**
     * Determine whether this list is empty. Delegates to the underlying map.
     *
     * @return {@code true} if this list contains no elements, otherwise
     * {@code false}
     */
    @Override
    public boolean isEmpty() {
        return m_Map.isEmpty();
    }

    /**
     * Returns the number of elements in this list. Delegates to the
     * underlying map.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return m_Map.size();
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
        return m_Map.valueAt(index);
    }

    /**
     * Creates a {@link Spliterator} over the elements in this list.
     * The underlying map is not a bimap, so the spliterator does not report
     * {@link Spliterator#DISTINCT}.
     *
     * @return a {@code Spliterator} over the elements in this list
     */
    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.CONCURRENT);
    }
}
