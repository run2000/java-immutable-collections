package net.njcull.collections;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Tests for ImmutableArraySet.
 *
 * @author run2000
 * @version 4/01/2016.
 */
public final class TestImmutableArraySet {

    @Test
    public void testEmptySet() throws Exception {
        Set<String> test = ImmutableArraySet.<String>builder().build();
        Assert.assertFalse(test.contains("3"));
        Assert.assertSame(test, ImmutableArraySet.emptySet());
        Assert.assertTrue(test.isEmpty());
        Assert.assertEquals(0, test.size());
    }

    @Test
    public void testBuildMerge() throws Exception {
        ImmutableArraySetBuilder<String> builder = new ImmutableArraySetBuilder<>();
        ImmutableArraySetBuilder<String> builder2 = new ImmutableArraySetBuilder<>();

        List<String> abc = Arrays.asList("a", "b", "c");
        List<String> defg = Arrays.asList("d", "e", "f", "g");

        builder.with(abc);
        builder2.with(defg);
        builder.merge(builder2);

        ImmutableArraySet<String> set = builder.build();

        Assert.assertEquals(7, set.size());
        Assert.assertEquals("[a, b, c, d, e, f, g]", set.toString());
    }

    @Test
    public void testSetList() throws Exception {
        ImmutableArraySetBuilder<String> builder = new ImmutableArraySetBuilder<String>();
        ImmutableArraySet<String> set = builder
                .with("a", "b", "b", "c", "d", "e")
                .with("b", "a")
                .build();

        Assert.assertFalse(set.contains("0"));
        Assert.assertTrue(set.contains("a"));
        Assert.assertFalse(set.contains("ab"));
        Assert.assertFalse(set.contains("da"));
        Assert.assertFalse(set.contains("g"));

        // Binary search indexes
        Assert.assertEquals(-1, set.indexOf("0"));
        Assert.assertEquals(0, set.indexOf("a"));
        Assert.assertEquals(-1, set.indexOf("ab"));
        Assert.assertEquals(3, set.indexOf("d"));
        Assert.assertEquals(-1, set.indexOf("da"));
        Assert.assertEquals(4, set.indexOf("e"));
        Assert.assertEquals(-1, set.indexOf("g"));

        Assert.assertEquals("e", set.getAtIndex(4));
        Assert.assertEquals("d", set.getAtIndex(3));
        Assert.assertEquals("c", set.getAtIndex(2));
        Assert.assertEquals("b", set.getAtIndex(1));
        Assert.assertEquals("a", set.getAtIndex(0));

        Iterator<String> it = set.iterator();
        Assert.assertEquals("a", it.next());
        Assert.assertEquals("b", it.next());
        Assert.assertEquals("c", it.next());
        Assert.assertEquals("d", it.next());
        Assert.assertEquals("e", it.next());
        Assert.assertFalse(it.hasNext());

        List<String> list = set.asList();
        List<String> arrayList = Arrays.asList("a", "b", "c", "d", "e");

        Assert.assertFalse(list.contains("0"));
        Assert.assertTrue(list.contains("a"));
        Assert.assertFalse(list.contains("ab"));
        Assert.assertFalse(list.contains("da"));
        Assert.assertFalse(list.contains("g"));

        Assert.assertEquals(arrayList, list);

        Assert.assertEquals("e", list.get(4));
        Assert.assertEquals("d", list.get(3));
        Assert.assertEquals("c", list.get(2));
        Assert.assertEquals("b", list.get(1));
        Assert.assertEquals("a", list.get(0));

        // Search indexes using indexOfRange()
        Assert.assertEquals(-1, list.indexOf("0"));
        Assert.assertEquals(0, list.indexOf("a"));
        Assert.assertEquals(-1, list.indexOf("ab"));
        Assert.assertEquals(3, list.indexOf("d"));
        Assert.assertEquals(-1, list.indexOf("da"));
        Assert.assertEquals(4, list.indexOf("e"));
        Assert.assertEquals(-1, list.indexOf("g"));

        it = list.iterator();
        Assert.assertEquals("a", it.next());
        Assert.assertEquals("b", it.next());
        Assert.assertEquals("c", it.next());
        Assert.assertEquals("d", it.next());
        Assert.assertEquals("e", it.next());
        Assert.assertFalse(it.hasNext());

        List<String> subList = list.subList(1, 5);

        Assert.assertFalse(subList.contains("0"));
        Assert.assertFalse(subList.contains("a"));
        Assert.assertFalse(subList.contains("ab"));
        Assert.assertTrue(subList.contains("d"));
        Assert.assertFalse(subList.contains("da"));
        Assert.assertTrue(subList.contains("e"));
        Assert.assertFalse(subList.contains("g"));

        // Search indexes using indexOfRange()
        Assert.assertEquals(-1, subList.indexOf("0"));
        Assert.assertEquals(-1, subList.indexOf("a"));
        Assert.assertEquals(-1, subList.indexOf("ab"));
        Assert.assertEquals(2, subList.indexOf("d"));
        Assert.assertEquals(-1, subList.indexOf("da"));
        Assert.assertEquals(3, subList.indexOf("e"));
        Assert.assertEquals(-1, subList.indexOf("g"));

        List<String> arrayList2 = arrayList.subList(1, 5);
        Assert.assertTrue(arrayList2.equals(subList));
        Assert.assertTrue(subList.equals(arrayList2));

        it = subList.iterator();
        Assert.assertEquals("b", it.next());
        Assert.assertEquals("c", it.next());
        Assert.assertEquals("d", it.next());
        Assert.assertEquals("e", it.next());
        Assert.assertFalse(it.hasNext());

        it = subList.listIterator();
        Assert.assertEquals("b", it.next());
        Assert.assertEquals("c", it.next());
        Assert.assertEquals("d", it.next());
        Assert.assertEquals("e", it.next());
        Assert.assertFalse(it.hasNext());

        Assert.assertEquals("[a, b, c, d, e]", set.toString());
        Assert.assertEquals("[a, b, c, d, e]", list.toString());
        Assert.assertEquals("[b, c, d, e]", subList.toString());
    }

