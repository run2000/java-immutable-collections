package net.njcull.collections;

import java.util.Collection;
import java.util.List;

/**
 * <p>A {@code Collection} implemented by a backing array, and has a {@link List}
 * view over the data.</p>
 * {@inheritDoc}
 *
 * @param <E> the type of elements maintained by this collection and backing list
 * @author run2000
 * @version 10/01/2016.
 */
public interface ArrayBackedCollection<E> extends Collection<E> {

    /**
     * Get the element at the specified array index.
     *
     * @param index the index of the item to be retrieved
     * @return the item at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    E getAtIndex(int index);

    /**
     * Determine the index of the given element, if it exists in this
     * collection.
     *
     * @param element the element to be found
     * @return a zero or positive integer if the element is in the
     * backing array, otherwise less than zero to indicate its absence
     */
    int indexOf(E element);

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
     *         (<code>index &lt; 0 || index &gt;= size()</code>), or toIndex
     *         is less than fromIndex
     */
    int indexOfRange(E element, int fromIndex, int toIndex);

    /**
     * Return a backing list view for this set.
     *
     * @return a list containing all the elements of this {@code Collection}
     */
    List<E> asList();

}
