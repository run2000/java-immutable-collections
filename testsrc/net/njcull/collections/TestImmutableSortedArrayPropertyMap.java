package net.njcull.collections;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Tests for ImmutableSortedArrayPropertyMap.
 *
 * @author NCULL
 * @version 1/09/2017 12:23 PM.
 */
public class TestImmutableSortedArrayPropertyMap {

    private static final Comparator<String> reverseComparator =
            Comparator.reverseOrder();

    @Test
    public void testEmptyMap() throws Exception {
        Map<String, String> test = ImmutableSortedArrayPropertyMap.<String, String>builder().build();
        Assert.assertFalse(test.containsKey("3"));
        Assert.assertSame(test, ImmutableSortedArrayPropertyMap.emptyMap());
        Assert.assertTrue(test.isEmpty());
        Assert.assertEquals(0, test.size());
    }

    @Test
    public void testSimplePropertyMap() throws Exception {
        ImmutableSortedArrayPropertyMapBuilder<String, TestClassWithProperty<String>> builder =
                ImmutableSortedArrayPropertyMapBuilder.newMapWithKeys(TestClassWithProperty::getName);

        builder.with(new TestClassWithProperty<>("f", "ac"));
        builder.with(new TestClassWithProperty<>("b", "bc"));
        builder.with(new TestClassWithProperty<>("c", "cc"));
        builder.with(new TestClassWithProperty<>("d", "dx"));
        builder.with(new TestClassWithProperty<>("e", "ec"));
        builder.with(new TestClassWithProperty<>("a", "fc"));
        builder.with(new TestClassWithProperty<>("g", "gc"));
        builder.with(new TestClassWithProperty<>("o", "0ma"));
        builder.with(new TestClassWithProperty<>("n", "0na"));
        builder.with(new TestClassWithProperty<>("m", "Zoa"));
        builder.with(new TestClassWithProperty<>("p", "Zpa"));

        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> map = builder.build();
        Assert.assertEquals(11, map.size());
        Assert.assertEquals("{a=fc, b=bc, c=cc, d=dx, e=ec, f=ac, g=gc, m=Zoa, n=0na, o=0ma, p=Zpa}", map.toString());
        System.out.println(map.toString());
    }

    @Test
    public void testSimplePropertyMapStatic() throws Exception {
        ImmutableSortedArrayPropertyMapBuilder<String, TestClassWithProperty<String>> builder =
                ImmutableSortedArrayPropertyMapBuilder.newMapWithKeys(TestImmutableSortedArrayPropertyMap::extractKey);

        builder.with(new TestClassWithProperty<>("f", "ac"));
        builder.with(new TestClassWithProperty<>("b", "bc"));
        builder.with(new TestClassWithProperty<>("c", "cc"));
        builder.with(new TestClassWithProperty<>(null, "dx"));
        builder.with(new TestClassWithProperty<>("e", "ec"));
        builder.with(new TestClassWithProperty<>("a", null));
        builder.with(new TestClassWithProperty<>("g", "gc"));
        builder.with(new TestClassWithProperty<>("o", "0ma"));
        builder.with(new TestClassWithProperty<>("n", "0na"));
        builder.with(new TestClassWithProperty<>("m", "Zoa"));
        builder.with(new TestClassWithProperty<>("p", "Zpa"));

        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> map = builder.build();
        Assert.assertEquals(11, map.size());
        Assert.assertEquals("{null=null, ac=ac, bc=bc, cc=cc, dx=dx, ec=ec, gc=gc, ma=0ma, na=0na, oa=Zoa, pa=Zpa}", map.toString());
        System.out.println(map.toString());
    }

