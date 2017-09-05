package net.njcull.collections;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;

/**
 * A Spliterator designed for use by sources that traverse and split
 * elements maintained in an unmodifiable indexer. This is based on
 * the {@code Spliterators.ArraySpliterator} implementation.
 * <p>
 * The spliterator, out-of-the-box, has the following characteristics:
 * <ul>
 *     <li>{@code Spliterator.ORDERED}</li>
 *     <li>{@code Spliterator.IMMUTABLE}</li>
 *     <li>{@code Spliterator.SIZED}</li>
 *     <li>{@code Spliterator.SUBSIZED}</li>
 * </ul>
 */
final class ImmutableIndexerSpliterator<T> implements Spliterator<T> {
    /**
     * The indexer, explicitly typed as {@code IntFunction<T>}.
     */
    private final IntFunction<T> array;
    private int index;        // current index, modified on advance/split
    private final int fence;  // one past last index
    private final int characteristics;
    private final Comparator<? super T> comparator;

    /**
     * Creates a spliterator covering all of the given indexer.
     *
     * @param array the array, assumed to be unmodified during use
     * @param size the size of the array
     * @param additionalCharacteristics Additional spliterator characteristics
     * of this spliterator's source or elements beyond {@code SIZED},
     * {@code SUBSIZED}, {@code ORDERED}, and {@code IMMUTABLE} which are
     * always reported
     */
    public ImmutableIndexerSpliterator(IntFunction<T> array, int size, int additionalCharacteristics) {
        this(array, 0, size, null, additionalCharacteristics);
    }

    /**
     * Creates a spliterator covering all of the given indexer, with the
     * given comparator.
     *
     * @param array the array, assumed to be unmodified during use
     * @param size the size of the array
     * @param comparator the comparator for the SORTED order
     * @param additionalCharacteristics Additional spliterator characteristics
     * of this spliterator's source or elements beyond {@code SIZED},
     * {@code SUBSIZED}, {@code ORDERED}, and {@code IMMUTABLE} which are
     * always reported
     */
    public ImmutableIndexerSpliterator(IntFunction<T> array, int size, Comparator<? super T> comparator, int additionalCharacteristics) {
        this(array, 0, size, comparator, additionalCharacteristics);
    }

    /**
     * Creates a spliterator covering the given array and range
     * @param array the array, assumed to be unmodified during use
     * @param origin the least index (inclusive) to cover
     * @param fence one past the greatest index to cover
     * @param comparator the comparator for the SORTED order
     * @param additionalCharacteristics Additional spliterator characteristics
     * of this spliterator's source or elements beyond {@code SIZED},
     * {@code SUBSIZED}, {@code ORDERED}, and {@code IMMUTABLE} which are
     * always reported
     */
    public ImmutableIndexerSpliterator(IntFunction<T> array, int origin, int fence, Comparator<? super T> comparator, int additionalCharacteristics) {
        this.array = array;
        this.index = origin;
        this.fence = fence;
        this.comparator = comparator;
        if ((comparator != null) && ((additionalCharacteristics & Spliterator.SORTED) == 0)) {
            throw new IllegalArgumentException("an unsorted spliterator cannot have a comparator");
        }
        this.characteristics = additionalCharacteristics | Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.SIZED | Spliterator.SUBSIZED;
    }

