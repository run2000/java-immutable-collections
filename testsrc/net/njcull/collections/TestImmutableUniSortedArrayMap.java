package net.njcull.collections;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tests for ImmutableUniSortedArrayMap.
 *
 * @author run2000
 * @version 8/01/2016.
 */
public final class TestImmutableUniSortedArrayMap {

    @Test
    public void testEmptyMap() throws Exception {
        ImmutableUniSortedArrayMap<String, String> test = ImmutableUniSortedArrayMap.<String, String>builder().build();
        Assert.assertFalse(test.containsKey("3"));
        Assert.assertSame(test, ImmutableUniSortedArrayMap.emptyMap());
        Assert.assertTrue(test.isEmpty());
        Assert.assertEquals(0, test.size());

        Assert.assertEquals("{}", test.toString());
        Assert.assertEquals(0, test.hashCode());

        ArrayBackedCollection<String> keys = test.keySet();
        Assert.assertEquals("[]", keys.toString());
        Assert.assertEquals(0, keys.hashCode());
        Assert.assertEquals(1, keys.asList().hashCode());
    }

    @Test
    public void testCollector() throws Exception {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("a", "ac");
        map1.put("b", "bc");
        map1.put("c", "cc");
        map1.put("d", "dx");
        map1.put("e", "ec");
        map1.put("f", "fc");
        map1.put("g", "gc");
        map1.put("m", "0ma");
        map1.put("n", "0na");
        map1.put("o", "Zoa");
        map1.put("p", "Zpa");

        ImmutableUniSortedArrayMap<String, String> result =
                map1.entrySet().stream()
                .filter(p -> p.getValue().charAt(1) != 'x')
                .collect(Collectors.toImmutableUniSortedArrayMap());

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

        Assert.assertTrue(result.containsValue("ac"));
        Assert.assertTrue(result.containsValue("bc"));
        Assert.assertTrue(result.containsValue("cc"));
        Assert.assertFalse(result.containsValue("dx"));
        Assert.assertTrue(result.containsValue("ec"));
        Assert.assertTrue(result.containsValue("fc"));
        Assert.assertTrue(result.containsValue("gc"));
        Assert.assertTrue(result.containsValue("0ma"));
        Assert.assertTrue(result.containsValue("0na"));
        Assert.assertTrue(result.containsValue("Zoa"));
        Assert.assertTrue(result.containsValue("Zpa"));
        Assert.assertFalse(result.containsValue("0"));

        Assert.assertEquals(0, result.indexOfValue("ac"));
        Assert.assertEquals(1, result.indexOfValue("bc"));
        Assert.assertEquals(2, result.indexOfValue("cc"));
        Assert.assertEquals(3, result.indexOfValue("ec"));
        Assert.assertEquals(4, result.indexOfValue("fc"));
        Assert.assertEquals(5, result.indexOfValue("gc"));
        Assert.assertEquals(6, result.indexOfValue("0ma"));
        Assert.assertEquals(7, result.indexOfValue("0na"));
        Assert.assertEquals(8, result.indexOfValue("Zoa"));
        Assert.assertEquals(9, result.indexOfValue("Zpa"));
        Assert.assertEquals(-1, result.indexOfValue("0"));
        Assert.assertEquals(-1, result.indexOfValue(null));

        Assert.assertEquals("ac", result.valueAt(0));
        Assert.assertEquals("bc", result.valueAt(1));
        Assert.assertEquals("cc", result.valueAt(2));
        Assert.assertEquals("ec", result.valueAt(3));
        Assert.assertEquals("fc", result.valueAt(4));
        Assert.assertEquals("gc", result.valueAt(5));
        Assert.assertEquals("0ma", result.valueAt(6));
        Assert.assertEquals("0na", result.valueAt(7));
        Assert.assertEquals("Zoa", result.valueAt(8));
        Assert.assertEquals("Zpa", result.valueAt(9));

        Map.Entry<String, String> entry = result.entryAt(0);

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue());