    @Test
    public void testSetListNull() throws Exception {
        ImmutableArraySetBuilder<String> builder = new ImmutableArraySetBuilder<String>();
        ImmutableArraySet<String> set = builder
                .with("a", "b", "b", null, "d", "e")
                .with("b", "a")
                .build();

        Assert.assertFalse(set.contains("0"));
        Assert.assertTrue(set.contains("a"));
        Assert.assertFalse(set.contains("ab"));
        Assert.assertTrue(set.contains(null));
        Assert.assertFalse(set.contains("da"));
        Assert.assertFalse(set.contains("g"));

        // Binary search indexes
        Assert.assertEquals(-1, set.indexOf("0"));
        Assert.assertEquals(0, set.indexOf("a"));
        Assert.assertEquals(-1, set.indexOf("ab"));
        Assert.assertEquals(2, set.indexOf(null));
        Assert.assertEquals(3, set.indexOf("d"));
        Assert.assertEquals(-1, set.indexOf("da"));
        Assert.assertEquals(4, set.indexOf("e"));
        Assert.assertEquals(-1, set.indexOf("g"));

        Assert.assertEquals("e", set.getAtIndex(4));
        Assert.assertEquals("d", set.getAtIndex(3));
        Assert.assertEquals(null, set.getAtIndex(2));
        Assert.assertEquals("b", set.getAtIndex(1));
        Assert.assertEquals("a", set.getAtIndex(0));

        Iterator<String> it = set.iterator();
        Assert.assertEquals("a", it.next());
        Assert.assertEquals("b", it.next());
        Assert.assertEquals(null, it.next());
        Assert.assertEquals("d", it.next());
        Assert.assertEquals("e", it.next());
        Assert.assertFalse(it.hasNext());

        List<String> list = set.asList();
        List<String> arrayList = Arrays.asList("a", "b", null, "d", "e");

        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(5, list.size());
        Assert.assertFalse(list.contains("0"));
        Assert.assertTrue(list.contains("a"));
        Assert.assertFalse(list.contains("ab"));
        Assert.assertTrue(list.contains(null));
        Assert.assertFalse(list.contains("da"));
        Assert.assertFalse(list.contains("g"));

        Assert.assertEquals(arrayList, list);

        Assert.assertEquals("e", list.get(4));
        Assert.assertEquals("d", list.get(3));
        Assert.assertEquals(null, list.get(2));
        Assert.assertEquals("b", list.get(1));
        Assert.assertEquals("a", list.get(0));

        // Search indexes using indexOfRange()
        Assert.assertEquals(-1, list.indexOf("0"));
        Assert.assertEquals(0, list.indexOf("a"));
        Assert.assertEquals(-1, list.indexOf("ab"));
        Assert.assertEquals(2, list.indexOf(null));
        Assert.assertEquals(3, list.indexOf("d"));
        Assert.assertEquals(-1, list.indexOf("da"));
        Assert.assertEquals(4, list.indexOf("e"));
        Assert.assertEquals(-1, list.indexOf("g"));

        it = list.iterator();
        Assert.assertEquals("a", it.next());
        Assert.assertEquals("b", it.next());
        Assert.assertEquals(null, it.next());
        Assert.assertEquals("d", it.next());
        Assert.assertEquals("e", it.next());
        Assert.assertFalse(it.hasNext());

        List<String> subList = list.subList(1, 5);

        Assert.assertFalse(subList.contains("0"));
        Assert.assertFalse(subList.contains("a"));
        Assert.assertFalse(subList.contains("ab"));
        Assert.assertTrue(subList.contains(null));
        Assert.assertTrue(subList.contains("d"));
        Assert.assertFalse(subList.contains("da"));
        Assert.assertTrue(subList.contains("e"));
        Assert.assertFalse(subList.contains("g"));

        // Search indexes using indexOfRange()
        Assert.assertEquals(-1, subList.indexOf("0"));
        Assert.assertEquals(-1, subList.indexOf("a"));
        Assert.assertEquals(-1, subList.indexOf("ab"));
        Assert.assertEquals(1, subList.indexOf(null));
        Assert.assertEquals(2, subList.indexOf("d"));
        Assert.assertEquals(-1, subList.indexOf("da"));
        Assert.assertEquals(3, subList.indexOf("e"));
        Assert.assertEquals(-1, subList.indexOf("g"));

        List<String> arrayList2 = arrayList.subList(1, 5);
        Assert.assertTrue(arrayList2.equals(subList));
        Assert.assertTrue(subList.equals(arrayList2));

        it = subList.iterator();
        Assert.assertEquals("b", it.next());
        Assert.assertEquals(null, it.next());
        Assert.assertEquals("d", it.next());
        Assert.assertEquals("e", it.next());
        Assert.assertFalse(it.hasNext());

        it = subList.listIterator();
        Assert.assertEquals("b", it.next());
        Assert.assertEquals(null, it.next());
        Assert.assertEquals("d", it.next());
        Assert.assertEquals("e", it.next());
        Assert.assertFalse(it.hasNext());

        Assert.assertEquals("[a, b, null, d, e]", set.toString());
        Assert.assertEquals("[a, b, null, d, e]", list.toString());
        Assert.assertEquals("[b, null, d, e]", subList.toString());
    }

