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

    /**
     * Returns the hash code value for the given set. The hash code of a set is
     * defined to be the sum of the hash codes of the elements in the set,
     * where the hash code of a {@code null} element is defined to be zero.
     * This ensures that {@code s1.equals(s2)} implies that
     * {@code s1.hashCode()==s2.hashCode()} for any two sets {@code s1}
     * and {@code s2}, as required by the general contract of
     * {@link Object#hashCode}.
     * <p>
     * This implementation is handled as a static method rather than a
     * default method because a default method cannot override any method
     * of {@code Object}.
     *
     * @param <E> the type of elements in the set
     * @param s the set for which the hashcode will be generated
     * @return the hash code value for the given set
     */
    static <E> int hashCode(ArrayBackedSet<E> s) {
        final int sz = s.size();
        int h = 0;
        for (int i = 0; i < sz; i++) {
            E obj = s.getAtIndex(i);
            if (obj != null) {
                h += obj.hashCode();
            }
        }
        return h;
    }
}