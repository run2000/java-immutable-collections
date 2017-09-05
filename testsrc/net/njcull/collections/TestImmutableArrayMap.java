package net.njcull.collections;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Tests for ImmutableArrayMap.
 *
 * @author run2000
 * @version 8/01/2016.
 */
public final class TestImmutableArrayMap {

    @Test
    public void testEmptyMap() throws Exception {
        Map<String, String> test = ImmutableArrayMap.<String, String>builder().build();
        Assert.assertFalse(test.containsKey("3"));
        Assert.assertSame(test, ImmutableArrayMap.emptyMap());
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

        ImmutableArrayMap<String, String> result =
                map1.entrySet().stream()
                .filter(p -> p.getValue().charAt(1) != 'x')
                .collect(Collectors.toImmutableArrayMap());

        Assert.assertEquals(10, result.size());
//        Assert.assertEquals("{a=ac, b=bc, c=cc, e=ec, f=fc, g=gc, m=0ma, n=0na, o=Zoa, p=Zpa}", result.toString());

        Assert.assertTrue(result.indexOfKey("a") >= 0);
        Assert.assertTrue(result.indexOfKey("b") >= 0);
        Assert.assertTrue(result.indexOfKey("c") >= 0);
        Assert.assertTrue(result.indexOfKey("e") >= 0);
        Assert.assertTrue(result.indexOfKey("f") >= 0);
        Assert.assertTrue(result.indexOfKey("g") >= 0);
        Assert.assertTrue(result.indexOfKey("m") >= 0);
        Assert.assertTrue(result.indexOfKey("n") >= 0);
        Assert.assertTrue(result.indexOfKey("o") >= 0);
        Assert.assertTrue(result.indexOfKey("p") >= 0);
        Assert.assertFalse(result.indexOfKey("0") >= 0);
        Assert.assertFalse(result.indexOfKey(null) >= 0);

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
        Assert.assertFalse(result.containsKey(null));

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
        Assert.assertFalse(result.containsValue(null));

        Assert.assertTrue(result.indexOfValue("ac") >= 0);
        Assert.assertTrue(result.indexOfValue("bc") >= 0);
        Assert.assertTrue(result.indexOfValue("cc") >= 0);
        Assert.assertTrue(result.indexOfValue("ec") >= 0);
        Assert.assertTrue(result.indexOfValue("fc") >= 0);
        Assert.assertTrue(result.indexOfValue("gc") >= 0);
        Assert.assertTrue(result.indexOfValue("0ma") >= 0);
        Assert.assertTrue(result.indexOfValue("0na") >= 0);
        Assert.assertTrue(result.indexOfValue("Zoa") >= 0);
        Assert.assertTrue(result.indexOfValue("Zpa") >= 0);
        Assert.assertFalse(result.indexOfValue("0") >= 0);
        Assert.assertFalse(result.indexOfValue(null) >= 0);

        Assert.assertTrue(result.lastIndexOfValue("ac") >= 0);
        Assert.assertTrue(result.lastIndexOfValue("bc") >= 0);
        Assert.assertTrue(result.lastIndexOfValue("cc") >= 0);
        Assert.assertTrue(result.lastIndexOfValue("ec") >= 0);
        Assert.assertTrue(result.lastIndexOfValue("fc") >= 0);
        Assert.assertTrue(result.lastIndexOfValue("gc") >= 0);
        Assert.assertTrue(result.lastIndexOfValue("0ma") >= 0);
        Assert.assertTrue(result.lastIndexOfValue("0na") >= 0);
        Assert.assertTrue(result.lastIndexOfValue("Zoa") >= 0);
        Assert.assertTrue(result.lastIndexOfValue("Zpa") >= 0);
        Assert.assertFalse(result.lastIndexOfValue("0") >= 0);
        Assert.assertFalse(result.lastIndexOfValue(null) >= 0);

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
        Assert.assertSame(null, result.get(null));

        ArrayBackedSet<Map.Entry<String, String>> entrySet = result.entrySet();
        Iterator<Map.Entry<String, String>> entryIt = entrySet.iterator();

        Assert.assertTrue(entryIt.hasNext());
        Assert.assertEquals(10, result.size());

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

        ImmutableArrayMap<String, String> result1 =
                map1.entrySet().stream()
                        .filter(p -> p.getValue().charAt(1) != 'x')
                        .collect(Collectors.toImmutableArrayMap());

        Assert.assertEquals(10, result1.size());

        // Stream the results from one map, collect back to another
        ImmutableArrayMap<String, String> result2 =
                result1.entrySet().parallelStream()
                        .collect(Collectors.toImmutableArrayMap());

        Assert.assertTrue(result2.indexOfKey("a") >= 0);
        Assert.assertTrue(result2.indexOfKey("b") >= 0);
        Assert.assertTrue(result2.indexOfKey("c") >= 0);
        Assert.assertTrue(result2.indexOfKey("e") >= 0);
        Assert.assertTrue(result2.indexOfKey("f") >= 0);
        Assert.assertTrue(result2.indexOfKey("g") >= 0);
        Assert.assertTrue(result2.indexOfKey("m") >= 0);
        Assert.assertTrue(result2.indexOfKey("n") >= 0);
        Assert.assertTrue(result2.indexOfKey("o") >= 0);
        Assert.assertTrue(result2.indexOfKey("p") >= 0);
        Assert.assertFalse(result2.indexOfKey("0") >= 0);
        Assert.assertFalse(result2.indexOfKey(null) >= 0);

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
        Assert.assertFalse(result2.containsKey(null));

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
        Assert.assertFalse(result2.containsValue(null));

        Assert.assertTrue(result2.indexOfValue("ac") >= 0);
        Assert.assertTrue(result2.indexOfValue("bc") >= 0);
        Assert.assertTrue(result2.indexOfValue("cc") >= 0);
        Assert.assertTrue(result2.indexOfValue("ec") >= 0);
        Assert.assertTrue(result2.indexOfValue("fc") >= 0);
        Assert.assertTrue(result2.indexOfValue("gc") >= 0);
        Assert.assertTrue(result2.indexOfValue("0ma") >= 0);
        Assert.assertTrue(result2.indexOfValue("0na") >= 0);
        Assert.assertTrue(result2.indexOfValue("Zoa") >= 0);
        Assert.assertTrue(result2.indexOfValue("Zpa") >= 0);
        Assert.assertFalse(result2.indexOfValue("0") >= 0);
        Assert.assertFalse(result2.indexOfValue(null) >= 0);

        Assert.assertTrue(result2.lastIndexOfValue("ac") >= 0);
        Assert.assertTrue(result2.lastIndexOfValue("bc") >= 0);
        Assert.assertTrue(result2.lastIndexOfValue("cc") >= 0);
        Assert.assertTrue(result2.lastIndexOfValue("ec") >= 0);
        Assert.assertTrue(result2.lastIndexOfValue("fc") >= 0);
        Assert.assertTrue(result2.lastIndexOfValue("gc") >= 0);
        Assert.assertTrue(result2.lastIndexOfValue("0ma") >= 0);
        Assert.assertTrue(result2.lastIndexOfValue("0na") >= 0);
        Assert.assertTrue(result2.lastIndexOfValue("Zoa") >= 0);
        Assert.assertTrue(result2.lastIndexOfValue("Zpa") >= 0);
        Assert.assertFalse(result2.lastIndexOfValue("0") >= 0);
        Assert.assertFalse(result2.lastIndexOfValue(null) >= 0);

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
        Assert.assertSame(null, result2.get(null));

        ArrayBackedSet<Map.Entry<String, String>> entrySet = result2.entrySet();
        Iterator<Map.Entry<String, String>> entryIt = entrySet.iterator();

        Assert.assertTrue(entryIt.hasNext());
        Assert.assertEquals(10, result2.size());
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

        ImmutableArrayMapBuilder<String, String> builder1 =
                new ImmutableArrayMapBuilder<>();
        ImmutableArrayMapBuilder<String, String> builder2 =
                new ImmutableArrayMapBuilder<>();

        builder1.with(map1.entrySet());

        map2.entrySet().forEach(builder2::with);

        builder1.merge(builder2);

        ArrayBackedMap<String, String> result = builder1.build();
        Assert.assertEquals(11, result.size());
    }

