package com.guardanis.collections.legacy.tools;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ListUtilsTests {

    @Test
    public void testJoin() throws Exception {
        String original = "a,b,c,d";
        String target = "a, b, c, d";

        String actual = ListUtils.from(original.split(","))
            .join(", ");

        assert (actual.equals(target));
    }

    @Test
    public void testMultiple() throws Exception {
        String original = "1,2,3,4,5,6,7";

        int reduced = ListUtils.from(original.split(","))
            .map(new SimpleNumberConverter()) // 1,2,3,4,5,6,7
            .filter(new ListUtils.Filter<Integer>() {
                public boolean isFilterMatched(Integer obj) {
                    return obj > 1 && obj < 7;
                }
            }) // 2,3,4,5,6
            .take(4)  // 2,3,4,5
            .reduce(0, new ListUtils.Reducer<Integer, Integer>() {
                public Integer reduce(Integer last, Integer value) {
                    return last + value;
                }
            });

        assert (reduced == 14);
    }

    @Test
    public void testUnique() throws Exception {
        String original = "1,1,1,2,2,3,3,3,4,4";

        int reduced = ListUtils.from(original.split(","))
            .unique() // "1,2,3,4"
            .map(new SimpleNumberConverter()) // 1,2,3,4
            .reduce(0, new ListUtils.Reducer<Integer, Integer>() {
                public Integer reduce(Integer last, Integer value) {
                    return last + value;
                }
            }); // 10

        assert (reduced == 10);
    }

    @Test
    public void testSorting() throws Exception {
        String original = "4,3,2,1,7,6,5";

        String joined = ListUtils.from(original.split(","))
            .map(new SimpleNumberConverter()) // 4,3,2,1,7,6,5
            .sort(new Comparator<Integer>() {
                public int compare(Integer first, Integer second) {
                    return first - second;
                }
            }) // 1,2,3,4,5,6,7
            .reverse() // 7,6,5,4,3,2,1
            .join(""); // 7654321

        assert (joined.equals("7654321"));
    }

    @Test
    public void testValuesToArrayReflection() throws Exception {
        Integer[] values = ListUtils.fromMappedCount(
                3,
                new ListUtils.Converter<Integer, Integer>() {
                    public Integer convert(Integer from) {
                        return from;
                    }
                }
            ) // 0,1,2
            .valuesToArray(Integer.class);

        assert (values[0] == 0
            && values[2] == 2);
    }

    @Test
    public void testFlatMap() throws Exception {
        List<Integer> child1 = new ArrayList<Integer>();
        child1.add(10);
        child1.add(11);
        child1.add(12);

        List<Integer> child2 = new ArrayList<Integer>();
        child2.add(20);
        child2.add(21);

        List<Object> parent = new ArrayList<Object>();
        parent.add(0);
        parent.add(child1);
        parent.add(child2);

        String value = ListUtils.from(parent)
            .flatMap(new ListUtils.Converter<Object, Integer>() {
                public Integer convert(Object from) {
                    return (Integer) from;
                }
            })
            .reduce("", new ListUtils.Reducer<String, Integer>() {
                public String reduce(String last, Integer current) {
                    return last + current + " ";
                }
            })
            .trim();

        assert (value.equals("0 10 11 12 20 21"));
    }

    @Test
    public void testFlatMapNestedList() throws Exception {
        List<Integer> child1 = new ArrayList<Integer>();
        child1.add(10);
        child1.add(11);
        child1.add(12);

        List<Integer> child2 = new ArrayList<Integer>();
        child2.add(20);
        child2.add(21);

        List<List<Integer>> parent = new ArrayList<List<Integer>>();
        parent.add(child1);
        parent.add(child2);

        String value = ListUtils.from(parent)
            .flatMap(new ListUtils.Converter<Integer, Integer>() {
                public Integer convert(Integer from) {
                    return from;
                }
            })
            .reduce("", new ListUtils.Reducer<String, Integer>() {
                public String reduce(String last, Integer current) {
                    return last + current + " ";
                }
            })
            .trim();

        assert (value.equals("10 11 12 20 21"));
    }

    @Test
    public void testFlatMapExcludesNull() throws Exception {
        List<Integer> parent = new ArrayList<Integer>();
        parent.add(0);
        parent.add(1);
        parent.add(2);
        parent.add(3);

        String value = ListUtils.from(parent)
            .flatMap(new ListUtils.Converter<Integer, Integer>() {
                public Integer convert(Integer from) {
                    return from < 2 ? null : from;
                }
            })
            .reduce("", new ListUtils.Reducer<String, Integer>() {
                public String reduce(String last, Integer current) {
                    return last + current + " ";
                }
            })
            .trim();

        assert (value.equals("2 3"));
    }

    @Test
    public void testGroupBy() throws Exception {
        String[] data = new String[]{"1", "2", "1"};

        int size = ListUtils.from(data)
            .groupBy(new ListUtils.Converter<String, String>() {
                public String convert(String item) {
                    return item;
                }
            })
            .filter(new ListUtils.Filter<Map.Entry<String, List<String>>>() {
                public boolean isFilterMatched(Map.Entry<String, List<String>> items) {
                    return items.getKey().equals("1");
                }
            })
            .first()
            .getValue()
            .size();

        assert (size == 2);
    }

    private static class SimpleNumberConverter implements ListUtils.Converter<String, Integer> {

        @Override
        public Integer convert(String from) {
            return Integer.parseInt(from);
        }
    }
}