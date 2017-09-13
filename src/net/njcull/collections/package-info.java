/**
 * Immutable collection classes and related utilities for Java 1.8.
 * Implements immutable collections, created with a corresponding builder
 * class.
 * <p>
 * There are sorted and unsorted variations of sets and maps, with ordering
 * facilities on the builder, as well as map and bi-map variations.
 * Sorted variations are searched using a binary search algorithm.
 * Most operations are optimized for index-based lookups, including the
 * usual collection convenience methods.
 * <p>
 * There are also stream spliterators and collectors optimized for these
 * collections.
 *
 * @author run2000
 * @version 13/09/2017.
 */
package net.njcull.collections;