    @Test
    public void testBuilder() throws Exception {
      ImmutableArrayMap<String, String> map =
              ImmutableArrayMap.<String, String>builder()
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

        ArrayBackedSet<String> keySet = map.keySet();
        Assert.assertFalse(keySet.isEmpty());
        Assert.assertEquals(7, keySet.size());
        Assert.assertEquals("[c, d, e, a, b, f, g]", keySet.toString());
        List<String> keyList = keySet.asList();
        Assert.assertFalse(keyList.isEmpty());
        Assert.assertEquals(7, keyList.size());
        Assert.assertEquals("[c, d, e, a, b, f, g]", keyList.toString());

        ArrayBackedSet<Map.Entry<String, String>> entrySet = map.entrySet();
        Assert.assertFalse(entrySet.isEmpty());
        Assert.assertEquals(7, entrySet.size());
        Assert.assertEquals("[c=5, d=4, e=3, a=7, b=96, f=2, g=1]", entrySet.toString());
        List<Map.Entry<String, String>> entryList = entrySet.asList();
        Assert.assertFalse(entryList.isEmpty());
        Assert.assertEquals(7, entryList.size());
        Assert.assertEquals("[c=5, d=4, e=3, a=7, b=96, f=2, g=1]", entryList.toString());

        ArrayBackedCollection<String> values = map.values();
        Assert.assertFalse(values.isEmpty());
        Assert.assertEquals(7, values.size());
        Assert.assertEquals("[5, 4, 3, 7, 96, 2, 1]", values.toString());
        List<String> valueList = values.asList();
        Assert.assertFalse(valueList.isEmpty());
        Assert.assertEquals(7, valueList.size());
        Assert.assertEquals("[5, 4, 3, 7, 96, 2, 1]", valueList.toString());

        Assert.assertEquals(1, map.indexOfValue("4"));
    }

    @Test
    public void testSubMap() throws Exception {
        ImmutableArrayMapBuilder<String, String> builder =
                new ImmutableArrayMapBuilder<>();
        builder.with("a", "ac");
        builder.with("b", "bc");
        builder.with("c", "cc");
        builder.with("d", "dx");
        builder.with("e", "ec");
        builder.with("f", "fc");
        builder.with("g", "gc");

        ImmutableArrayMap<String, String> map = builder.build();

        List<Map.Entry<String,String>> list = map.entrySet().asList();
        List<Map.Entry<String, String>> subList = list.subList(2, 3);
        List<Map.Entry<String, String>> emptySubMap = list.subList(1, 1);
        List<Map.Entry<String, String>> fullSubMap = list.subList(0, 7);

        Assert.assertEquals(1, subList.size());
        Assert.assertTrue(emptySubMap.isEmpty());
        Assert.assertEquals(7, fullSubMap.size());

    }
}
