package net.njcull.collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author NCULL
 * @version 1/09/2017 12:23 PM.
 */
public class TestImmutableSortedArrayPropertyMap {

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
        System.out.println(map.toString());
    }

    private static String extractKey(TestClassWithProperty<String> tc) {
        if(tc == null) {
            return null;
        }
        String value = tc.getValue();
        return (value == null) ? null : value.substring(value.length() - 2);
    }

    private static final class TestClassWithProperty<T> {
        private String name;
        private T value;

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
            final StringBuilder sb = new StringBuilder("TestClassWithProperty { ");
            sb.append("name = '").append(name);
            sb.append("', value = ").append(value);
            sb.append(" }");
            return sb.toString();
        }
    }
}