    @Test
    public void testCollector() throws Exception {
        Collection<TestClassWithProperty<String>> coll1 = new HashSet<>();
        coll1.add(new TestClassWithProperty<>("a", "ac"));
        coll1.add(new TestClassWithProperty<>("b", "bc"));
        coll1.add(new TestClassWithProperty<>("c", "cc"));
        coll1.add(new TestClassWithProperty<>("d", "dx"));
        coll1.add(new TestClassWithProperty<>("e", "ec"));
        coll1.add(new TestClassWithProperty<>("f", "fc"));
        coll1.add(new TestClassWithProperty<>("g", "gc"));
        coll1.add(new TestClassWithProperty<>("m", "0ma"));
        coll1.add(new TestClassWithProperty<>("n", "0na"));
        coll1.add(new TestClassWithProperty<>("o", "Zoa"));
        coll1.add(new TestClassWithProperty<>("p", "Zpa"));

        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> result =
                coll1.stream()
                        .filter(p -> p.getValue().charAt(1) != 'x')
                        .collect(Collectors.toImmutableSortedArrayPropertyMap(TestClassWithProperty::getName));

        Assert.assertEquals(10, result.size());
        Assert.assertEquals("{a=ac, b=bc, c=cc, e=ec, f=fc, g=gc, m=0ma, n=0na, o=Zoa, p=Zpa}", result.toString());

        Assert.assertEquals(0, result.indexOfKey("a"));
        Assert.assertEquals(1, result.indexOfKey("b"));
        Assert.assertEquals(2, result.indexOfKey("c"));
        Assert.assertEquals(3, result.indexOfKey("e"));
        Assert.assertEquals(4, result.indexOfKey("f"));
        Assert.assertEquals(5, result.indexOfKey("g"));
        Assert.assertEquals(6, result.indexOfKey("m"));
        Assert.assertEquals(7, result.indexOfKey("n"));
        Assert.assertEquals(8, result.indexOfKey("o"));
        Assert.assertEquals(9, result.indexOfKey("p"));
        Assert.assertEquals(-1, result.indexOfKey("0"));

        Assert.assertTrue(result.containsKey("a"));
        Assert.assertTrue(result.containsKey("b"));
        Assert.assertTrue(result.containsKey("c"));
        Assert.assertFalse(result.containsKey("d"));
        Assert.assertTrue(result.containsKey("e"));
        Assert.assertTrue(result.containsKey("f"));
        Assert.assertTrue(result.containsKey("g"));
        Assert.assertTrue(result.containsKey("m"));
        Assert.assertTrue(result.containsKey("n"));
        Assert.assertTrue(result.containsKey("o"));
        Assert.assertTrue(result.containsKey("p"));
        Assert.assertFalse(result.containsKey("0"));

        Assert.assertEquals("a", result.keyAt(0));
        Assert.assertEquals("b", result.keyAt(1));
        Assert.assertEquals("c", result.keyAt(2));
        Assert.assertEquals("e", result.keyAt(3));
        Assert.assertEquals("f", result.keyAt(4));
        Assert.assertEquals("g", result.keyAt(5));
        Assert.assertEquals("m", result.keyAt(6));
        Assert.assertEquals("n", result.keyAt(7));
        Assert.assertEquals("o", result.keyAt(8));
        Assert.assertEquals("p", result.keyAt(9));

        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("a", "ac")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("b", "bc")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("c", "cc")));
        Assert.assertFalse(result.containsValue(new TestClassWithProperty<>("d", "dx")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("e", "ec")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("f", "fc")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("g", "gc")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("m", "0ma")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("n", "0na")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("o", "Zoa")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("p", "Zpa")));
        Assert.assertFalse(result.containsValue(new TestClassWithProperty<String>("0", null)));

        Assert.assertEquals(0, result.indexOfValue(new TestClassWithProperty<>("a", "ac")));
        Assert.assertEquals(1, result.indexOfValue(new TestClassWithProperty<>("b", "bc")));
        Assert.assertEquals(2, result.indexOfValue(new TestClassWithProperty<>("c", "cc")));
        Assert.assertEquals(3, result.indexOfValue(new TestClassWithProperty<>("e", "ec")));
        Assert.assertEquals(4, result.indexOfValue(new TestClassWithProperty<>("f", "fc")));
        Assert.assertEquals(5, result.indexOfValue(new TestClassWithProperty<>("g", "gc")));
        Assert.assertEquals(6, result.indexOfValue(new TestClassWithProperty<>("m", "0ma")));
        Assert.assertEquals(7, result.indexOfValue(new TestClassWithProperty<>("n", "0na")));
        Assert.assertEquals(8, result.indexOfValue(new TestClassWithProperty<>("o", "Zoa")));
        Assert.assertEquals(9, result.indexOfValue(new TestClassWithProperty<>("p", "Zpa")));
        Assert.assertEquals(-1, result.indexOfValue(new TestClassWithProperty<String>("0", null)));

        Assert.assertEquals("ac", result.valueAt(0).getValue());
        Assert.assertEquals("bc", result.valueAt(1).getValue());
        Assert.assertEquals("cc", result.valueAt(2).getValue());
        Assert.assertEquals("ec", result.valueAt(3).getValue());
        Assert.assertEquals("fc", result.valueAt(4).getValue());
        Assert.assertEquals("gc", result.valueAt(5).getValue());
        Assert.assertEquals("0ma", result.valueAt(6).getValue());
        Assert.assertEquals("0na", result.valueAt(7).getValue());
        Assert.assertEquals("Zoa", result.valueAt(8).getValue());
        Assert.assertEquals("Zpa", result.valueAt(9).getValue());

        Map.Entry<String, TestClassWithProperty<String>> entry = result.entryAt(0);

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue().getValue());

        entry = result.entryAt(1);
        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue().getValue());

        entry = result.entryAt(2);
        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue().getValue());

        entry = result.entryAt(3);
        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue().getValue());

        entry = result.entryAt(4);
        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue().getValue());

        entry = result.entryAt(5);
        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue().getValue());

        entry = result.entryAt(6);
        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue().getValue());

        entry = result.entryAt(7);
        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue().getValue());

        entry = result.entryAt(8);
        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue().getValue());

        entry = result.entryAt(9);
        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue().getValue());

        Assert.assertEquals("ac", result.get("a").getValue());
        Assert.assertEquals("bc", result.get("b").getValue());
        Assert.assertEquals("cc", result.get("c").getValue());
        Assert.assertEquals("ec", result.get("e").getValue());
        Assert.assertEquals("fc", result.get("f").getValue());
        Assert.assertEquals("gc", result.get("g").getValue());
        Assert.assertEquals("0ma", result.get("m").getValue());
        Assert.assertEquals("0na", result.get("n").getValue());
        Assert.assertEquals("Zoa", result.get("o").getValue());
        Assert.assertEquals("Zpa", result.get("p").getValue());
        Assert.assertSame(null, result.get("0"));

        ArrayBackedSet<Map.Entry<String, TestClassWithProperty<String>>> entrySet = result.entrySet();
        Iterator<Map.Entry<String, TestClassWithProperty<String>>> entryIt = entrySet.iterator();

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue().getValue());

        Assert.assertFalse(entryIt.hasNext());

    }

    @Test
    public void testSplitter() throws Exception {
        Collection<TestClassWithProperty<String>> coll1 = new HashSet<>();
        coll1.add(new TestClassWithProperty<>("a", "ac"));
        coll1.add(new TestClassWithProperty<>("b", "bc"));
        coll1.add(new TestClassWithProperty<>("c", "cc"));
        coll1.add(new TestClassWithProperty<>("d", "dx"));
        coll1.add(new TestClassWithProperty<>("e", "ec"));
        coll1.add(new TestClassWithProperty<>("f", "fc"));
        coll1.add(new TestClassWithProperty<>("g", "gc"));
        coll1.add(new TestClassWithProperty<>("m", "0ma"));
        coll1.add(new TestClassWithProperty<>("n", "0na"));
        coll1.add(new TestClassWithProperty<>("o", "Zoa"));
        coll1.add(new TestClassWithProperty<>("p", "Zpa"));

        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> result1 =
                coll1.stream()
                        .filter(p -> p.getValue().charAt(1) != 'x')
                        .collect(Collectors.toImmutableSortedArrayPropertyMap(TestClassWithProperty::getName));

        // Stream the results from one map, collect back to another
        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> result2 =
                result1.values().parallelStream()
                        .collect(Collectors.toImmutableSortedArrayPropertyMap(TestClassWithProperty::getName));

        Assert.assertEquals(10, result2.size());
        Assert.assertEquals("{a=ac, b=bc, c=cc, e=ec, f=fc, g=gc, m=0ma, n=0na, o=Zoa, p=Zpa}", result2.toString());

        Assert.assertEquals(0, result2.indexOfKey("a"));
        Assert.assertEquals(1, result2.indexOfKey("b"));
        Assert.assertEquals(2, result2.indexOfKey("c"));
        Assert.assertEquals(3, result2.indexOfKey("e"));
        Assert.assertEquals(4, result2.indexOfKey("f"));
        Assert.assertEquals(5, result2.indexOfKey("g"));
        Assert.assertEquals(6, result2.indexOfKey("m"));
        Assert.assertEquals(7, result2.indexOfKey("n"));
        Assert.assertEquals(8, result2.indexOfKey("o"));
        Assert.assertEquals(9, result2.indexOfKey("p"));
        Assert.assertEquals(-1, result2.indexOfKey("0"));

        Assert.assertTrue(result2.containsKey("a"));
        Assert.assertTrue(result2.containsKey("b"));
        Assert.assertTrue(result2.containsKey("c"));
        Assert.assertFalse(result2.containsKey("d"));
        Assert.assertTrue(result2.containsKey("e"));
        Assert.assertTrue(result2.containsKey("f"));
        Assert.assertTrue(result2.containsKey("g"));
        Assert.assertTrue(result2.containsKey("m"));
        Assert.assertTrue(result2.containsKey("n"));
        Assert.assertTrue(result2.containsKey("o"));
        Assert.assertTrue(result2.containsKey("p"));
        Assert.assertFalse(result2.containsKey("0"));

        Assert.assertEquals("a", result2.keyAt(0));
        Assert.assertEquals("b", result2.keyAt(1));
        Assert.assertEquals("c", result2.keyAt(2));
        Assert.assertEquals("e", result2.keyAt(3));
        Assert.assertEquals("f", result2.keyAt(4));
        Assert.assertEquals("g", result2.keyAt(5));
        Assert.assertEquals("m", result2.keyAt(6));
        Assert.assertEquals("n", result2.keyAt(7));
        Assert.assertEquals("o", result2.keyAt(8));
        Assert.assertEquals("p", result2.keyAt(9));

        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("a", "ac")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("b", "bc")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("c", "cc")));
        Assert.assertFalse(result2.containsValue(new TestClassWithProperty<>("d", "dx")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("e", "ec")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("f", "fc")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("g", "gc")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("m", "0ma")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("n", "0na")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("o", "Zoa")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("p", "Zpa")));
        Assert.assertFalse(result2.containsValue(new TestClassWithProperty<String>("0", null)));

        Assert.assertEquals(0, result2.indexOfValue(new TestClassWithProperty<>("a", "ac")));
        Assert.assertEquals(1, result2.indexOfValue(new TestClassWithProperty<>("b", "bc")));
        Assert.assertEquals(2, result2.indexOfValue(new TestClassWithProperty<>("c", "cc")));
        Assert.assertEquals(3, result2.indexOfValue(new TestClassWithProperty<>("e", "ec")));
        Assert.assertEquals(4, result2.indexOfValue(new TestClassWithProperty<>("f", "fc")));
        Assert.assertEquals(5, result2.indexOfValue(new TestClassWithProperty<>("g", "gc")));
        Assert.assertEquals(6, result2.indexOfValue(new TestClassWithProperty<>("m", "0ma")));
        Assert.assertEquals(7, result2.indexOfValue(new TestClassWithProperty<>("n", "0na")));
        Assert.assertEquals(8, result2.indexOfValue(new TestClassWithProperty<>("o", "Zoa")));
        Assert.assertEquals(9, result2.indexOfValue(new TestClassWithProperty<>("p", "Zpa")));
        Assert.assertEquals(-1, result2.indexOfValue(new TestClassWithProperty<String>("0", null)));

        Assert.assertEquals("ac", result2.valueAt(0).getValue());
        Assert.assertEquals("bc", result2.valueAt(1).getValue());
        Assert.assertEquals("cc", result2.valueAt(2).getValue());
        Assert.assertEquals("ec", result2.valueAt(3).getValue());
        Assert.assertEquals("fc", result2.valueAt(4).getValue());
        Assert.assertEquals("gc", result2.valueAt(5).getValue());
        Assert.assertEquals("0ma", result2.valueAt(6).getValue());
        Assert.assertEquals("0na", result2.valueAt(7).getValue());
        Assert.assertEquals("Zoa", result2.valueAt(8).getValue());
        Assert.assertEquals("Zpa", result2.valueAt(9).getValue());

        Map.Entry<String, TestClassWithProperty<String>> entry = result2.entryAt(0);

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue().getValue());

        entry = result2.entryAt(1);
        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue().getValue());

        entry = result2.entryAt(2);
        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue().getValue());

        entry = result2.entryAt(3);
        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue().getValue());

        entry = result2.entryAt(4);
        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue().getValue());

        entry = result2.entryAt(5);
        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue().getValue());

        entry = result2.entryAt(6);
        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue().getValue());

        entry = result2.entryAt(7);
        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue().getValue());

        entry = result2.entryAt(8);
        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue().getValue());

        entry = result2.entryAt(9);
        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue().getValue());

        Assert.assertEquals("ac", result2.get("a").getValue());
        Assert.assertEquals("bc", result2.get("b").getValue());
        Assert.assertEquals("cc", result2.get("c").getValue());
        Assert.assertEquals("ec", result2.get("e").getValue());
        Assert.assertEquals("fc", result2.get("f").getValue());
        Assert.assertEquals("gc", result2.get("g").getValue());
        Assert.assertEquals("0ma", result2.get("m").getValue());
        Assert.assertEquals("0na", result2.get("n").getValue());
        Assert.assertEquals("Zoa", result2.get("o").getValue());
        Assert.assertEquals("Zpa", result2.get("p").getValue());
        Assert.assertSame(null, result2.get("0"));

        ArrayBackedSet<Map.Entry<String, TestClassWithProperty<String>>> entrySet = result2.entrySet();
        Iterator<Map.Entry<String, TestClassWithProperty<String>>> entryIt = entrySet.iterator();

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue().getValue());

        Assert.assertFalse(entryIt.hasNext());

    }

    @Test
    public void testKeySplitter() throws Exception {
        Collection<TestClassWithProperty<String>> coll1 = new HashSet<>();
        coll1.add(new TestClassWithProperty<>("a", "ac"));
        coll1.add(new TestClassWithProperty<>("b", "bc"));
        coll1.add(new TestClassWithProperty<>("c", "cc"));
        coll1.add(new TestClassWithProperty<>("d", "dx"));
        coll1.add(new TestClassWithProperty<>("e", "ec"));
        coll1.add(new TestClassWithProperty<>("f", "fc"));
        coll1.add(new TestClassWithProperty<>("g", "gc"));
        coll1.add(new TestClassWithProperty<>("m", "0ma"));
        coll1.add(new TestClassWithProperty<>("n", "0na"));
        coll1.add(new TestClassWithProperty<>("o", "Zoa"));
        coll1.add(new TestClassWithProperty<>("p", "Zpa"));

        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> result1 =
                coll1.stream()
                        .filter(p -> p.getValue().charAt(1) != 'x')
                        .collect(Collectors.toImmutableSortedArrayPropertyMap(TestClassWithProperty::getName));

        // Stream the results from one map, collect back to an arraylist
        ArrayList<String> result2 =
                result1.keySet().parallelStream()
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        Assert.assertEquals(10, result2.size());
        Assert.assertEquals("[a, b, c, e, f, g, m, n, o, p]", result2.toString());

        Assert.assertEquals(0, result2.indexOf("a"));
        Assert.assertEquals(1, result2.indexOf("b"));
        Assert.assertEquals(2, result2.indexOf("c"));
        Assert.assertEquals(3, result2.indexOf("e"));
        Assert.assertEquals(4, result2.indexOf("f"));
        Assert.assertEquals(5, result2.indexOf("g"));
        Assert.assertEquals(6, result2.indexOf("m"));
        Assert.assertEquals(7, result2.indexOf("n"));
        Assert.assertEquals(8, result2.indexOf("o"));
        Assert.assertEquals(9, result2.indexOf("p"));
        Assert.assertEquals(-1, result2.indexOf("0"));

        Assert.assertTrue(result2.contains("a"));
        Assert.assertTrue(result2.contains("b"));
        Assert.assertTrue(result2.contains("c"));
        Assert.assertFalse(result2.contains("d"));
        Assert.assertTrue(result2.contains("e"));
        Assert.assertTrue(result2.contains("f"));
        Assert.assertTrue(result2.contains("g"));
        Assert.assertTrue(result2.contains("m"));
        Assert.assertTrue(result2.contains("n"));
        Assert.assertTrue(result2.contains("o"));
        Assert.assertTrue(result2.contains("p"));
        Assert.assertFalse(result2.contains("0"));

        Assert.assertEquals("a", result2.get(0));
        Assert.assertEquals("b", result2.get(1));
        Assert.assertEquals("c", result2.get(2));
        Assert.assertEquals("e", result2.get(3));
        Assert.assertEquals("f", result2.get(4));
        Assert.assertEquals("g", result2.get(5));
        Assert.assertEquals("m", result2.get(6));
        Assert.assertEquals("n", result2.get(7));
        Assert.assertEquals("o", result2.get(8));
        Assert.assertEquals("p", result2.get(9));

    }

    @Test
    public void testValueSplitter() throws Exception {
        Collection<TestClassWithProperty<String>> coll1 = new HashSet<>();
        coll1.add(new TestClassWithProperty<>("a", "ac"));
        coll1.add(new TestClassWithProperty<>("b", "bc"));
        coll1.add(new TestClassWithProperty<>("c", "cc"));
        coll1.add(new TestClassWithProperty<>("d", "dx"));
        coll1.add(new TestClassWithProperty<>("e", "ec"));
        coll1.add(new TestClassWithProperty<>("f", "fc"));
        coll1.add(new TestClassWithProperty<>("g", "gc"));
        coll1.add(new TestClassWithProperty<>("m", "0ma"));
        coll1.add(new TestClassWithProperty<>("n", "0na"));
        coll1.add(new TestClassWithProperty<>("o", "Zoa"));
        coll1.add(new TestClassWithProperty<>("p", "Zpa"));

        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> result1 =
                coll1.stream()
                        .filter(p -> p.getValue().charAt(1) != 'x')
                        .collect(Collectors.toImmutableSortedArrayPropertyMap(TestClassWithProperty::getName));

        // Stream the results from one map, collect back to another
        ArrayList<TestClassWithProperty<String>> result2 =
                result1.values().parallelStream()
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        Assert.assertEquals(10, result2.size());
        Assert.assertEquals("[ac, bc, cc, ec, fc, gc, 0ma, 0na, Zoa, Zpa]", result2.toString());

        Assert.assertTrue(result2.contains(new TestClassWithProperty<>("a", "ac")));
        Assert.assertTrue(result2.contains(new TestClassWithProperty<>("b", "bc")));
        Assert.assertTrue(result2.contains(new TestClassWithProperty<>("c", "cc")));
        Assert.assertFalse(result2.contains(new TestClassWithProperty<>("d", "dx")));
        Assert.assertTrue(result2.contains(new TestClassWithProperty<>("e", "ec")));
        Assert.assertTrue(result2.contains(new TestClassWithProperty<>("f", "fc")));
        Assert.assertTrue(result2.contains(new TestClassWithProperty<>("g", "gc")));
        Assert.assertTrue(result2.contains(new TestClassWithProperty<>("m", "0ma")));
        Assert.assertTrue(result2.contains(new TestClassWithProperty<>("n", "0na")));
        Assert.assertTrue(result2.contains(new TestClassWithProperty<>("o", "Zoa")));
        Assert.assertTrue(result2.contains(new TestClassWithProperty<>("p", "Zpa")));
        Assert.assertFalse(result2.contains(new TestClassWithProperty<String>("0", null)));

        Assert.assertEquals(0, result2.indexOf(new TestClassWithProperty<>("a", "ac")));
        Assert.assertEquals(1, result2.indexOf(new TestClassWithProperty<>("b", "bc")));
        Assert.assertEquals(2, result2.indexOf(new TestClassWithProperty<>("c", "cc")));
        Assert.assertEquals(3, result2.indexOf(new TestClassWithProperty<>("e", "ec")));
        Assert.assertEquals(4, result2.indexOf(new TestClassWithProperty<>("f", "fc")));
        Assert.assertEquals(5, result2.indexOf(new TestClassWithProperty<>("g", "gc")));
        Assert.assertEquals(6, result2.indexOf(new TestClassWithProperty<>("m", "0ma")));
        Assert.assertEquals(7, result2.indexOf(new TestClassWithProperty<>("n", "0na")));
        Assert.assertEquals(8, result2.indexOf(new TestClassWithProperty<>("o", "Zoa")));
        Assert.assertEquals(9, result2.indexOf(new TestClassWithProperty<>("p", "Zpa")));
        Assert.assertEquals(-1, result2.indexOf(new TestClassWithProperty<String>("0", null)));

        Assert.assertEquals("ac", result2.get(0).getValue());
        Assert.assertEquals("bc", result2.get(1).getValue());
        Assert.assertEquals("cc", result2.get(2).getValue());
        Assert.assertEquals("ec", result2.get(3).getValue());
        Assert.assertEquals("fc", result2.get(4).getValue());
        Assert.assertEquals("gc", result2.get(5).getValue());
        Assert.assertEquals("0ma", result2.get(6).getValue());
        Assert.assertEquals("0na", result2.get(7).getValue());
        Assert.assertEquals("Zoa", result2.get(8).getValue());
        Assert.assertEquals("Zpa", result2.get(9).getValue());

    }

    @Test
    public void testEntrySplitter() throws Exception {
        Collection<TestClassWithProperty<String>> coll1 = new HashSet<>();
        coll1.add(new TestClassWithProperty<>("a", "ac"));
        coll1.add(new TestClassWithProperty<>("b", "bc"));
        coll1.add(new TestClassWithProperty<>("c", "cc"));
        coll1.add(new TestClassWithProperty<>("d", "dx"));
        coll1.add(new TestClassWithProperty<>("e", "ec"));
        coll1.add(new TestClassWithProperty<>("f", "fc"));
        coll1.add(new TestClassWithProperty<>("g", "gc"));
        coll1.add(new TestClassWithProperty<>("m", "0ma"));
        coll1.add(new TestClassWithProperty<>("n", "0na"));
        coll1.add(new TestClassWithProperty<>("o", "Zoa"));
        coll1.add(new TestClassWithProperty<>("p", "Zpa"));

        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> result1 =
                coll1.stream()
                        .filter(p -> p.getValue().charAt(1) != 'x')
                        .collect(Collectors.toImmutableSortedArrayPropertyMap(TestClassWithProperty::getName));

        // Stream the results from one map, collect back to another
        Map<String, TestClassWithProperty<String>> result2 =
                result1.entrySet().parallelStream()
                        .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Assert.assertTrue(result2.containsKey("a"));
        Assert.assertTrue(result2.containsKey("b"));
        Assert.assertTrue(result2.containsKey("c"));
        Assert.assertFalse(result2.containsKey("d"));
        Assert.assertTrue(result2.containsKey("e"));
        Assert.assertTrue(result2.containsKey("f"));
        Assert.assertTrue(result2.containsKey("g"));
        Assert.assertTrue(result2.containsKey("m"));
        Assert.assertTrue(result2.containsKey("n"));
        Assert.assertTrue(result2.containsKey("o"));
        Assert.assertTrue(result2.containsKey("p"));
        Assert.assertFalse(result2.containsKey("0"));

        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("a", "ac")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("b", "bc")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("c", "cc")));
        Assert.assertFalse(result2.containsValue(new TestClassWithProperty<>("d", "dx")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("e", "ec")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("f", "fc")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("g", "gc")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("m", "0ma")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("n", "0na")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("o", "Zoa")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("p", "Zpa")));
        Assert.assertFalse(result2.containsValue(new TestClassWithProperty<String>("0", null)));

    }

    @Test
    public void testCollectorWithComparator() throws Exception {
        Collection<TestClassWithProperty<String>> coll1 = new HashSet<>();
        coll1.add(new TestClassWithProperty<>("a", "ac"));
        coll1.add(new TestClassWithProperty<>("b", "bc"));
        coll1.add(new TestClassWithProperty<>("c", "cc"));
        coll1.add(new TestClassWithProperty<>("d", "dx"));
        coll1.add(new TestClassWithProperty<>("e", "ec"));
        coll1.add(new TestClassWithProperty<>("f", "fc"));
        coll1.add(new TestClassWithProperty<>("g", "gc"));
        coll1.add(new TestClassWithProperty<>("m", "0ma"));
        coll1.add(new TestClassWithProperty<>("n", "0na"));
        coll1.add(new TestClassWithProperty<>("o", "Zoa"));
        coll1.add(new TestClassWithProperty<>("p", "Zpa"));

        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> result =
                coll1.stream()
                        .filter(p -> p.getValue().charAt(1) != 'x')
                        .collect(Collectors.toImmutableSortedArrayPropertyMapComparingKeys(TestClassWithProperty::getName, reverseComparator));

        Assert.assertEquals(10, result.size());
        Assert.assertEquals("{p=Zpa, o=Zoa, n=0na, m=0ma, g=gc, f=fc, e=ec, c=cc, b=bc, a=ac}", result.toString());

        Assert.assertEquals(9, result.indexOfKey("a"));
        Assert.assertEquals(8, result.indexOfKey("b"));
        Assert.assertEquals(7, result.indexOfKey("c"));
        Assert.assertEquals(6, result.indexOfKey("e"));
        Assert.assertEquals(5, result.indexOfKey("f"));
        Assert.assertEquals(4, result.indexOfKey("g"));
        Assert.assertEquals(3, result.indexOfKey("m"));
        Assert.assertEquals(2, result.indexOfKey("n"));
        Assert.assertEquals(1, result.indexOfKey("o"));
        Assert.assertEquals(0, result.indexOfKey("p"));
        Assert.assertEquals(-1, result.indexOfKey("0"));

        Assert.assertTrue(result.containsKey("a"));
        Assert.assertTrue(result.containsKey("b"));
        Assert.assertTrue(result.containsKey("c"));
        Assert.assertFalse(result.containsKey("d"));
        Assert.assertTrue(result.containsKey("e"));
        Assert.assertTrue(result.containsKey("f"));
        Assert.assertTrue(result.containsKey("g"));
        Assert.assertTrue(result.containsKey("m"));
        Assert.assertTrue(result.containsKey("n"));
        Assert.assertTrue(result.containsKey("o"));
        Assert.assertTrue(result.containsKey("p"));
        Assert.assertFalse(result.containsKey("0"));

        Assert.assertEquals("a", result.keyAt(9));
        Assert.assertEquals("b", result.keyAt(8));
        Assert.assertEquals("c", result.keyAt(7));
        Assert.assertEquals("e", result.keyAt(6));
        Assert.assertEquals("f", result.keyAt(5));
        Assert.assertEquals("g", result.keyAt(4));
        Assert.assertEquals("m", result.keyAt(3));
        Assert.assertEquals("n", result.keyAt(2));
        Assert.assertEquals("o", result.keyAt(1));
        Assert.assertEquals("p", result.keyAt(0));

        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("a", "ac")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("b", "bc")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("c", "cc")));
        Assert.assertFalse(result.containsValue(new TestClassWithProperty<>("d", "dx")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("e", "ec")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("f", "fc")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("g", "gc")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("m", "0ma")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("n", "0na")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("o", "Zoa")));
        Assert.assertTrue(result.containsValue(new TestClassWithProperty<>("p", "Zpa")));
        Assert.assertFalse(result.containsValue(new TestClassWithProperty<String>("0", null)));

        Assert.assertEquals(9, result.indexOfValue(new TestClassWithProperty<>("a", "ac")));
        Assert.assertEquals(8, result.indexOfValue(new TestClassWithProperty<>("b", "bc")));
        Assert.assertEquals(7, result.indexOfValue(new TestClassWithProperty<>("c", "cc")));
        Assert.assertEquals(6, result.indexOfValue(new TestClassWithProperty<>("e", "ec")));
        Assert.assertEquals(5, result.indexOfValue(new TestClassWithProperty<>("f", "fc")));
        Assert.assertEquals(4, result.indexOfValue(new TestClassWithProperty<>("g", "gc")));
        Assert.assertEquals(3, result.indexOfValue(new TestClassWithProperty<>("m", "0ma")));
        Assert.assertEquals(2, result.indexOfValue(new TestClassWithProperty<>("n", "0na")));
        Assert.assertEquals(1, result.indexOfValue(new TestClassWithProperty<>("o", "Zoa")));
        Assert.assertEquals(0, result.indexOfValue(new TestClassWithProperty<>("p", "Zpa")));
        Assert.assertEquals(-1, result.indexOfValue(new TestClassWithProperty<String>("0", null)));

        Assert.assertEquals("ac", result.valueAt(9).getValue());
        Assert.assertEquals("bc", result.valueAt(8).getValue());
        Assert.assertEquals("cc", result.valueAt(7).getValue());
        Assert.assertEquals("ec", result.valueAt(6).getValue());
        Assert.assertEquals("fc", result.valueAt(5).getValue());
        Assert.assertEquals("gc", result.valueAt(4).getValue());
        Assert.assertEquals("0ma", result.valueAt(3).getValue());
        Assert.assertEquals("0na", result.valueAt(2).getValue());
        Assert.assertEquals("Zoa", result.valueAt(1).getValue());
        Assert.assertEquals("Zpa", result.valueAt(0).getValue());

        Map.Entry<String, TestClassWithProperty<String>> entry = result.entryAt(9);

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue().getValue());

        entry = result.entryAt(8);
        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue().getValue());

        entry = result.entryAt(7);
        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue().getValue());

        entry = result.entryAt(6);
        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue().getValue());

        entry = result.entryAt(5);
        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue().getValue());

        entry = result.entryAt(4);
        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue().getValue());

        entry = result.entryAt(3);
        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue().getValue());

        entry = result.entryAt(2);
        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue().getValue());

        entry = result.entryAt(1);
        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue().getValue());

        entry = result.entryAt(0);
        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue().getValue());

        Assert.assertEquals("ac", result.get("a").getValue());
        Assert.assertEquals("bc", result.get("b").getValue());
        Assert.assertEquals("cc", result.get("c").getValue());
        Assert.assertEquals("ec", result.get("e").getValue());
        Assert.assertEquals("fc", result.get("f").getValue());
        Assert.assertEquals("gc", result.get("g").getValue());
        Assert.assertEquals("0ma", result.get("m").getValue());
        Assert.assertEquals("0na", result.get("n").getValue());
        Assert.assertEquals("Zoa", result.get("o").getValue());
        Assert.assertEquals("Zpa", result.get("p").getValue());
        Assert.assertSame(null, result.get("0"));

        ArrayBackedSet<Map.Entry<String, TestClassWithProperty<String>>> entrySet = result.entrySet();
        Iterator<Map.Entry<String, TestClassWithProperty<String>>> entryIt = entrySet.iterator();

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue().getValue());

        Assert.assertFalse(entryIt.hasNext());

    }

    @Test
    public void testSplitterWithComparator() throws Exception {
        Collection<TestClassWithProperty<String>> coll1 = new HashSet<>();
        coll1.add(new TestClassWithProperty<>("a", "ac"));
        coll1.add(new TestClassWithProperty<>("b", "bc"));
        coll1.add(new TestClassWithProperty<>("c", "cc"));
        coll1.add(new TestClassWithProperty<>("d", "dx"));
        coll1.add(new TestClassWithProperty<>("e", "ec"));
        coll1.add(new TestClassWithProperty<>("f", "fc"));
        coll1.add(new TestClassWithProperty<>("g", "gc"));
        coll1.add(new TestClassWithProperty<>("m", "0ma"));
        coll1.add(new TestClassWithProperty<>("n", "0na"));
        coll1.add(new TestClassWithProperty<>("o", "Zoa"));
        coll1.add(new TestClassWithProperty<>("p", "Zpa"));

        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> result1 =
                coll1.stream()
                        .filter(p -> p.getValue().charAt(1) != 'x')
                        .collect(Collectors.toImmutableSortedArrayPropertyMapComparingKeys(TestClassWithProperty::getName, reverseComparator));

        // Stream the results from one map, collect back to another
        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> result2 =
                result1.values().parallelStream()
                        .collect(Collectors.toImmutableSortedArrayPropertyMapComparingKeys(TestClassWithProperty::getName, reverseComparator));

        Assert.assertEquals(10, result2.size());
        Assert.assertEquals("{p=Zpa, o=Zoa, n=0na, m=0ma, g=gc, f=fc, e=ec, c=cc, b=bc, a=ac}", result2.toString());

        Assert.assertEquals(9, result2.indexOfKey("a"));
        Assert.assertEquals(8, result2.indexOfKey("b"));
        Assert.assertEquals(7, result2.indexOfKey("c"));
        Assert.assertEquals(6, result2.indexOfKey("e"));
        Assert.assertEquals(5, result2.indexOfKey("f"));
        Assert.assertEquals(4, result2.indexOfKey("g"));
        Assert.assertEquals(3, result2.indexOfKey("m"));
        Assert.assertEquals(2, result2.indexOfKey("n"));
        Assert.assertEquals(1, result2.indexOfKey("o"));
        Assert.assertEquals(0, result2.indexOfKey("p"));
        Assert.assertEquals(-1, result2.indexOfKey("0"));

        Assert.assertTrue(result2.containsKey("a"));
        Assert.assertTrue(result2.containsKey("b"));
        Assert.assertTrue(result2.containsKey("c"));
        Assert.assertFalse(result2.containsKey("d"));
        Assert.assertTrue(result2.containsKey("e"));
        Assert.assertTrue(result2.containsKey("f"));
        Assert.assertTrue(result2.containsKey("g"));
        Assert.assertTrue(result2.containsKey("m"));
        Assert.assertTrue(result2.containsKey("n"));
        Assert.assertTrue(result2.containsKey("o"));
        Assert.assertTrue(result2.containsKey("p"));
        Assert.assertFalse(result2.containsKey("0"));

        Assert.assertEquals("a", result2.keyAt(9));
        Assert.assertEquals("b", result2.keyAt(8));
        Assert.assertEquals("c", result2.keyAt(7));
        Assert.assertEquals("e", result2.keyAt(6));
        Assert.assertEquals("f", result2.keyAt(5));
        Assert.assertEquals("g", result2.keyAt(4));
        Assert.assertEquals("m", result2.keyAt(3));
        Assert.assertEquals("n", result2.keyAt(2));
        Assert.assertEquals("o", result2.keyAt(1));
        Assert.assertEquals("p", result2.keyAt(0));

        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("a", "ac")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("b", "bc")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("c", "cc")));
        Assert.assertFalse(result2.containsValue(new TestClassWithProperty<>("d", "dx")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("e", "ec")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("f", "fc")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("g", "gc")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("m", "0ma")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("n", "0na")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("o", "Zoa")));
        Assert.assertTrue(result2.containsValue(new TestClassWithProperty<>("p", "Zpa")));
        Assert.assertFalse(result2.containsValue(new TestClassWithProperty<String>("0", null)));

        Assert.assertEquals(9, result2.indexOfValue(new TestClassWithProperty<>("a", "ac")));
        Assert.assertEquals(8, result2.indexOfValue(new TestClassWithProperty<>("b", "bc")));
        Assert.assertEquals(7, result2.indexOfValue(new TestClassWithProperty<>("c", "cc")));
        Assert.assertEquals(6, result2.indexOfValue(new TestClassWithProperty<>("e", "ec")));
        Assert.assertEquals(5, result2.indexOfValue(new TestClassWithProperty<>("f", "fc")));
        Assert.assertEquals(4, result2.indexOfValue(new TestClassWithProperty<>("g", "gc")));
        Assert.assertEquals(3, result2.indexOfValue(new TestClassWithProperty<>("m", "0ma")));
        Assert.assertEquals(2, result2.indexOfValue(new TestClassWithProperty<>("n", "0na")));
        Assert.assertEquals(1, result2.indexOfValue(new TestClassWithProperty<>("o", "Zoa")));
        Assert.assertEquals(0, result2.indexOfValue(new TestClassWithProperty<>("p", "Zpa")));
        Assert.assertEquals(-1, result2.indexOfValue(new TestClassWithProperty<String>("0", null)));

        Assert.assertEquals("ac", result2.valueAt(9).getValue());
        Assert.assertEquals("bc", result2.valueAt(8).getValue());
        Assert.assertEquals("cc", result2.valueAt(7).getValue());
        Assert.assertEquals("ec", result2.valueAt(6).getValue());
        Assert.assertEquals("fc", result2.valueAt(5).getValue());
        Assert.assertEquals("gc", result2.valueAt(4).getValue());
        Assert.assertEquals("0ma", result2.valueAt(3).getValue());
        Assert.assertEquals("0na", result2.valueAt(2).getValue());
        Assert.assertEquals("Zoa", result2.valueAt(1).getValue());
        Assert.assertEquals("Zpa", result2.valueAt(0).getValue());

        Map.Entry<String, TestClassWithProperty<String>> entry = result2.entryAt(9);

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue().getValue());

        entry = result2.entryAt(8);
        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue().getValue());

        entry = result2.entryAt(7);
        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue().getValue());

        entry = result2.entryAt(6);
        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue().getValue());

        entry = result2.entryAt(5);
        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue().getValue());

        entry = result2.entryAt(4);
        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue().getValue());

        entry = result2.entryAt(3);
        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue().getValue());

        entry = result2.entryAt(2);
        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue().getValue());

        entry = result2.entryAt(1);
        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue().getValue());

        entry = result2.entryAt(0);
        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue().getValue());

        Assert.assertEquals("ac", result2.get("a").getValue());
        Assert.assertEquals("bc", result2.get("b").getValue());
        Assert.assertEquals("cc", result2.get("c").getValue());
        Assert.assertEquals("ec", result2.get("e").getValue());
        Assert.assertEquals("fc", result2.get("f").getValue());
        Assert.assertEquals("gc", result2.get("g").getValue());
        Assert.assertEquals("0ma", result2.get("m").getValue());
        Assert.assertEquals("0na", result2.get("n").getValue());
        Assert.assertEquals("Zoa", result2.get("o").getValue());
        Assert.assertEquals("Zpa", result2.get("p").getValue());
        Assert.assertSame(null, result2.get("0"));

        ArrayBackedSet<Map.Entry<String, TestClassWithProperty<String>>> entrySet = result2.entrySet();
        Iterator<Map.Entry<String, TestClassWithProperty<String>>> entryIt = entrySet.iterator();

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue().getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue().getValue());

        Assert.assertFalse(entryIt.hasNext());

    }

    @Test
    public void testMerge() throws Exception {
        Collection<TestClassWithProperty<String>> list1 = new HashSet<>();
        list1.add(new TestClassWithProperty<>("a", "ac"));
        list1.add(new TestClassWithProperty<>("b", "bc"));
        list1.add(new TestClassWithProperty<>("c", "cc"));
        list1.add(new TestClassWithProperty<>("d", "dx"));
        list1.add(new TestClassWithProperty<>("e", "ec"));
        list1.add(new TestClassWithProperty<>("f", "fc"));
        list1.add(new TestClassWithProperty<>("g", "gc"));

        Collection<TestClassWithProperty<String>> list2 = new HashSet<>();
        list2.add(new TestClassWithProperty<>("m", "ma"));
        list2.add(new TestClassWithProperty<>("n", "na"));
        list2.add(new TestClassWithProperty<>("o", "oa"));
        list2.add(new TestClassWithProperty<>("p", "pa"));

        ImmutableSortedArrayPropertyMapBuilder<String, TestClassWithProperty<String>> builder1 =
                new ImmutableSortedArrayPropertyMapBuilder<>();
        ImmutableSortedArrayPropertyMapBuilder<String, TestClassWithProperty<String>> builder2 =
                ImmutableSortedArrayPropertyMapBuilder.newMap();

        builder1.byKeyMethod(TestClassWithProperty::getName);
        builder2.byKeyMethod(TestClassWithProperty::getName);
        builder1.with(list1);
        list2.forEach(builder2::with);
        builder1.byNaturalKeyOrder();
        builder1.merge(builder2);

        ArrayBackedMap<String, TestClassWithProperty<String>> result = builder1.build();
        Assert.assertEquals(11, result.size());
    }

    @Test
    public void testBuilder() throws Exception {
        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> map =
                ImmutableSortedArrayPropertyMap.<String, TestClassWithProperty<String>>builder()
                        .byKeyMethod(TestClassWithProperty::getName)
                        .with(new TestClassWithProperty<>("c", "5"), new TestClassWithProperty<>("d", "4"), new TestClassWithProperty<>("e", "3"))
                        .with(new TestClassWithProperty<>("a", "7"), new TestClassWithProperty<>("b", "96"))
                        .with(new TestClassWithProperty<>("f", "2"), new TestClassWithProperty<>("g", "1")).build();

        Assert.assertEquals(7, map.size());

        Assert.assertFalse(map.containsKey("0"));
        Assert.assertEquals(new TestClassWithProperty<>("a","7"), map.get("a"));
        Assert.assertFalse(map.containsKey("ab"));
        Assert.assertFalse(map.containsKey("da"));
        Assert.assertEquals(new TestClassWithProperty<>("g","1"), map.get("g"));
        Assert.assertFalse(map.containsKey("zz"));

        ArrayBackedSet<String> keySet = map.keySet();
        Assert.assertFalse(keySet.isEmpty());
        Assert.assertEquals(7, keySet.size());
        Assert.assertEquals("[a, b, c, d, e, f, g]", keySet.toString());
        List<String> keyList = keySet.asList();
        Assert.assertFalse(keyList.isEmpty());
        Assert.assertEquals(7, keyList.size());
        Assert.assertEquals("[a, b, c, d, e, f, g]", keyList.toString());

        ArrayBackedSet<Map.Entry<String, TestClassWithProperty<String>>> entrySet = map.entrySet();
        Assert.assertFalse(entrySet.isEmpty());
        Assert.assertEquals(7, entrySet.size());
        Assert.assertEquals("[a=7, b=96, c=5, d=4, e=3, f=2, g=1]", entrySet.toString());
        List<Map.Entry<String, TestClassWithProperty<String>>> entryList = entrySet.asList();
        Assert.assertFalse(entryList.isEmpty());
        Assert.assertEquals(7, entryList.size());
        Assert.assertEquals("[a=7, b=96, c=5, d=4, e=3, f=2, g=1]", entryList.toString());

        ArrayBackedCollection<TestClassWithProperty<String>> values = map.values();
        Assert.assertFalse(values.isEmpty());
        Assert.assertEquals(7, values.size());
        Assert.assertEquals("[7, 96, 5, 4, 3, 2, 1]", values.toString());
        List<TestClassWithProperty<String>> valueList = values.asList();
        Assert.assertFalse(valueList.isEmpty());
        Assert.assertEquals(7, valueList.size());
        Assert.assertEquals("[7, 96, 5, 4, 3, 2, 1]", valueList.toString());

        Assert.assertEquals(1, map.indexOfValue(new TestClassWithProperty<>("b","96")));
    }

    @Test
    public void testSubMap() throws Exception {
        ImmutableSortedArrayPropertyMapBuilder<String, TestClassWithProperty<String>> builder =
                new ImmutableSortedArrayPropertyMapBuilder<>();
        builder.with(new TestClassWithProperty<>("a", "ac"));
        builder.with(new TestClassWithProperty<>("b", "bc"));
        builder.with(new TestClassWithProperty<>("c", "cc"));
        builder.with(new TestClassWithProperty<>("d", "dx"));
        builder.with(new TestClassWithProperty<>("e", "ec"));
        builder.with(new TestClassWithProperty<>("f", "fc"));
        builder.with(new TestClassWithProperty<>("g", "gc"));
        builder.byKeyMethod(TestClassWithProperty::getName);

        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> map = builder.build();
        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> headMap = map.headMap("d");
        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> tailMap = map.tailMap("e");
        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> subMap = map.subMap("d", "e");
        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> emptySubMap = map.subMap("ab", "ac");
        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> fullSubMap = map.subMap("a", "h");

        Assert.assertEquals(3, headMap.size());
        Assert.assertEquals(3, tailMap.size());
        Assert.assertEquals(1, subMap.size());
        Assert.assertTrue(emptySubMap.isEmpty());
        Assert.assertEquals(7, fullSubMap.size());


        Assert.assertEquals("a", map.firstKey());
        Assert.assertEquals("g", map.lastKey());
    }

    /**
     * A static method that fits the Function definition for extracting a
     * key from the given test class.
     */
    private static String extractKey(TestClassWithProperty<String> tc) {
        if(tc == null) {
            return null;
        }
        String value = tc.getValue();
        return (value == null) ? null : value.substring(value.length() - 2);
    }

    /**
     * An internal class for testing the map.
     *
     * @param <T> the value type
     */
    private static final class TestClassWithProperty<T> {
        private final String name;
        private final T value;

        public TestClassWithProperty(String name, T value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public T getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TestClassWithProperty)) {
                return false;
            }
            TestClassWithProperty<?> that = (TestClassWithProperty<?>) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            int result = Objects.hashCode(name);
            result = 31 * result + Objects.hashCode(value);
            return result;
        }
    }
}
