package net.njcull.collections;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Tests for ImmutableSortedArrayPropertyMap.
 *
 * @author NCULL
 * @version 1/09/2017 12:23 PM.
 */
public class TestImmutableSortedArrayPropertyMap {

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

        builder.with(new TestClassWithProperty<String>("f", "ac"));
        builder.with(new TestClassWithProperty<String>("b", "bc"));
        builder.with(new TestClassWithProperty<String>("c", "cc"));
        builder.with(new TestClassWithProperty<String>("d", "dx"));
        builder.with(new TestClassWithProperty<String>("e", "ec"));
        builder.with(new TestClassWithProperty<String>("a", "fc"));
        builder.with(new TestClassWithProperty<String>("g", "gc"));
        builder.with(new TestClassWithProperty<String>("o", "0ma"));
        builder.with(new TestClassWithProperty<String>("n", "0na"));
        builder.with(new TestClassWithProperty<String>("m", "Zoa"));
        builder.with(new TestClassWithProperty<String>("p", "Zpa"));

        ImmutableSortedArrayPropertyMap<String, TestClassWithProperty<String>> map = builder.build();
        Assert.assertEquals(11, map.size());
        Assert.assertEquals("{a=fc, b=bc, c=cc, d=dx, e=ec, f=ac, g=gc, m=Zoa, n=0na, o=0ma, p=Zpa}", map.toString());
        System.out.println(map.toString());
    }

    @Test
    public void testSimplePropertyMapStatic() throws Exception {
        ImmutableSortedArrayPropertyMapBuilder<String, TestClassWithProperty<String>> builder =
                ImmutableSortedArrayPropertyMapBuilder.newMapWithKeys(TestImmutableSortedArrayPropertyMap::extractKey);

        builder.with(new TestClassWithProperty<String>("f", "ac"));
        builder.with(new TestClassWithProperty<String>("b", "bc"));
        builder.with(new TestClassWithProperty<String>("c", "cc"));
        builder.with(new TestClassWithProperty<String>(null, "dx"));
        builder.with(new TestClassWithProperty<String>("e", "ec"));
        builder.with(new TestClassWithProperty<String>("a", null));
        builder.with(new TestClassWithProperty<String>("g", "gc"));
        builder.with(new TestClassWithProperty<String>("o", "0ma"));
        builder.with(new TestClassWithProperty<String>("n", "0na"));
        builder.with(new TestClassWithProperty<String>("m", "Zoa"));
        builder.with(new TestClassWithProperty<String>("p", "Zpa"));

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

/*
        // Values don't implement equals
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

        // Values don't implement equals
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
*/

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
    }
}
