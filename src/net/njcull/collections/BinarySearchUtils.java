package net.njcull.collections;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.IntFunction;

/**
 * Utility methods for binary searching. These can be applied to arrays or
 * similar array-like structures that use an indexer to retrieve elements.
 *
 * @author run2000
 * @version 8/01/2016.
 */
public final class BinarySearchUtils {

    private BinarySearchUtils() {
    }

    /**
     * Searches the specified list for the specified object using the binary
     * search algorithm.  The list must be sorted into ascending order
     * according to the {@linkplain Comparable natural ordering} of its
     * elements (as by the {@code java.util.Arrays.sort(Object[])} method)
     * prior to making this call.  If it is not sorted, the results are undefined.
     * If the list contains multiple elements equal to the specified object,
     * there is no guarantee which one will be found.
     * <p>
     * Flogged from Collections#binarySearch(), and adjusted to take an
     * IntFunction as the indexer. This allows the search to operate over
     * arrays, lists, and other objects that have an indexer method.
     * </p>
     *
     * @param  <T> the class of the objects in the list
     * @param indexer an int function that takes an index and returns the
     *                element at the given index. Indexes must be zero-based, and valid for &gt;= 0.
     * @param size the total number of elements in the index, from {@code 0} to {@code size - 1}
     * @param key the key to be found
     * @return the index of the search key, if it is contained in the index;
     *         otherwise, {@code (-(insertion point) - 1)}. The
     *         <em>insertion point</em> is defined as the point at which the
     *         key would be inserted into the list: the index of the first
     *         element greater than the key, or {@code size} if all
     *         elements in the list are less than the specified key. Note
     *         that this guarantees that the return value will be &gt;= 0 if
     *         and only if the key is found.
     * @throws NullPointerException the {@code indexer} is null
     * @throws IndexOutOfBoundsException {@code size} is less than 0
     */
    public static <T> int indexedSearch(IntFunction<? extends Comparable<? super T>> indexer, int size, T key) {
        return indexedSearch(indexer, 0, size, key);
    }

