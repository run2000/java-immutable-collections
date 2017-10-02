package net.njcull.collections;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Implementations of stream {@link Collector}s for the maps and sets in this
 * package. Variations include map versus bi-map, and various sorting options
 * for those sets and maps that are ordered.
 *
 * @author run2000
 * @version 9/01/2016.
 */
public final class Collectors {

    private Collectors() {
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableArraySet}, in encounter order.
     *
     * @param <E> the type of elements maintained by the resulting set
     * @return a {@code Collector} which collects all the input elements into a
     * {@code ImmutableArraySet}, in encounter order
     */
    public static <E> Collector<E, ?, ImmutableArraySet<E>> toImmutableArraySet() {
        return Collector.<E, ImmutableArraySetBuilder<E>, ImmutableArraySet<E>>of(
                ImmutableArraySetBuilder<E>::new,
                ImmutableArraySetBuilder::with,
                ImmutableArraySetBuilder::merge,
                ImmutableArraySetBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableHashedArraySet}, in encounter order.
     *
     * @param <E> the type of elements maintained by the resulting set
     * @return a {@code Collector} which collects all the input elements into a
     * {@code ImmutableHashedArraySet}, in encounter order
     */
    public static <E> Collector<E, ?, ImmutableHashedArraySet<E>> toImmutableHashedArraySet() {
        return Collector.<E, ImmutableHashedArraySetBuilder<E>, ImmutableHashedArraySet<E>>of(
                ImmutableHashedArraySetBuilder<E>::new,
                ImmutableHashedArraySetBuilder::with,
                ImmutableHashedArraySetBuilder::merge,
                ImmutableHashedArraySetBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArraySet}, in natural sort order.
     *
     * @param <E> the type of elements maintained by the resulting set
     * @return a {@code Collector} which collects all the input elements into a
     * {@code ImmutableSortedArraySet}, in natural sort order
     */
    public static <E> Collector<E, ?, ImmutableSortedArraySet<E>> toImmutableSortedArraySet() {
        return Collector.<E, ImmutableSortedArraySetBuilder<E>, ImmutableSortedArraySet<E>>of(
                ImmutableSortedArraySetBuilder<E>::new,
                ImmutableSortedArraySetBuilder::with,
                ImmutableSortedArraySetBuilder::merge,
                ImmutableSortedArraySetBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArraySet}, in the sort order determined by the
     * supplied {@code Comparator}.
     *
     * @param <E> the type of elements maintained by the resulting set
     * @param cmp the comparator used for determining the order of the resulting
     *            {@code SortedSet}
     * @return a {@code Collector} which collects all the input elements into a
     * {@code ImmutableSortedArraySet}, in the sort order determine by the
     * supplied {@code Comparator}
     */
    public static <E> Collector<E, ?, ImmutableSortedArraySet<E>> toImmutableSortedArraySetComparing(
            final Comparator<? super E> cmp) {
        return Collector.<E, ImmutableSortedArraySetBuilder<E>, ImmutableSortedArraySet<E>>of(
                () -> ImmutableSortedArraySetBuilder.<E>newComparing(cmp),
                ImmutableSortedArraySetBuilder::with,
                ImmutableSortedArraySetBuilder::merge,
                ImmutableSortedArraySetBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableArrayMap}, in encounter order.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableArrayMap}, in encounter order
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableArrayMap<K,V>> toImmutableArrayMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableArrayMapBuilder<K,V>, ImmutableArrayMap<K,V>>of(
                ImmutableArrayMapBuilder::<K,V>newMap,
                ImmutableArrayMapBuilder::with,
                ImmutableArrayMapBuilder::merge,
                ImmutableArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableArrayMap}, created as a bi-map, in encounter order.
     * <p>
     * A bi-map is a map where both keys and values are unique. Any given key
     * can be mapped "forwards" to a single value; any given value can be
     * mapped "backwards" to a single key.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableArrayMap}, created as a bi-map, in encounter order
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableArrayMap<K,V>> toImmutableArrayBiMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableArrayMapBuilder<K,V>, ImmutableArrayMap<K,V>>of(
                ImmutableArrayMapBuilder::<K,V>newBiMap,
                ImmutableArrayMapBuilder::with,
                ImmutableArrayMapBuilder::merge,
                ImmutableArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableHashedArrayMap}, in encounter order.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableHashedArrayMap}, in encounter order
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableHashedArrayMap<K,V>> toImmutableHashedArrayMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableHashedArrayMapBuilder<K,V>, ImmutableHashedArrayMap<K,V>>of(
                ImmutableHashedArrayMapBuilder::<K,V>newMap,
                ImmutableHashedArrayMapBuilder::with,
                ImmutableHashedArrayMapBuilder::merge,
                ImmutableHashedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableHashedArrayMap}, created as a bi-map, in
     * encounter order.
     * <p>
     * A bi-map is a map where both keys and values are unique. Any given key
     * can be mapped "forwards" to a single value; any given value can be
     * mapped "backwards" to a single key.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableHashedArrayMap}, created as a bi-map, in
     * encounter order
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableHashedArrayMap<K,V>> toImmutableHashedArrayBiMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableHashedArrayMapBuilder<K,V>, ImmutableHashedArrayMap<K,V>>of(
                ImmutableHashedArrayMapBuilder::<K,V>newBiMap,
                ImmutableHashedArrayMapBuilder::with,
                ImmutableHashedArrayMapBuilder::merge,
                ImmutableHashedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArrayMap}, with elements ordered by their
     * natural order.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, with elements ordered by their
     * natural order
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                ImmutableSortedArrayMapBuilder::newMap,
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArrayMap}, with keys ordered by the given
     * {@code Comparator}.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @param keyCmp a comparator for ordering keys, or {@code null} to indicate
     * natural key ordering
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, with keys ordered by the given
     * comparator
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayMapComparingKeys(
            final Comparator<? super K> keyCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.newMapComparingKeys(keyCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArrayMap}, with values ordered by the given
     * {@code Comparator}.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @param valCmp a comparator for ordering values, or {@code null}
     * to indicate natural value ordering
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, with values ordered by the given
     * comparator
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayMapComparingValues(
            final Comparator<? super V> valCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.newMapComparingValues(valCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArrayMap}, with keys and values ordered by
     * the given {@code Comparator}s.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @param keyCmp a comparator for ordering keys, or {@code null} to indicate
     * natural key ordering
     * @param valCmp a comparator for ordering values, or {@code null}
     * to indicate natural value ordering
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, with keys and values ordered by
     * the given comparators
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayMapComparingKeysAndValues(
            final Comparator<? super K> keyCmp, final Comparator<? super V> valCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.newMapComparing(keyCmp, valCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArrayMap}, created as a bi-map, with elements
     * ordered by their natural order.
     * <p>
     * A bi-map is a map where both keys and values are unique. Any given key
     * can be mapped "forwards" to a single value; any given value can be
     * mapped "backwards" to a single key.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, created as a bi-map, with
     * elements ordered by their natural order
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayBiMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                ImmutableSortedArrayMapBuilder::<K,V>newBiMap,
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArrayMap}, created as a bi-map, with keys
     * ordered by the given {@code Comparator}.
     * <p>
     * A bi-map is a map where both keys and values are unique. Any given key
     * can be mapped "forwards" to a single value; any given value can be
     * mapped "backwards" to a single key.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @param keyCmp a comparator for ordering keys, or {@code null} to indicate
     * natural key ordering
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, created as a bi-map, with keys
     * ordered by the given comparator
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayBiMapComparingKeys(
            final Comparator<? super K> keyCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.<K,V>newBiMapComparingKeys(keyCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArrayMap}, created as a bi-map, with values
     * ordered by the given {@code Comparator}.
     * <p>
     * A bi-map is a map where both keys and values are unique. Any given key
     * can be mapped "forwards" to a single value; any given value can be
     * mapped "backwards" to a single key.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @param valCmp a comparator for ordering values, or {@code null}
     * to indicate natural value ordering
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, created as a bi-map, with values
     * ordered by the given comparator
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayBiMapComparingValues(
            final Comparator<? super V> valCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.<K,V>newBiMapComparingValues(valCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArrayMap}, created as a bi-map, with keys
     * and values ordered by the given {@code Comparator}s.
     * <p>
     * A bi-map is a map where both keys and values are unique. Any given key
     * can be mapped "forwards" to a single value; any given value can be
     * mapped "backwards" to a single key.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @param keyCmp a comparator for ordering keys, or {@code null} to indicate
     * natural key ordering
     * @param valCmp a comparator for ordering values, or {@code null}
     * to indicate natural value ordering
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, created as a bi-map, with keys
     * and values ordered by the given comparators
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayBiMapComparingKeysAndValues(
            final Comparator<? super K> keyCmp, final Comparator<? super V> valCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.<K,V>newBiMapComparing(keyCmp, valCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableUniSortedArrayMap}, with keys ordered by their
     * natural order.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, with keys ordered by their
     * natural order
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableUniSortedArrayMap<K,V>> toImmutableUniSortedArrayMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableUniSortedArrayMapBuilder<K,V>, ImmutableUniSortedArrayMap<K,V>>of(
                ImmutableUniSortedArrayMapBuilder::newMap,
                ImmutableUniSortedArrayMapBuilder::with,
                ImmutableUniSortedArrayMapBuilder::merge,
                ImmutableUniSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableUniSortedArrayMap}, with keys ordered by the given
     * {@code Comparator}.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @param keyCmp a comparator for ordering keys, or {@code null} to indicate
     * natural key ordering
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, with keys ordered by the given
     * comparator
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableUniSortedArrayMap<K,V>> toImmutableUniSortedArrayMapComparing(
            final Comparator<? super K> keyCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableUniSortedArrayMapBuilder<K,V>, ImmutableUniSortedArrayMap<K,V>>of(
                () -> ImmutableUniSortedArrayMapBuilder.newMapComparing(keyCmp),
                ImmutableUniSortedArrayMapBuilder::with,
                ImmutableUniSortedArrayMapBuilder::merge,
                ImmutableUniSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableUniSortedArrayMap}, created as a bi-map, with keys
     * ordered by their natural order.
     * <p>
     * A bi-map is a map where both keys and values are unique. Any given key
     * can be mapped "forwards" to a single value; any given value can be
     * mapped "backwards" to a single key.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, created as a bi-map, with keys
     * ordered by their natural order
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableUniSortedArrayMap<K,V>> toImmutableUniSortedArrayBiMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableUniSortedArrayMapBuilder<K,V>, ImmutableUniSortedArrayMap<K,V>>of(
                ImmutableUniSortedArrayMapBuilder::<K,V>newBiMap,
                ImmutableUniSortedArrayMapBuilder::with,
                ImmutableUniSortedArrayMapBuilder::merge,
                ImmutableUniSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableUniSortedArrayMap}, created as a bi-map, with keys
     * ordered by the given {@code Comparator}.
     * <p>
     * A bi-map is a map where both keys and values are unique. Any given key
     * can be mapped "forwards" to a single value; any given value can be
     * mapped "backwards" to a single key.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @param keyCmp a comparator for ordering keys, or {@code null} to indicate
     * natural key ordering
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, created as a bi-map, with keys
     * ordered by the given comparator
     */
    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableUniSortedArrayMap<K,V>> toImmutableUniSortedArrayBiMapComparing(
            final Comparator<? super K> keyCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableUniSortedArrayMapBuilder<K,V>, ImmutableUniSortedArrayMap<K,V>>of(
                () -> ImmutableUniSortedArrayMapBuilder.<K,V>newBiMapComparing(keyCmp),
                ImmutableUniSortedArrayMapBuilder::with,
                ImmutableUniSortedArrayMapBuilder::merge,
                ImmutableUniSortedArrayMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArrayPropertyMap}, with keys generated by
     * the given key supplier, and keys ordered by their natural order.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @param keySupplier a function for generating a key from a given value
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, with keys ordered by their
     * natural order
     */
    public static <K,V> Collector<V, ?, ImmutableSortedArrayPropertyMap<K,V>> toImmutableSortedArrayPropertyMap(
            final Function<? super V,? extends K> keySupplier) {
        return Collector.of(
                () -> ImmutableSortedArrayPropertyMapBuilder.<K,V>newMapWithKeys(keySupplier),
                ImmutableSortedArrayPropertyMapBuilder::with,
                ImmutableSortedArrayPropertyMapBuilder::merge,
                ImmutableSortedArrayPropertyMapBuilder::build);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a
     * new {@code ImmutableSortedArrayPropertyMap}, with keys generated by
     * the given key supplier, and keys ordered by the given {@code Comparator}.
     *
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @param keySupplier a function for generating a key from a given value
     * @param keyCmp a comparator for ordering keys, or {@code null} to indicate
     * natural key ordering
     * @return a {@code Collector} which collects all the input keys and values
     * into a {@code ImmutableSortedArrayMap}, with keys ordered by the given
     * comparator
     */
    public static <K,V> Collector<V, ?, ImmutableSortedArrayPropertyMap<K,V>> toImmutableSortedArrayPropertyMapComparingKeys(
            final Function<? super V,? extends K> keySupplier, final Comparator<? super K> keyCmp) {
        return Collector.of(
                () -> ImmutableSortedArrayPropertyMapBuilder.<K,V>newMapWithKeysComparing(keySupplier, keyCmp),
                ImmutableSortedArrayPropertyMapBuilder::with,
                ImmutableSortedArrayPropertyMapBuilder::merge,
                ImmutableSortedArrayPropertyMapBuilder::build);
    }
}
