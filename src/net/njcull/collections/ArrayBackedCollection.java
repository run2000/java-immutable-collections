package net.njcull.collections;

import java.util.Collection;
import java.util.List;

/**
 * @author run2000
 * @version 10/01/2016.
 */
public interface ArrayBackedCollection<E> extends Collection<E> {

    E getAtIndex(int index);

    int indexOf(E element);

    int indexOfRange(E element, int fromIndex, int toIndex);

    /**
     * Return a backing list view for this set.
     *
     * @return a list containing all the elements of this {@code Set}
     */
    List<E> asList();

}
