package net.njcull.collections;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Implementations of {@link Collector}s for the maps and sets in this package.
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
     * supplied Comparator.
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

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableArrayMap<K,V>> toImmutableArrayMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableArrayMapBuilder<K,V>, ImmutableArrayMap<K,V>>of(
                ImmutableArrayMapBuilder::<K,V>newMap,
                ImmutableArrayMapBuilder::with,
                ImmutableArrayMapBuilder::merge,
                ImmutableArrayMapBuilder::build);
    }

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableArrayMap<K,V>> toImmutableArrayBiMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableArrayMapBuilder<K,V>, ImmutableArrayMap<K,V>>of(
                ImmutableArrayMapBuilder::<K,V>newBiMap,
                ImmutableArrayMapBuilder::with,
                ImmutableArrayMapBuilder::merge,
                ImmutableArrayMapBuilder::build);
    }

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableHashedArrayMap<K,V>> toImmutableHashedArrayMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableHashedArrayMapBuilder<K,V>, ImmutableHashedArrayMap<K,V>>of(
                ImmutableHashedArrayMapBuilder::<K,V>newMap,
                ImmutableHashedArrayMapBuilder::with,
                ImmutableHashedArrayMapBuilder::merge,
                ImmutableHashedArrayMapBuilder::build);
    }

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableHashedArrayMap<K,V>> toImmutableHashedArrayBiMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableHashedArrayMapBuilder<K,V>, ImmutableHashedArrayMap<K,V>>of(
                ImmutableHashedArrayMapBuilder::<K,V>newBiMap,
                ImmutableHashedArrayMapBuilder::with,
                ImmutableHashedArrayMapBuilder::merge,
                ImmutableHashedArrayMapBuilder::build);
    }

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                ImmutableSortedArrayMapBuilder::newMap,
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayMapComparingKeys(
            final Comparator<? super K> keyCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.newMapComparingKeys(keyCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayMapComparingValues(
            final Comparator<? super V> valCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.newMapComparingValues(valCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayMapComparingKeysAndValues(final Comparator<? super K> keyCmp, final Comparator<? super V> valCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.newMapComparing(keyCmp, valCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayBiMap() {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                ImmutableSortedArrayMapBuilder::<K,V>newBiMap,
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayBiMapComparingKeys(
            final Comparator<? super K> keyCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.<K,V>newBiMapComparingKeys(keyCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayBiMapComparingValues(
            final Comparator<? super V> valCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.<K,V>newBiMapComparingValues(valCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    public static <K,V> Collector<Map.Entry<? extends K,? extends V>, ?, ImmutableSortedArrayMap<K,V>> toImmutableSortedArrayBiMapComparingKeysAndValues(
            final Comparator<? super K> keyCmp, final Comparator<? super V> valCmp) {
        return Collector.<Map.Entry<? extends K,? extends V>, ImmutableSortedArrayMapBuilder<K,V>, ImmutableSortedArrayMap<K,V>>of(
                () -> ImmutableSortedArrayMapBuilder.<K,V>newBiMapComparing(keyCmp, valCmp),
                ImmutableSortedArrayMapBuilder::with,
                ImmutableSortedArrayMapBuilder::merge,
                ImmutableSortedArrayMapBuilder::build);
    }

    public static <K,V> Collector<V, ?, ImmutableSortedArrayPropertyMap<K,V>> toImmutableSortedArrayPropertyMap(
            final Function<? super V,? extends K> keySupplier) {
        return Collector.of(
                () -> ImmutableSortedArrayPropertyMapBuilder.<K,V>newMapWithKeys(keySupplier),
                ImmutableSortedArrayPropertyMapBuilder::with,
                ImmutableSortedArrayPropertyMapBuilder::merge,
                ImmutableSortedArrayPropertyMapBuilder::build);
    }

    public static <K,V> Collector<V, ?, ImmutableSortedArrayPropertyMap<K,V>> toImmutableSortedArrayPropertyMapComparingKeys(
            final Function<? super V,? extends K> keySupplier, final Comparator<? super K> keyCmp) {
        return Collector.of(
                () -> ImmutableSortedArrayPropertyMapBuilder.<K,V>newMapWithKeysComparing(keySupplier, keyCmp),
                ImmutableSortedArrayPropertyMapBuilder::with,
                ImmutableSortedArrayPropertyMapBuilder::merge,
                ImmutableSortedArrayPropertyMapBuilder::build);
    }
}
