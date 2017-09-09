package net.njcull.collections;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestImmutableArrayMap.class,
        TestImmutableArrayBiMap.class,
        TestImmutableArraySet.class,
        TestImmutableHashedArrayMap.class,
        TestImmutableHashedArrayBiMap.class,
        TestImmutableHashedArraySet.class,
        TestImmutableSortedArrayMap.class,
        TestImmutableSortedArrayBiMap.class,
        TestImmutableSortedArraySet.class,
        TestImmutableSortedArrayPropertyMap.class,
        TestImmutableUniSortedArrayMap.class,
        TestImmutableUniSortedArrayBiMap.class
})
public final class TestAll {
    // Nothing to do here
}