    /**
     * If this spliterator can be partitioned, returns a Spliterator
     * covering elements, that will, upon return from this method, not
     * be covered by this Spliterator.
     *
     * <p>This Spliterator is {@link #ORDERED}, so the returned Spliterator
     * covers a strict prefix of the elements.
     *
     * <p>Repeated calls to {@code trySplit()} must eventually return {@code null}.
     * Upon non-null return:
     * <ul>
     * <li>the value reported for {@code estimateSize()} before splitting,
     * must, after splitting, be greater than or equal to {@code estimateSize()}
     * for this and the returned Spliterator; and</li>
     * <li>This Spliterator is {@code SUBSIZED}, so {@code estimateSize()}
     * for this spliterator before splitting must be equal to the sum of
     * {@code estimateSize()} for this and the returned Spliterator after
     * splitting.</li>
     * </ul>
     *
     * <p>This method may return {@code null} for any reason,
     * including emptiness, inability to split after traversal has
     * commenced, data structure constraints, and efficiency
     * considerations.
     *
     * <p>
     * An ideal {@code trySplit} method efficiently (without
     * traversal) divides its elements exactly in half, allowing
     * balanced parallel computation.
     *
     * @return a {@code Spliterator} covering some portion of the
     * elements, or {@code null} if this spliterator cannot be split
     */
    @Override
    public Spliterator<T> trySplit() {
        int lo = index, mid = (lo + fence) >>> 1;
        return (lo >= mid) ? null :
                new ImmutableIndexerSpliterator<>(array, lo, index = mid, comparator, characteristics);
    }

    /**
     * Performs the given action for each remaining element, sequentially in
     * the current thread, until all elements have been processed or the action
     * throws an exception.  The Spliterator is {@link #ORDERED}, so actions
     * are performed in encounter order.  Exceptions thrown by the action
     * are relayed to the caller.
     *
     * @param action The action
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        IntFunction<T> a;
        int i, hi; // hoist accesses and checks from loop
        if (action == null) {
            throw new NullPointerException("action is null");
        }
        a = array; hi = fence;
        if ((i = index) >= 0 && i < (index = hi)) {
            do {
                action.accept(a.apply(i));
            } while (++i < hi);
        }
    }

    /**
     * If a remaining element exists, performs the given action on it,
     * returning {@code true}; else returns {@code false}.  The
     * Spliterator is {@link #ORDERED}, so the action is performed on the
     * next element in encounter order.  Exceptions thrown by the
     * action are relayed to the caller.
     *
     * @param action The action
     * @return {@code false} if no remaining elements existed
     * upon entry to this method, else {@code true}.
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (action == null) {
            throw new NullPointerException("action is null");
        }
        if ((index >= 0) && (index < fence)) {
            T e = array.apply(index++);
            action.accept(e);
            return true;
        }
        return false;
    }

    /**
     * Returns an estimate of the number of elements that would be
     * encountered by a {@link #forEachRemaining} traversal, or returns {@link
     * Long#MAX_VALUE} if infinite, unknown, or too expensive to compute.
     *
     * <p>This estimate must be an accurate count of elements that would be
     * encountered by a complete traversal.
     *
     * @return the estimated size, or {@code Long.MAX_VALUE} if infinite,
     *         unknown, or too expensive to compute.
     */
    @Override
    public long estimateSize() {
        return (long)(fence - index);
    }

    /**
     * Returns a set of characteristics of this Spliterator and its
     * elements. The result is represented as ORed values from {@link
     * #ORDERED}, {@link #DISTINCT}, {@link #SORTED}, {@link #SIZED},
     * {@link #NONNULL}, {@link #IMMUTABLE}, {@link #SUBSIZED}.
     * Repeated calls to {@code characteristics()} on a given
     * spliterator, prior to or in-between calls to {@code trySplit},
     * always return the same result.
     *
     * @return a representation of characteristics
     */
    @Override
    public int characteristics() {
        return characteristics;
    }

    /**
     * If this Spliterator's source is {@link #SORTED} by a {@link Comparator},
     * returns that {@code Comparator}. If the source is {@code SORTED} in
     * {@linkplain Comparable natural order}, returns {@code null}.  Otherwise,
     * if the source is not {@code SORTED}, throws {@link IllegalStateException}.
     *
     * <p>This implementation always returns {@code null}.
     *
     * @return a Comparator, or {@code null} if the elements are sorted in the
     * natural order.
     * @throws IllegalStateException if the spliterator does not report
     *         a characteristic of {@code SORTED}.
     */
    @Override
    public Comparator<? super T> getComparator() {
        if ((characteristics & Spliterator.SORTED) == Spliterator.SORTED) {
            return comparator;
        }
        throw new IllegalStateException("no comparator for non-sorted spliterator");
    }
}
