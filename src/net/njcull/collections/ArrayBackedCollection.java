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
    default int indexOf(E element) {
        final int size = size();
        if(element == null) {
            for(int i = 0; i < size; i++) {
                if(element == getAtIndex(i)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element.equals(getAtIndex(i))) {
                    return i;
                }
            }
        }
        return -1;
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
     *         (<code>index &lt; 0 || index &gt;= size()</code>), or toIndex
     *         is less than fromIndex
     */
    default int indexOfRange(E element, int fromIndex, int toIndex) {
        final int size = size();
        if(fromIndex < 0 || fromIndex >= size) {
            throw new IndexOutOfBoundsException("fromIndex: "+ fromIndex);
        }
        if(toIndex < fromIndex || toIndex > size) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex);
        }
        if(element == null) {
            for (int i = fromIndex; i < toIndex; i++) {
                if(element == getAtIndex(i)) {
                    return i;
                }
            }
        } else {
            for (int i = fromIndex; i < toIndex; i++) {
                if(element.equals(getAtIndex(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Return a backing list view for this set.
     *
     * @return a list containing all the elements of this {@code Collection}
     */
    List<E> asList();

    /**
     * Returns a string representation of this collection.  The string
     * representation consists of a sequence of the collection's elements in the
     * order they are returned by its {@code get(int)} method, enclosed in
     * square brackets ({@code "[]"}).  Adjacent elements are separated by
     * the characters {@code ", "} (comma and space).  Elements are converted
     * to strings as by {@link String#valueOf(Object)}.
     * <p>
     * This implementation is handled as a static method rather than a
     * default method because a default method cannot override any methods
     * of {@code Object}.
     *
     * @param <E> the type of elements in the collection
     * @param c the collection from which the string will be generated
     * @return a string representation of this collection
     */
    static <E> String toString(ArrayBackedCollection<E> c) {
            final int sz = c.size();
            if (sz == 0) {
                return "[]";
            }

            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (int i = 0; i < sz; ) {
                E e = c.getAtIndex(i++);
                sb.append(e == c ? "(this Collection)" : e);
                if (i < sz) {
                    sb.append(',').append(' ');
                }
            }
            return sb.append(']').toString();
    }
}
