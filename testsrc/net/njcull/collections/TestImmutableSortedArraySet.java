package net.njcull.collections;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for ImmutableSortedArraySet.
 *
 * @author run2000
 * @version 4/01/2016.
 */
public final class TestImmutableSortedArraySet {

    @Test
    public void testEmptySet() throws Exception {
        ImmutableSortedArraySet<String> test = ImmutableSortedArraySet.<String>builder().build();
        Assert.assertFalse(test.contains("3"));
        Assert.assertSame(test, ImmutableSortedArraySet.emptySet());
        Assert.assertTrue(test.isEmpty());
        Assert.assertEquals(0, test.size());

        Assert.assertEquals("[]", test.toString());
        Assert.assertEquals(0, test.hashCode());
        Assert.assertEquals(1, test.asList().hashCode());
    }

    @Test
    public void testBuildMerge() throws Exception {
        ImmutableSortedArraySetBuilder<String> builder = new ImmutableSortedArraySetBuilder<>();
        ImmutableSortedArraySetBuilder<String> builder2 = new ImmutableSortedArraySetBuilder<>();

        List<String> abc = Arrays.asList("a", "b", "c");
        List<String> defg = Arrays.asList("d", "e", "f", "g");

        builder.with(abc);
        builder2.with(defg);
        builder.merge(builder2);

        ImmutableSortedArraySet<String> set = builder.build();

        Assert.assertEquals(4, builder2.size());
        builder2.clear();
        Assert.assertEquals(0, builder2.size());

        Assert.assertEquals(7, set.size());
        Assert.assertEquals("[a, b, c, d, e, f, g]", set.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetList() throws Exception {
        ImmutableSortedArraySetBuilder<String> builder = new ImmutableSortedArraySetBuilder<>();
        ImmutableSortedArraySet<String> set = builder
                .with("a", "b", "b", "c", "d", "e")
                .with("b", "a")
//                .byComparing(Comparator.reverseOrder())
                .byNaturalOrder()
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

        Assert.assertEquals("a", set.first());
        Assert.assertEquals("e", set.last());

        Assert.assertEquals("e", set.getAtIndex(4));
        Assert.assertEquals("d", set.getAtIndex(3));
        Assert.assertEquals("c", set.getAtIndex(2));
        Assert.assertEquals("b", set.getAtIndex(1));
        Assert.assertEquals("a", set.getAtIndex(0));

        // New methods in 1.8 - forEach, removeIf
        Assert.assertFalse(set.removeIf(e -> e.length() > 1));

        StringBuilder builder2 = new StringBuilder();
        set.forEach(builder2::append);
        Assert.assertEquals("abcde", builder2.toString());

        // toArray()
        Object[] arrAct = set.toArray();
        Object[] arrExp = new Object[] { "a", "b", "c", "d", "e" };
        Assert.assertArrayEquals(arrExp, arrAct);

        // toArray(String[]) -- three cases to consider
        String[] arrAct1 = new String[4];
        String[] arrExp1 = new String[] { "a", "b", "c", "d", "e" };

        String[] arrAct1a = set.toArray(arrAct1);
        Assert.assertNotSame(arrAct, arrAct1);
        Assert.assertArrayEquals(arrExp1, arrAct1a);

        String[] arrAct2 = new String[5];
        String[] arrAct2a = set.toArray(arrAct2);
        Assert.assertSame(arrAct2, arrAct2a);
        Assert.assertArrayEquals(arrExp1, arrAct2);

        String[] arrAct3 = new String[6];
        String[] arrExp3 = new String[] { "a", "b", "c", "d", "e", null };
        String[] arrAct3a = set.toArray(arrAct3);
        Assert.assertSame(arrAct3, arrAct3a);
        Assert.assertArrayEquals(arrExp3, arrAct3);

        Iterator<String> it = set.iterator();
        Assert.assertEquals("a", it.next());
        Assert.assertEquals("b", it.next());
        Assert.assertEquals("c", it.next());
        Assert.assertEquals("d", it.next());
        Assert.assertEquals("e", it.next());
        Assert.assertFalse(it.hasNext());

        Assert.assertNull(set.comparator());
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

        ArrayBackedCollection<String> arrayBacked = (ArrayBackedCollection<String>)list;

        Assert.assertEquals("e", arrayBacked.getAtIndex(4));
        Assert.assertEquals("d", arrayBacked.getAtIndex(3));
        Assert.assertEquals("c", arrayBacked.getAtIndex(2));
        Assert.assertEquals("b", arrayBacked.getAtIndex(1));
        Assert.assertEquals("a", arrayBacked.getAtIndex(0));

        Assert.assertEquals( -1, arrayBacked.indexOfRange(null, 1, 5));
        Assert.assertEquals( -1, arrayBacked.indexOfRange("a", 1, 5));
        Assert.assertEquals( 1, arrayBacked.indexOfRange("b", 1, 5));
        Assert.assertEquals( 2, arrayBacked.indexOfRange("c", 1, 5));
        Assert.assertEquals( 3, arrayBacked.indexOfRange("d", 1, 5));
        Assert.assertEquals( 4, arrayBacked.indexOfRange("e", 1, 5));

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

        // New methods in 1.8 - forEach, removeIf
        Assert.assertFalse(subList.removeIf(e -> e.length() > 1));

        StringBuilder builder3 = new StringBuilder();
        subList.forEach(builder3::append);
        Assert.assertEquals("bcde", builder3.toString());

        // toArray()
        arrAct = subList.toArray();
        arrExp = new Object[] { "b", "c", "d", "e" };
        Assert.assertArrayEquals(arrExp, arrAct);

        // toArray(String[]) -- three cases to consider
        arrAct1 = new String[3];
        arrExp1 = new String[] { "b", "c", "d", "e" };

        arrAct1a = subList.toArray(arrAct1);
        Assert.assertNotSame(arrAct, arrAct1);
        Assert.assertArrayEquals(arrExp1, arrAct1a);

        arrAct2 = new String[4];
        arrAct2a = subList.toArray(arrAct2);
        Assert.assertSame(arrAct2, arrAct2a);
        Assert.assertArrayEquals(arrExp1, arrAct2);

        arrAct3 = new String[5];
        arrExp3 = new String[] { "b", "c", "d", "e", null };
        arrAct3a = subList.toArray(arrAct3);
        Assert.assertSame(arrAct3, arrAct3a);
        Assert.assertArrayEquals(arrExp3, arrAct3);

        // Assert sublists implement equals correctly
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

        Set<String> subSet = set.headSet("c");
        Assert.assertEquals("[a, b]", subSet.toString());
        it = subSet.iterator();
        Assert.assertEquals("a", it.next());
        Assert.assertEquals("b", it.next());
        Assert.assertFalse(it.hasNext());

        subSet = set.headSet("ca");
        Assert.assertEquals("[a, b, c]", subSet.toString());
        it = subSet.iterator();
        Assert.assertEquals("a", it.next());
        Assert.assertEquals("b", it.next());
        Assert.assertEquals("c", it.next());
        Assert.assertFalse(it.hasNext());

        subSet = set.headSet("a");
        Assert.assertTrue(subSet.isEmpty());
        Assert.assertSame(ImmutableSortedArraySet.emptySet(), subSet);

        subSet = set.headSet("g");
        Assert.assertEquals(5, subSet.size());
        Assert.assertSame(set, subSet);

        subSet = set.tailSet("ca");
        Assert.assertEquals("[d, e]", subSet.toString());
        it = subSet.iterator();
        Assert.assertEquals("d", it.next());
        Assert.assertEquals("e", it.next());
        Assert.assertFalse(it.hasNext());

        subSet = set.tailSet("a");
        Assert.assertEquals(5, subSet.size());

        subSet = set.tailSet("g");
        Assert.assertTrue(subSet.isEmpty());

        subSet = set.subSet("bc", "e");
        Assert.assertEquals("[c, d]", subSet.toString());
        it = subSet.iterator();
        Assert.assertEquals("c", it.next());
        Assert.assertEquals("d", it.next());
        Assert.assertFalse(it.hasNext());

        subSet = set.subSet("bc", "c");
        Assert.assertEquals("[]", subSet.toString());
        Assert.assertSame(subSet, ImmutableSortedArraySet.emptySet());
        it = subSet.iterator();
        Assert.assertFalse(it.hasNext());

        subSet = set.subSet("a", "g");
        Assert.assertEquals(5, subSet.size());
        Assert.assertSame(subSet, set);
    }

    @Test
    public void testCollector() throws Exception {
        List<String> abcdefg = Arrays.asList("d", "e", "Qrst", "f", "a", "abc", "b", "c", "g");

        ArrayBackedSet<String> result = abcdefg.stream()
                .filter(p -> p.length() == 1)
                .collect(Collectors.toImmutableSortedArraySet());

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

        Assert.assertEquals(3, result.indexOf("d"));
        Assert.assertEquals(4, result.indexOf("e"));
        Assert.assertEquals(5, result.indexOf("f"));
        Assert.assertEquals(0, result.indexOf("a"));
        Assert.assertEquals(1, result.indexOf("b"));
        Assert.assertEquals(2, result.indexOf("c"));
        Assert.assertEquals(6, result.indexOf("g"));
    }

    @Test
    public void testSplitter() throws Exception {
        List<String> abcdefg = Arrays.asList("d", "e", "Qrst", "f", "a", "abc", "b", "c", "g");

        ArrayBackedSet<String> result1 = abcdefg.stream()
                .filter(p -> p.length() == 1)
                .collect(Collectors.toImmutableSortedArraySet());

        ArrayBackedSet<String> result2 = result1.parallelStream()
                .collect(Collectors.toImmutableSortedArraySet());

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

        Assert.assertEquals(3, result2.indexOf("d"));
        Assert.assertEquals(4, result2.indexOf("e"));
        Assert.assertEquals(5, result2.indexOf("f"));
        Assert.assertEquals(0, result2.indexOf("a"));
        Assert.assertEquals(1, result2.indexOf("b"));
        Assert.assertEquals(2, result2.indexOf("c"));
        Assert.assertEquals(6, result2.indexOf("g"));
    }

    @Test
    public void testExceptions() throws Exception {
        ImmutableSortedArraySetBuilder<String> builder = new ImmutableSortedArraySetBuilder<>();
        ImmutableSortedArraySet<String> set = builder
                .with("a", "b", "b", "c", "d", "e")
                .with("f", "g")
                .build();

        try {
            Assert.assertTrue(set.remove("c"));
            Assert.fail("Remove of existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Assert.assertTrue(set.add("k"));
            Assert.fail("Put operation for existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            set.clear();
            Assert.fail("Clear operation for non-empty set should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Set<String> s = Collections.singleton("j");
            set.addAll(s);
            Assert.fail("addAll operation should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            List<String> s = Collections.singletonList("g");
            set.removeAll(s);
            Assert.fail("removeAll operation should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            List<String> s = Collections.singletonList("e");
            set.retainAll(s);
            Assert.fail("retainAll operation should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Assert.assertFalse(set.removeIf(e -> e.charAt(0) > 'c'));
            Assert.fail("removeIf operation should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        // No exception, since no elements to add
        try {
            List<String> result = set.asList();
            Assert.assertFalse(result.removeIf(e -> e.charAt(0) > 'c'));
            Assert.fail("removeIf operation should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        // No exception, since no elements to add
        Assert.assertFalse(set.addAll(Collections.emptyList()));

        // No exception, since no elements removed
        List<String> s = Collections.singletonList("k");
        Assert.assertFalse(set.removeAll(s));

        // No exception, since all elements retained
        s = Arrays.<String>asList("a", "b", "c", "d", "e", "f", "g");
        Assert.assertFalse(set.retainAll(s));

        // No exception, since clearing an empty collection does nothing
        ImmutableSortedArraySet.emptySet().clear();

        // The list returned from keySet().asList() is itself an ArrayBackedCollection
        ArrayBackedCollection<String> list1 = (ArrayBackedCollection<String>)set.asList();
        List<String> list2 = list1.asList();
        Assert.assertSame(list1, list2);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerialization() throws Exception {
        ImmutableSortedArraySetBuilder<String> builder = new ImmutableSortedArraySetBuilder<>();
        ImmutableSortedArraySet<String> set = builder
                .with("a", "b", "c", "d")
                .with( "e", "f", "g")
                .build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(set);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        ImmutableSortedArraySet<String> set2 = (ImmutableSortedArraySet<String>) ois.readObject();
        Assert.assertEquals("[a, b, c, d, e, f, g]", set2.toString());
        Assert.assertEquals(7, set2.size());
        Assert.assertNotSame(set, set2);

        Assert.assertEquals(2, set2.indexOf("c"));
        Assert.assertEquals(6, set2.indexOf("g"));

        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);

        oos.writeObject(ImmutableSortedArraySet.emptySet());

        bais = new ByteArrayInputStream(baos.toByteArray());
        ois = new ObjectInputStream(bais);

        set2 = (ImmutableSortedArraySet<String>) ois.readObject();
        Assert.assertEquals("[]", set2.toString());
        Assert.assertEquals(0, set2.size());
        Assert.assertSame(ImmutableSortedArraySet.emptySet(), set2);
    }
}