    /**
     * Searches a range of the specified indexer for the specified object
     * using the binary search algorithm.
     * The range must be sorted into ascending order according to the
     * {@linkplain Comparable natural ordering} of its elements (as by the
     * {@code java.util.Arrays.sort(Object[], int, int} method)
     * prior to making this call. If it is not sorted, the results are undefined.
     * <p>
     * Flogged from Arrays#binarySearch(), and adjusted to take an
     * IntFunction as the indexer. This allows the search to operate over
     * arrays, lists, and other objects that have an indexer method.
     * </p>
     *
     * @param  <T> the class of the objects in the list
     * @param indexer an int function that takes an index and returns the
     *                element at the given index. Indexes must be &gt;= 0.
     * @param fromIndex the index of the first element (inclusive) to be
     *          searched
     * @param toIndex the index of the last element (exclusive) to be searched
     * @param key the key to be found
     * @return index of the search key, if it is contained in the index
     *         within the specified range;
     *         otherwise, {@code (-(insertion point) - 1)}. The
     *         <em>insertion point</em> is defined as the point at which the
     *         key would be inserted into the array: the index of the first
     *         element in the range greater than the key, or {@code toIndex} if all
     *         elements in the range are less than the specified key. Note
     *         that this guarantees that the return value will be &gt;= 0 if
     *         and only if the key is found.
     * @throws NullPointerException the {@code indexer} is null
     * @throws IndexOutOfBoundsException {@code fromIndex} is less than 0,
     *         or {@code toIndex} is less than {@code fromIndex}
     */
    public static <T> int indexedSearch(IntFunction<? extends Comparable<? super T>> indexer,
                                        int fromIndex, int toIndex, T key) {
        int low = fromIndex;
        int high = toIndex - 1;

        Objects.requireNonNull(indexer, "indexer function must not be null");
        if(fromIndex < 0) {
            throw new IndexOutOfBoundsException("fromIndex must be >= 0");
        }
        if(toIndex < fromIndex) {
            throw new IndexOutOfBoundsException("toIndex must be >= fromIndex");
        }

        while (low <= high) {
            int mid = (low + high) >>> 1;
            Comparable<? super T> midVal = indexer.apply(mid);
            int cmp = midVal.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found.
    }

    /**
     * Searches the specified list for the specified object using the binary
     * search algorithm.  The list must be sorted into ascending order
     * according to the specified comparator (as by the
     * {@code java.util.Arrays.sort(Object[], Comparator)}
     * method), prior to making this call.  If it is
     * not sorted, the results are undefined.  If the list contains multiple
     * elements equal to the specified object, there is no guarantee which one
     * will be found.
     * <p>
     * Flogged from Collections#binarySearch(), and adjusted to take an
     * IntFunction as the indexer. This allows the search to operate over
     * arrays, lists, and other objects that have an indexer method.
     * </p>
     *
     * @param  <T> the class of the objects in the list
     * @param indexer an int function that takes an index and returns the
     *                element at the given index. Indexes must be zero-based, and valid for &gt;= 0.
     * @param size the total number of elements in the index, from {@code 0} to {@code size - 1}
     * @param key the key to be found
     * @param comparator the comparator to determine the order of the elements
     * @return the index of the search key, if it is contained in the index;
     *         otherwise, {@code (-(insertion point) - 1)}. The
     *         <em>insertion point</em> is defined as the point at which the
     *         key would be inserted into the list: the index of the first
     *         element greater than the key, or {@code size} if all
     *         elements in the list are less than the specified key. Note
     *         that this guarantees that the return value will be &gt;= 0 if
     *         and only if the key is found.
     * @throws NullPointerException the {@code indexer} is null, or
     *         {@code comparator} is null
     * @throws IndexOutOfBoundsException {@code fromIndex} is less than 0,
     *         or {@code toIndex} is less than {@code fromIndex}
     */
    public static <T> int indexedSearch(IntFunction<? extends T> indexer, int size, T key, Comparator<? super T> comparator) {
        return indexedSearch(indexer, 0, size, key, comparator);
    }

    /**
     * Searches a range of
     * the specified indexer for the specified object using the binary
     * search algorithm.  The array must be sorted into ascending order
     * according to the specified comparator (as by the
     * {@code java.util.Arrays.sort(T[], int, int, Comparator)}
     * method) prior to making this call.  If it is not sorted, the results
     * are undefined. If the array contains multiple
     * elements equal to the specified object, there is no guarantee which one
     * will be found.
     * <p>
     * Flogged from Arrays#binarySearch(), and adjusted to take an
     * IntFunction as the indexer. This allows the search to operate over
     * arrays, lists, and other objects that have an indexer method.
     * </p>
     *
     * @param  <T> the class of the objects in the list
     * @param indexer an int function that takes an index and returns the
     *                element at the given index. Indexes must be &gt;= 0.
     * @param fromIndex the index of the first element (inclusive) to be
     *          searched
     * @param toIndex the index of the last element (exclusive) to be searched
     * @param key the key to be found
     * @param comparator the comparator to determine the order of the elements
     * @return index of the search key, if it is contained in the index
     *         within the specified range;
     *         otherwise, {@code (-(insertion point) - 1)}. The
     *         <em>insertion point</em> is defined as the point at which the
     *         key would be inserted into the array: the index of the first
     *         element in the range greater than the key, or {@code toIndex} if all
     *         elements in the range are less than the specified key. Note
     *         that this guarantees that the return value will be &gt;= 0 if
     *         and only if the key is found.
     * @throws NullPointerException the {@code indexer} is null, or
     *         {@code comparator} is null
     * @throws IndexOutOfBoundsException {@code fromIndex} is less than 0,
     *         or {@code toIndex} is less than {@code fromIndex}
     */
    public static <T> int indexedSearch(IntFunction<? extends T> indexer,
                                        int fromIndex, int toIndex, T key,
                                        Comparator<? super T> comparator) {
        int low = fromIndex;
        int high = toIndex - 1;

        Objects.requireNonNull(indexer, "indexer function must not be null");
        if(fromIndex < 0) {
            throw new IndexOutOfBoundsException("fromIndex must be >= 0");
        }
        if(toIndex < fromIndex) {
            throw new IndexOutOfBoundsException("toIndex must be >= fromIndex");
        }
        Objects.requireNonNull(comparator, "comparator must not be null");

        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = indexer.apply(mid);
            int cmp = comparator.compare(midVal, key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found.
    }
}
