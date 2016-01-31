package net.njcull.collections;

import java.util.List;
import java.util.Set;

/**
 * <p>A {@code Set} implemented by a backing array, and has a {@link List}
 * view over the data.</p>
 * {@inheritDoc}
 *
 * @param <E> the type of elements maintained by this set and backing list
 * @author run2000
 * @version 9/01/2016.
 */
public interface ArrayBackedSet<E> extends ArrayBackedCollection<E>, Set<E> {

}