        entry = result.entryAt(1);
        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue());

        entry = result.entryAt(2);
        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue());

        entry = result.entryAt(3);
        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue());

        entry = result.entryAt(4);
        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue());

        entry = result.entryAt(5);
        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue());

        entry = result.entryAt(6);
        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue());

        entry = result.entryAt(7);
        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue());

        entry = result.entryAt(8);
        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue());

        entry = result.entryAt(9);
        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue());

        Assert.assertEquals("ac", result.get("a"));
        Assert.assertEquals("bc", result.get("b"));
        Assert.assertEquals("cc", result.get("c"));
        Assert.assertEquals("ec", result.get("e"));
        Assert.assertEquals("fc", result.get("f"));
        Assert.assertEquals("gc", result.get("g"));
        Assert.assertEquals("0ma", result.get("m"));
        Assert.assertEquals("0na", result.get("n"));
        Assert.assertEquals("Zoa", result.get("o"));
        Assert.assertEquals("Zpa", result.get("p"));
        Assert.assertSame(null, result.get("0"));

        ArrayBackedSet<Map.Entry<String, String>> entrySet = result.entrySet();
        Iterator<Map.Entry<String, String>> entryIt = entrySet.iterator();

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue());

        Assert.assertFalse(entryIt.hasNext());

    }

    @Test
    public void testSplitter() throws Exception {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("a", "ac");
        map1.put("b", "bc");
        map1.put("c", "cc");
        map1.put("d", "dx");
        map1.put("e", "ec");
        map1.put("f", "fc");
        map1.put("g", "gc");
        map1.put("m", "0ma");
        map1.put("n", "0na");
        map1.put("o", "Zoa");
        map1.put("p", "Zpa");

        ImmutableUniSortedArrayMap<String, String> result1 =
                map1.entrySet().stream()
                        .filter(p -> p.getValue().charAt(1) != 'x')
                        .collect(Collectors.toImmutableUniSortedArrayMap());

        // Stream the results from one map, collect back to another
        ImmutableUniSortedArrayMap<String, String> result2 =
                result1.entrySet().parallelStream()
                        .collect(Collectors.toImmutableUniSortedArrayMap());

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

        Assert.assertTrue(result2.containsValue("ac"));
        Assert.assertTrue(result2.containsValue("bc"));
        Assert.assertTrue(result2.containsValue("cc"));
        Assert.assertFalse(result2.containsValue("dx"));
        Assert.assertTrue(result2.containsValue("ec"));
        Assert.assertTrue(result2.containsValue("fc"));
        Assert.assertTrue(result2.containsValue("gc"));
        Assert.assertTrue(result2.containsValue("0ma"));
        Assert.assertTrue(result2.containsValue("0na"));
        Assert.assertTrue(result2.containsValue("Zoa"));
        Assert.assertTrue(result2.containsValue("Zpa"));
        Assert.assertFalse(result2.containsValue("0"));

        Assert.assertEquals(0, result2.indexOfValue("ac"));
        Assert.assertEquals(1, result2.indexOfValue("bc"));
        Assert.assertEquals(2, result2.indexOfValue("cc"));
        Assert.assertEquals(3, result2.indexOfValue("ec"));
        Assert.assertEquals(4, result2.indexOfValue("fc"));
        Assert.assertEquals(5, result2.indexOfValue("gc"));
        Assert.assertEquals(6, result2.indexOfValue("0ma"));
        Assert.assertEquals(7, result2.indexOfValue("0na"));
        Assert.assertEquals(8, result2.indexOfValue("Zoa"));
        Assert.assertEquals(9, result2.indexOfValue("Zpa"));
        Assert.assertEquals(-1, result2.indexOfValue("0"));
        Assert.assertEquals(-1, result2.indexOfValue(null));

        Assert.assertEquals("ac", result2.valueAt(0));
        Assert.assertEquals("bc", result2.valueAt(1));
        Assert.assertEquals("cc", result2.valueAt(2));
        Assert.assertEquals("ec", result2.valueAt(3));
        Assert.assertEquals("fc", result2.valueAt(4));
        Assert.assertEquals("gc", result2.valueAt(5));
        Assert.assertEquals("0ma", result2.valueAt(6));
        Assert.assertEquals("0na", result2.valueAt(7));
        Assert.assertEquals("Zoa", result2.valueAt(8));
        Assert.assertEquals("Zpa", result2.valueAt(9));

        Map.Entry<String, String> entry = result2.entryAt(0);

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue());

        entry = result2.entryAt(1);
        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue());

        entry = result2.entryAt(2);
        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue());

        entry = result2.entryAt(3);
        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue());

        entry = result2.entryAt(4);
        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue());

        entry = result2.entryAt(5);
        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue());

        entry = result2.entryAt(6);
        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue());

        entry = result2.entryAt(7);
        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue());

        entry = result2.entryAt(8);
        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue());

        entry = result2.entryAt(9);
        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue());

        Assert.assertEquals("ac", result2.get("a"));
        Assert.assertEquals("bc", result2.get("b"));
        Assert.assertEquals("cc", result2.get("c"));
        Assert.assertEquals("ec", result2.get("e"));
        Assert.assertEquals("fc", result2.get("f"));
        Assert.assertEquals("gc", result2.get("g"));
        Assert.assertEquals("0ma", result2.get("m"));
        Assert.assertEquals("0na", result2.get("n"));
        Assert.assertEquals("Zoa", result2.get("o"));
        Assert.assertEquals("Zpa", result2.get("p"));
        Assert.assertSame(null, result2.get("0"));

        ArrayBackedSet<Map.Entry<String, String>> entrySet = result2.entrySet();
        Iterator<Map.Entry<String, String>> entryIt = entrySet.iterator();

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("a", entry.getKey());
        Assert.assertEquals("ac", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("b", entry.getKey());
        Assert.assertEquals("bc", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("c", entry.getKey());
        Assert.assertEquals("cc", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("e", entry.getKey());
        Assert.assertEquals("ec", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("f", entry.getKey());
        Assert.assertEquals("fc", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("g", entry.getKey());
        Assert.assertEquals("gc", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("m", entry.getKey());
        Assert.assertEquals("0ma", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("n", entry.getKey());
        Assert.assertEquals("0na", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("o", entry.getKey());
        Assert.assertEquals("Zoa", entry.getValue());

        Assert.assertTrue(entryIt.hasNext());
        entry = entryIt.next();

        Assert.assertEquals("p", entry.getKey());
        Assert.assertEquals("Zpa", entry.getValue());

        Assert.assertFalse(entryIt.hasNext());

        List<String> keyList = result2.keySet().asList();

        Assert.assertEquals(0, keyList.indexOf("a"));
        Assert.assertEquals(1, keyList.indexOf("b"));
        Assert.assertEquals(2, keyList.indexOf("c"));
        Assert.assertEquals(3, keyList.indexOf("e"));
        Assert.assertEquals(4, keyList.indexOf("f"));
        Assert.assertEquals(5, keyList.indexOf("g"));
        Assert.assertEquals(6, keyList.indexOf("m"));
        Assert.assertEquals(7, keyList.indexOf("n"));
        Assert.assertEquals(8, keyList.indexOf("o"));
        Assert.assertEquals(9, keyList.indexOf("p"));
        Assert.assertEquals(-1, keyList.indexOf(null));
    }

    @Test
    public void testKeySplitter() throws Exception {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("a", "ac");
        map1.put("b", "bc");
        map1.put("c", "cc");
        map1.put("d", "dx");
        map1.put("e", "ec");
        map1.put("f", "fc");
        map1.put("g", "gc");
        map1.put("m", "0ma");
        map1.put("n", "0na");
        map1.put("o", "Zoa");
        map1.put("p", "Zpa");

        ImmutableUniSortedArrayMap<String, String> result1 =
                map1.entrySet().stream()
                        .filter(p -> p.getValue().charAt(1) != 'x')
                        .collect(Collectors.toImmutableUniSortedArrayMap());

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
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("a", "ac");
        map1.put("b", "bc");
        map1.put("c", "cc");
        map1.put("d", "dx");
        map1.put("e", "ec");
        map1.put("f", "fc");
        map1.put("g", "gc");
        map1.put("m", "0ma");
        map1.put("n", "0na");
        map1.put("o", "Zoa");
        map1.put("p", "Zpa");

        ImmutableUniSortedArrayMap<String, String> result1 =
                map1.entrySet().stream()
                        .filter(p -> p.getValue().charAt(1) != 'x')
                        .collect(Collectors.toImmutableUniSortedArrayMap());

        // Stream the results from one map, collect back to another
        ArrayList<String> result2 =
                result1.values().parallelStream()
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        Assert.assertEquals(10, result2.size());
        Assert.assertEquals("[ac, bc, cc, ec, fc, gc, 0ma, 0na, Zoa, Zpa]", result2.toString());

        Assert.assertTrue(result2.contains("ac"));
        Assert.assertTrue(result2.contains("bc"));
        Assert.assertTrue(result2.contains("cc"));
        Assert.assertFalse(result2.contains("dx"));
        Assert.assertTrue(result2.contains("ec"));
        Assert.assertTrue(result2.contains("fc"));
        Assert.assertTrue(result2.contains("gc"));
        Assert.assertTrue(result2.contains("0ma"));
        Assert.assertTrue(result2.contains("0na"));
        Assert.assertTrue(result2.contains("Zoa"));
        Assert.assertTrue(result2.contains("Zpa"));
        Assert.assertFalse(result2.contains("0"));

        Assert.assertEquals(0, result2.indexOf("ac"));
        Assert.assertEquals(1, result2.indexOf("bc"));
        Assert.assertEquals(2, result2.indexOf("cc"));
        Assert.assertEquals(3, result2.indexOf("ec"));
        Assert.assertEquals(4, result2.indexOf("fc"));
        Assert.assertEquals(5, result2.indexOf("gc"));
        Assert.assertEquals(6, result2.indexOf("0ma"));
        Assert.assertEquals(7, result2.indexOf("0na"));
        Assert.assertEquals(8, result2.indexOf("Zoa"));
        Assert.assertEquals(9, result2.indexOf("Zpa"));
        Assert.assertEquals(-1, result2.indexOf("0"));

        Assert.assertEquals("ac", result2.get(0));
        Assert.assertEquals("bc", result2.get(1));
        Assert.assertEquals("cc", result2.get(2));
        Assert.assertEquals("ec", result2.get(3));
        Assert.assertEquals("fc", result2.get(4));
        Assert.assertEquals("gc", result2.get(5));
        Assert.assertEquals("0ma", result2.get(6));
        Assert.assertEquals("0na", result2.get(7));
        Assert.assertEquals("Zoa", result2.get(8));
        Assert.assertEquals("Zpa", result2.get(9));

    }

    @Test
    public void testMerge() throws Exception {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("a", "ac");
        map1.put("b", "bc");
        map1.put("c", "cc");
        map1.put("d", "dx");
        map1.put("e", "ec");
        map1.put("f", "fc");
        map1.put("g", "gc");

        HashMap<String, String> map2 = new HashMap<>();
        map2.put("m", "ma");
        map2.put("n", "na");
        map2.put("o", "oa");
        map2.put("p", "pa");

        ImmutableUniSortedArrayMapBuilder<String, String> builder1 =
                new ImmutableUniSortedArrayMapBuilder<>();
        ImmutableUniSortedArrayMapBuilder<String, String> builder2 =
                new ImmutableUniSortedArrayMapBuilder<>();

        builder1.with(map1.entrySet());

        map2.entrySet().forEach(builder2::with);

        builder1.merge(builder2);

        Assert.assertEquals(4, builder2.size());
        builder2.clear();
        Assert.assertEquals(0, builder2.size());

        ArrayBackedMap<String, String> result = builder1.build();
        Assert.assertEquals(11, result.size());
    }

    @Test
    public void testBuilder() throws Exception {
      ImmutableUniSortedArrayMap<String, String> map =
              ImmutableUniSortedArrayMap.<String, String>builder()
              .with("c", "5", "d", "4", "e", "3")
              .with("a", "7", "b", "96")
              .with("f", "2", "g", "1").build();

        Assert.assertEquals(7, map.size());

        Assert.assertFalse(map.containsKey("0"));
        Assert.assertEquals("7", map.get("a"));
        Assert.assertFalse(map.containsKey("ab"));
        Assert.assertFalse(map.containsKey("da"));
        Assert.assertEquals("1", map.get("g"));
        Assert.assertFalse(map.containsKey("zz"));

        Assert.assertEquals("5", map.getOrDefault("c", ""));
        Assert.assertEquals("", map.getOrDefault("z", ""));

        StringBuilder builder = new StringBuilder();
        map.forEach((k,v) -> builder.append(k));
        Assert.assertEquals("abcdefg", builder.toString());

        ArrayBackedSet<String> keySet = map.keySet();
        Assert.assertFalse(keySet.isEmpty());
        Assert.assertEquals(7, keySet.size());
        Assert.assertEquals("[a, b, c, d, e, f, g]", keySet.toString());

        Assert.assertTrue(keySet.contains("a"));
        Assert.assertFalse(keySet.contains("p"));

        List<String> elements = Arrays.asList("a", "b", "c");
        Assert.assertTrue(keySet.containsAll(elements));

        Assert.assertEquals("c", keySet.getAtIndex(2));
        Assert.assertEquals("d", keySet.getAtIndex(3));
        Assert.assertEquals("e", keySet.getAtIndex(4));
        Assert.assertEquals("a", keySet.getAtIndex(0));
        Assert.assertEquals("b", keySet.getAtIndex(1));
        Assert.assertEquals("f", keySet.getAtIndex(5));
        Assert.assertEquals("g", keySet.getAtIndex(6));

        Assert.assertEquals(2, keySet.indexOf("c"));
        Assert.assertEquals(3, keySet.indexOf("d"));
        Assert.assertEquals(4, keySet.indexOf("e"));
        Assert.assertEquals(0, keySet.indexOf("a"));
        Assert.assertEquals(1, keySet.indexOf("b"));
        Assert.assertEquals(5, keySet.indexOf("f"));
        Assert.assertEquals(6, keySet.indexOf("g"));

        Assert.assertEquals(2, keySet.indexOfRange("c", 2, 7));
        Assert.assertEquals(3, keySet.indexOfRange("d", 2, 7));
        Assert.assertEquals(4, keySet.indexOfRange("e", 2, 7));
        Assert.assertEquals(-1, keySet.indexOfRange("a", 2, 7));
        Assert.assertEquals(-1, keySet.indexOfRange("b", 2, 7));
        Assert.assertEquals(5, keySet.indexOfRange("f", 2, 7));
        Assert.assertEquals(6, keySet.indexOfRange("g", 2, 7));
        Assert.assertEquals(-1, keySet.indexOfRange(null, 2, 7));

        // toArray()
        Object[] arrAct = keySet.toArray();
        Object[] arrExp = new Object[] { "a", "b", "c", "d", "e", "f", "g" };
        Assert.assertArrayEquals(arrExp, arrAct);

        // toArray(String[]) -- three cases to consider
        String[] arrAct1 = new String[6];
        String[] arrExp1 = new String[] { "a", "b", "c", "d", "e", "f", "g" };

        String[] arrAct1a = keySet.toArray(arrAct1);
        Assert.assertNotSame(arrAct, arrAct1);
        Assert.assertArrayEquals(arrExp1, arrAct1a);

        String[] arrAct2 = new String[7];
        String[] arrAct2a = keySet.toArray(arrAct2);
        Assert.assertSame(arrAct2, arrAct2a);
        Assert.assertArrayEquals(arrExp1, arrAct2);

        String[] arrAct3 = new String[8];
        String[] arrExp3 = new String[] { "a", "b", "c", "d", "e", "f", "g", null };
        String[] arrAct3a = keySet.toArray(arrAct3);
        Assert.assertSame(arrAct3, arrAct3a);
        Assert.assertArrayEquals(arrExp3, arrAct3);

        // New methods in 1.8 - forEach, removeIf
        Assert.assertFalse(keySet.removeIf(e -> e.length() > 1));

        // Key set as list
        List<String> keyList = keySet.asList();
        Assert.assertFalse(keyList.isEmpty());
        Assert.assertEquals(7, keyList.size());
        Assert.assertEquals("[a, b, c, d, e, f, g]", keyList.toString());
        Assert.assertNull(map.comparator());

        List<String> subKeys = keyList.subList(2, 5);
        Assert.assertFalse(subKeys.isEmpty());
        Assert.assertEquals(3, subKeys.size());
        Assert.assertEquals("[c, d, e]", subKeys.toString());

        ArrayBackedCollection<String> keyColl = (ArrayBackedCollection<String>)keyList;
        Assert.assertEquals("a", keyColl.getAtIndex(0));
        Assert.assertEquals("b", keyColl.getAtIndex(1));
        Assert.assertEquals("c", keyColl.getAtIndex(2));
        Assert.assertEquals("d", keyColl.getAtIndex(3));
        Assert.assertEquals("e", keyColl.getAtIndex(4));
        Assert.assertEquals("f", keyColl.getAtIndex(5));
        Assert.assertEquals("g", keyColl.getAtIndex(6));

        Assert.assertEquals(3, keyColl.indexOfRange("d", 2, 5));
        Assert.assertEquals(4, keyColl.indexOfRange("e", 2, 5));
        Assert.assertEquals(2, keyColl.indexOfRange("c", 2, 5));
        Assert.assertEquals(-1, keyColl.indexOfRange(null, 2, 5));

        ArrayBackedSet<Map.Entry<String, String>> entrySet = map.entrySet();
        Assert.assertFalse(entrySet.isEmpty());
        Assert.assertEquals(7, entrySet.size());
        Assert.assertEquals("[a=7, b=96, c=5, d=4, e=3, f=2, g=1]", entrySet.toString());
        List<Map.Entry<String, String>> entryList = entrySet.asList();
        Assert.assertFalse(entryList.isEmpty());
        Assert.assertEquals(7, entryList.size());
        Assert.assertEquals("[a=7, b=96, c=5, d=4, e=3, f=2, g=1]", entryList.toString());

        ArrayBackedCollection<String> values = map.values();
        Assert.assertFalse(values.isEmpty());
        Assert.assertEquals(7, values.size());
        Assert.assertEquals("[7, 96, 5, 4, 3, 2, 1]", values.toString());

        Assert.assertTrue(values.contains("5"));
        Assert.assertFalse(values.contains("21"));

        elements = Arrays.asList("7", "5", "2");
        Assert.assertTrue(values.containsAll(elements));

        Assert.assertEquals("7", values.getAtIndex(0));
        Assert.assertEquals("96", values.getAtIndex(1));
        Assert.assertEquals("5", values.getAtIndex(2));
        Assert.assertEquals("4", values.getAtIndex(3));
        Assert.assertEquals("3", values.getAtIndex(4));
        Assert.assertEquals("2", values.getAtIndex(5));
        Assert.assertEquals("1", values.getAtIndex(6));

        Assert.assertEquals(0, values.indexOf("7"));
        Assert.assertEquals(1, values.indexOf("96"));
        Assert.assertEquals(2, values.indexOf("5"));
        Assert.assertEquals(3, values.indexOf("4"));
        Assert.assertEquals(4, values.indexOf("3"));
        Assert.assertEquals(5, values.indexOf("2"));
        Assert.assertEquals(6, values.indexOf("1"));

        Assert.assertEquals(-1, values.indexOfRange("7", 2, 7));
        Assert.assertEquals(-1, values.indexOfRange("96", 2, 7));
        Assert.assertEquals(2, values.indexOfRange("5", 2, 7));
        Assert.assertEquals(3, values.indexOfRange("4", 2, 7));
        Assert.assertEquals(4, values.indexOfRange("3", 2, 7));
        Assert.assertEquals(5, values.indexOfRange("2", 2, 7));
        Assert.assertEquals(6, values.indexOfRange("1", 2, 7));
        Assert.assertEquals(-1, values.indexOfRange(null, 2, 7));

        // toArray()
        arrAct = values.toArray();
        arrExp = new Object[] { "7", "96", "5", "4", "3", "2", "1" };
        Assert.assertArrayEquals(arrExp, arrAct);

        // toArray(String[]) -- three cases to consider
        arrAct1 = new String[6];
        arrExp1 = new String[] { "7", "96", "5", "4", "3", "2", "1" };

        arrAct1a = values.toArray(arrAct1);
        Assert.assertNotSame(arrAct, arrAct1);
        Assert.assertArrayEquals(arrExp1, arrAct1a);

        arrAct2 = new String[7];
        arrAct2a = values.toArray(arrAct2);
        Assert.assertSame(arrAct2, arrAct2a);
        Assert.assertArrayEquals(arrExp1, arrAct2);

        arrAct3 = new String[8];
        arrExp3 = new String[] { "7", "96", "5", "4", "3", "2", "1", null };
        arrAct3a = values.toArray(arrAct3);
        Assert.assertSame(arrAct3, arrAct3a);
        Assert.assertArrayEquals(arrExp3, arrAct3);

        Iterator<String> valIt = values.iterator();
        StringBuilder builder1 = new StringBuilder();

        while(valIt.hasNext()) {
            builder1.append(valIt.next());
            if(valIt.hasNext()) {
                builder1.append(':');
            }
        }
        Assert.assertEquals("7:96:5:4:3:2:1", builder1.toString());

        // New methods in 1.8 - forEach, removeIf
        Assert.assertFalse(values.removeIf(e -> e.length() > 2));

        // Values as list
        List<String> valueList = values.asList();
        Assert.assertFalse(valueList.isEmpty());
        Assert.assertEquals(7, valueList.size());
        Assert.assertEquals("[7, 96, 5, 4, 3, 2, 1]", valueList.toString());

    }

    @Test
    public void testSubMap() throws Exception {
        ImmutableUniSortedArrayMapBuilder<String, String> builder =
                new ImmutableUniSortedArrayMapBuilder<>();
        builder.with("a", "ac");
        builder.with("b", "bc");
        builder.with("c", "cc");
        builder.with("d", "dx");
        builder.with("e", "ec");
        builder.with("f", "fc");
        builder.with("g", "gc");
        builder.byNaturalOrder();

        ImmutableUniSortedArrayMap<String, String> map = builder.build();
        ImmutableUniSortedArrayMap<String, String> headMap = map.headMap("d");
        ImmutableUniSortedArrayMap<String, String> tailMap = map.tailMap("e");
        ImmutableUniSortedArrayMap<String, String> subMap = map.subMap("d", "e");
        ImmutableUniSortedArrayMap<String, String> emptySubMap = map.subMap("ab", "ac");
        ImmutableUniSortedArrayMap<String, String> fullSubMap = map.subMap("a", "h");

        Assert.assertEquals(3, headMap.size());
        Assert.assertEquals(3, tailMap.size());
        Assert.assertEquals(1, subMap.size());
        Assert.assertTrue(emptySubMap.isEmpty());
        Assert.assertEquals(7, fullSubMap.size());


        Assert.assertEquals("a", map.firstKey());
        Assert.assertEquals("g", map.lastKey());
    }

    @Test
    public void testExceptions() throws Exception {
        ImmutableUniSortedArrayMap<String, String> map =
                ImmutableUniSortedArrayMap.<String, String>builder()
                        .with("c", "5", "d", "4", "e", "3", "a", "7")
                        .with("b", "96")
                        .with("f", "2", "g", "1").build();

        Assert.assertEquals(7, map.size());

        try {
            Assert.assertEquals("5", map.remove("c"));
            Assert.fail("Remove of existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Assert.assertNull(map.put("q", "26"));
            Assert.fail("Put operation for new item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Assert.assertEquals("4", map.put("d", "27"));
            Assert.fail("Put operation for existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            map.clear();
            Assert.fail("Clear operation for non-empty set should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Map<String, String> s = Collections.singletonMap("j", "j");
            map.putAll(s);
            Assert.fail("putAll operation should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Assert.assertTrue(map.keySet().add("k"));
            Assert.fail("Add operation for new item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Assert.assertTrue(map.keySet().add("c"));
            Assert.fail("Add operation for existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Set<String> s = Collections.singleton("j");
            map.keySet().addAll(s);
            Assert.fail("addAll operation should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Assert.assertTrue(map.keySet().remove("c"));
            Assert.fail("Remove of existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            List<String> s = Collections.singletonList("g");
            map.keySet().removeAll(s);
            Assert.fail("Remove of existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            List<String> s = Collections.singletonList("e");
            map.keySet().retainAll(s);
            Assert.fail("Retain of existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Assert.assertTrue(map.values().remove("5"));
            Assert.fail("Remove of existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            List<String> s = Collections.singletonList("4");
            map.values().removeAll(s);
            Assert.fail("Remove of existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            List<String> s = Collections.singletonList("3");
            map.values().retainAll(s);
            Assert.fail("Retain of existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Assert.assertTrue(map.values().add("8"));
            Assert.fail("Add operation for new item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Assert.assertTrue(map.values().add("3"));
            Assert.fail("Add operation for existing item should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            Set<String> s = Collections.singleton("12");
            map.values().addAll(s);
            Assert.fail("addAll operation should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        try {
            map.values().clear();
            Assert.fail("Clear operation should fail");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }

        // No exception, since no elements removed
        List<String> s = Collections.singletonList("k");
        Assert.assertFalse(map.keySet().removeAll(s));

        // No exception, since all elements retained
        s = Arrays.<String>asList("a", "b", "c", "d", "e", "f", "g");
        Assert.assertFalse(map.keySet().retainAll(s));

        // No exception, since no elements removed
        s = Collections.singletonList("9");
        Assert.assertFalse(map.values().removeAll(s));

        // No exception, since all elements retained
        s = Arrays.<String>asList("1", "2", "3", "4", "5", "7", "96");
        Assert.assertFalse(map.values().retainAll(s));

        // No exception, since clearing an empty collection does nothing
        ImmutableUniSortedArrayMap.emptyMap().clear();

        // The list returned from keySet().asList() is itself an ArrayBackedCollection
        ArrayBackedCollection<String> keys1 = (ArrayBackedCollection<String>)map.keySet().asList();
        List<String> keys2 = keys1.asList();
        Assert.assertSame(keys1, keys2);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerialization() throws Exception {
        ImmutableUniSortedArrayMapBuilder<String, Integer> builder =
                new ImmutableUniSortedArrayMapBuilder<>();
        builder.with("a", 13);
        builder.with("b", 23);
        builder.with("c", 33);
        builder.with("d", 48);
        builder.with("e", 53);
        builder.with("f", 63);
        builder.with("g", 73);

        ImmutableUniSortedArrayMap<String, Integer> map = builder.build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(map);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        ImmutableUniSortedArrayMap<String, Integer> map2 =
                (ImmutableUniSortedArrayMap<String, Integer>) ois.readObject();

        Assert.assertEquals(7, map2.size());
        Assert.assertEquals("{a=13, b=23, c=33, d=48, e=53, f=63, g=73}", map2.toString());

        Assert.assertEquals(Integer.valueOf(23), map2.get("b"));
        Assert.assertEquals(Integer.valueOf(73), map2.get("g"));

        // Keysets
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);

        ArrayBackedSet<String> keySet = map.keySet();

        oos.writeObject(keySet);

        bais = new ByteArrayInputStream(baos.toByteArray());
        ois = new ObjectInputStream(bais);

        ArrayBackedSet<String> keySet2 = (ArrayBackedSet<String>) ois.readObject();

        Assert.assertEquals(keySet, keySet2);
        Assert.assertEquals(7, keySet2.size());
        Assert.assertEquals("[a, b, c, d, e, f, g]", keySet2.toString());

        // Values
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);

        ArrayBackedCollection<Integer> values = map.values();
        oos.writeObject(values);

        bais = new ByteArrayInputStream(baos.toByteArray());
        ois = new ObjectInputStream(bais);

        ArrayBackedCollection<Integer> values2 = (ArrayBackedCollection<Integer>) ois.readObject();

        Assert.assertEquals(values, values2);
        Assert.assertEquals(7, values2.size());
        Assert.assertEquals("[13, 23, 33, 48, 53, 63, 73]", values2.toString());

        // Entrysets
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);

        ArrayBackedSet<Map.Entry<String,Integer>> entrySet = map.entrySet();

        oos.writeObject(entrySet);

        bais = new ByteArrayInputStream(baos.toByteArray());
        ois = new ObjectInputStream(bais);

        ArrayBackedSet<Map.Entry<String,Integer>> entrySet2 =
                (ArrayBackedSet<Map.Entry<String,Integer>>) ois.readObject();

        Assert.assertEquals(entrySet, entrySet2);
        Assert.assertEquals(7, entrySet2.size());
        Assert.assertEquals("[a=13, b=23, c=33, d=48, e=53, f=63, g=73]", entrySet2.toString());

        // Test empty map
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);

        oos.writeObject(ImmutableUniSortedArrayMap.emptyMap());

        bais = new ByteArrayInputStream(baos.toByteArray());
        ois = new ObjectInputStream(bais);

        map2 = (ImmutableUniSortedArrayMap<String, Integer>) ois.readObject();
        Assert.assertEquals("{}", map2.toString());
        Assert.assertEquals(0, map2.size());
        Assert.assertSame(ImmutableUniSortedArrayMap.emptyMap(), map2);

    }
}