    @Test
    public void testCollector() throws Exception {
        List<String> abcdefg = Arrays.asList("d", "e", "Qrst", "f", "a", "abc", "b", "c", "g");

        ArrayBackedSet<String> result = abcdefg.stream()
                .filter(p -> p.length() == 1)
                .collect(Collectors.toImmutableArraySet());

        Assert.assertEquals(7, result.size());

        Assert.assertTrue(result.contains("d"));
        Assert.assertTrue(result.contains("e"));
        Assert.assertFalse(result.contains("Qrst"));
        Assert.assertTrue(result.contains("f"));
        Assert.assertTrue(result.contains("a"));
        Assert.assertFalse(result.contains("abc"));
        Assert.assertTrue(result.contains("b"));
        Assert.assertTrue(result.contains("c"));
        Assert.assertTrue(result.contains("g"));
    }

    @Test
    public void testSplitter() throws Exception {
        List<String> abcdefg = Arrays.asList("d", "e", "Qrst", "f", "a", "abc", "b", "c", "g");

        ArrayBackedSet<String> result1 = abcdefg.stream()
                .filter(p -> p.length() == 1)
                .collect(Collectors.toImmutableArraySet());

        ArrayBackedSet<String> result2 = result1.parallelStream()
                .collect(Collectors.toImmutableArraySet());

        Assert.assertEquals(7, result2.size());

        Assert.assertTrue(result2.contains("d"));
        Assert.assertTrue(result2.contains("e"));
        Assert.assertFalse(result2.contains("Qrst"));
        Assert.assertTrue(result2.contains("f"));
        Assert.assertTrue(result2.contains("a"));
        Assert.assertFalse(result2.contains("abc"));
        Assert.assertTrue(result2.contains("b"));
        Assert.assertTrue(result2.contains("c"));
        Assert.assertTrue(result2.contains("g"));
    }
}
