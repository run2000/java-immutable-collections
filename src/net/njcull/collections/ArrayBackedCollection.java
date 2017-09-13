package net.njcull.collections;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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
     *         ({@code index < 0 || index >= size()})
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
     *         ({@code index < 0 || index >= size()}), or toIndex
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
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception. Actions are performed in the order of iteration.
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     */
    default void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        final int size = size();

        for (int i = 0; i < size; i++) {
            action.accept(getAtIndex(i));
        }
    }

    /**
     * Return a backing list view for this collection.
     *
     * @return a list view containing all the elements of this {@code Collection}
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
     * default method because a default method cannot override any method
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

    /**
     * Returns an array containing all of the elements in the given collection.
     * If this collection makes any guarantees as to what order its elements
     * are returned by its indexer, this method must return the
     * elements in the same order.
     *
     * <p>The returned array will be "safe" in that no references to it
     * are maintained by this collection.  In other words, this method
     * allocates a new array even if this collection is backed by an array.
     * The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * <p>This implementation is handled as a static method rather than a
     * default method because a default method cannot override any method
     * of {@code Object}.
     *
     * @param c the collection from which the elements are copied
     * @return an array containing all the elements in this set
     */
    static Object[] toArray(ArrayBackedCollection<?> c) {
        final int sz = c.size();
        Object[] arr = new Object[sz];

        for(int i = 0; i < sz; i++) {
            arr[i] = c.getAtIndex(i);
        }
        return arr;
    }

    /**
     * Returns an array containing all of the elements in the given collection
     * in proper sequence (from first to last element); the runtime type of the
     * returned array is that of the specified array.  If the collection fits
     * in the specified array, it is returned therein.  Otherwise, a new array
     * is allocated with the runtime type of the specified array and the size
     * of this collection.
     *
     * <p>If the collection fits in the specified array with room to spare
     * (i.e., the array has more elements than the collection), the element in
     * the array immediately following the end of the collection is set to
     * {@code null}.  This is useful in determining the length of the
     * collection <em>only</em> if the caller knows that the collection does
     * not contain any null elements.
     *
     * <p>This implementation is handled as a static method rather than a
     * default method because a default method cannot override any method
     * of {@code Object}.
     *
     * @param <T> the runtime type of the array to contain the collection
     * @param c the collection from which the elements are copied
     * @param a the array into which the elements of the collection are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of the collection
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         the given collection
     * @throws NullPointerException if the specified array is null
     */
    @SuppressWarnings("unchecked")
    static <T> T[] toArray(ArrayBackedCollection<?> c, T[] a) {
        final int sz = c.size();
        if (a.length < sz) {
            // Make a new array of a's runtime type, but my contents:
            a = (T[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), sz);
        }
        for(int i = 0; i < sz; i++) {
            a[i] = (T)c.getAtIndex(i);
        }
        if (a.length > sz) {
            a[sz] = null;
        }
        return a;
    }
}
