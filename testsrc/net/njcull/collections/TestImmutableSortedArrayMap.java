package net.njcull.collections;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Tests for ImmutableSortedArrayMap.
 *
 * @author run2000
 * @version 8/01/2016.
 */
public final class TestImmutableSortedArrayMap {

    @Test
    public void testEmptyMap() throws Exception {
        Map<String, String> test = ImmutableSortedArrayMap.<String, String>builder().build();
        Assert.assertFalse(test.containsKey("3"));
        Assert.assertSame(test, ImmutableSortedArrayMap.emptyMap());
        Assert.assertTrue(test.isEmpty());
        Assert.assertEquals(0, test.size());
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

        ImmutableSortedArrayMap<String, String> result =
                map1.entrySet().stream()
                .filter(p -> p.getValue().charAt(1) != 'x')
                .collect(Collectors.toImmutableSortedArrayMap());

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

        Assert.assertEquals(0, result.indexOfValueSorted("0ma"));
        Assert.assertEquals(1, result.indexOfValueSorted("0na"));
        Assert.assertEquals(2, result.indexOfValueSorted("Zoa"));
        Assert.assertEquals(3, result.indexOfValueSorted("Zpa"));
        Assert.assertEquals(4, result.indexOfValueSorted("ac"));
        Assert.assertEquals(5, result.indexOfValueSorted("bc"));
        Assert.assertEquals(6, result.indexOfValueSorted("cc"));
        Assert.assertEquals(7, result.indexOfValueSorted("ec"));
        Assert.assertEquals(8, result.indexOfValueSorted("fc"));
        Assert.assertEquals(9, result.indexOfValueSorted("gc"));
        Assert.assertEquals(-1, result.indexOfValueSorted("0"));

        Assert.assertEquals("0ma", result.sortedValueAt(0));
        Assert.assertEquals("0na", result.sortedValueAt(1));
        Assert.assertEquals("Zoa", result.sortedValueAt(2));
        Assert.assertEquals("Zpa", result.sortedValueAt(3));
        Assert.assertEquals("ac", result.sortedValueAt(4));
        Assert.assertEquals("bc", result.sortedValueAt(5));
        Assert.assertEquals("cc", result.sortedValueAt(6));
        Assert.assertEquals("ec", result.sortedValueAt(7));
        Assert.assertEquals("fc", result.sortedValueAt(8));
        Assert.assertEquals("gc", result.sortedValueAt(9));

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

        entrySet = result.entrySetByValue();
        entryIt = entrySet.iterator();

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

        Assert.assertFalse(entryIt.hasNext());

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

        ImmutableSortedArrayMapBuilder<String, String> builder1 =
                new ImmutableSortedArrayMapBuilder<>();
        ImmutableSortedArrayMapBuilder<String, String> builder2 =
                new ImmutableSortedArrayMapBuilder<>();

        builder1.with(map1.entrySet());

        map2.entrySet().forEach(builder2::with);

        builder1.merge(builder2);

        ArrayBackedMap<String, String> result = builder1.build();
        Assert.assertEquals(11, result.size());
    }

    @Test
    public void testBuilder() throws Exception {
      ImmutableSortedArrayMap<String, String> map =
              ImmutableSortedArrayMap.<String, String>builder()
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

        Assert.assertEquals("[a=7, b=96, c=5, d=4, e=3, f=2, g=1]", map.entrySet().toString());
        Assert.assertEquals("[1, 2, 3, 4, 5, 7, 96]", map.values().toString());
        Assert.assertEquals("[g=1, f=2, e=3, d=4, c=5, a=7, b=96]", map.entrySetByValue().toString());

        Assert.assertEquals(1, map.indexOfValue("96"));
    }
}
