package com.guardanis.collections.legacy.tools;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListUtils<V> {

    public interface Filter<V> {

        public boolean isFilterMatched(V obj);
    }

    public interface Converter<V, T> {

        public T convert(V from);
    }

    public interface Zipper<V, T, U> {

        public U zip(V o1, T o2);
    }

    public interface Reducer<T, V> {

        public T reduce(T last, V value);
    }

    public interface Action<V> {

        public void executeAction(V value);
    }

    private List<V> values;

    public ListUtils(@NonNull V[] values) {
        this(Arrays.asList(values));
    }

    public ListUtils(@NonNull List<V> values) {
        this.values = values;
    }

    public ListUtils(@NonNull Set<V> values) {
        this(values.toArray((V[]) new Object[values.size()]));
    }

    public <T> ListUtils<T> map(Converter<V, T> converter) {
        List<T> returnables = new ArrayList<T>();

        for (V value : values) {
            returnables.add(converter.convert(value));
        }

        return new ListUtils(returnables);
    }

    public ListUtils<Object> flatMap() {
        return flatMap(new Converter<V, Object>() {
            public Object convert(V from) {
                return from;
            }
        });
    }

    public <T, S> ListUtils<T> flatMap(Converter<S, T> converter) {
        List<T> returnables = new ArrayList<T>();

        for (V value : values) {
            List<T> converted = new ArrayList<T>();

            if (value instanceof List) {
                converted.addAll(
                    new ListUtils((List<V>) value)
                        .flatMap(converter)
                        .values()
                );
            }
            else if (value instanceof Object[]) {
                converted.addAll(
                    new ListUtils((V[]) value)
                        .flatMap(converter)
                        .values()
                );
            }
            else if (value instanceof Set) {
                converted.addAll(
                    new ListUtils((Set<V>) value)
                        .flatMap(converter)
                        .values()
                );
            }
            else if (value != null) {
                T item = converter.convert((S) value);

                if (item != null) {
                    converted.add(item);
                }
            }

            if (converted == null || converted.size() < 1) {
                continue;
            }

            returnables.addAll(converted);
        }

        return new ListUtils(returnables);
    }

    public ListUtils<V> filter(Filter<V> filter) {
        List<V> returnables = new ArrayList<V>();

        for (V value : values) {
            if (filter.isFilterMatched(value)) {
                returnables.add(value);
            }
        }

        return new ListUtils(returnables);
    }

    public <T> T reduce(T initial, Reducer<T, V> reducer) {
        for (V value : values) {
            initial = reducer.reduce(initial, value);
        }

        return initial;
    }

    public ListUtils<V> each(Action<V> action) {
        for (V value : values) {
            action.executeAction(value);
        }

        return this;
    }

    public ListUtils<V> eachIndexed(Action<MapEntry<Integer, V>> action) {
        for (int i = 0; i < values.size(); i++) {
            action.executeAction(new MapEntry(i, values.get(i)));
        }

        return this;
    }

    public ListUtils<V> take(int count) {
        return take(count, 0);
    }

    public ListUtils<V> take(int count, int offset) {
        List<V> returnables = new ArrayList<V>();

        for (int i = offset; i < offset + count && i < values.size(); i++) {
            returnables.add(values.get(i));
        }

        return new ListUtils(returnables);
    }

    public <T, U> ListUtils<U> zipWith(List<T> subset, ListUtils.Zipper<V, T, U> zipper) {
        if (values.size() != subset.size()) {
            throw new IllegalArgumentException("Subset size [" + subset.size() + "] does not match values [" + values.size() + "]");
        }

        List<U> returnables = new ArrayList<U>();

        for (int i = 0; i < values.size(); i++) {
            returnables.add(zipper.zip(values.get(i), subset.get(i)));
        }

        return new ListUtils(returnables);
    }

    public ListUtils<V> reverse() {
        List<V> returnables = new ArrayList<V>();

        for (int i = values.size() - 1; 0 <= i; i--) {
            returnables.add(values.get(i));
        }

        return new ListUtils(returnables);
    }

    public ListUtils<V> sort(Comparator<V> comparator) {
        List<V> returnables = new ArrayList<V>();

        for (int i = 0; i < values.size(); i++) {
            returnables.add(values.get(i));
        }

        Collections.sort(
            returnables,
            comparator
        );

        return new ListUtils(returnables);
    }

    public ListUtils<V> unique() {
        List<V> returnables = new ArrayList<V>();

        for (int i = 0; i < values.size(); i++) {
            if (!returnables.contains(values.get(i))) {
                returnables.add(values.get(i));
            }
        }

        return new ListUtils(returnables);
    }

    public ListUtils<Map.Entry<String, List<V>>> groupBy(@NonNull Converter<V, String> groupHasher) {
        Map<String, List<V>> map = new HashMap<String, List<V>>();

        for (V value : values) {
            String hash = groupHasher.convert(value);

            List<V> values = map.get(hash);

            if (values == null) {
                values = new ArrayList<V>();

                map.put(hash, values);
            }

            values.add(value);
        }

        List<Map.Entry<String, List<V>>> returnables = new ArrayList<Map.Entry<String, List<V>>>();

        for (String key : map.keySet()) {
            returnables.add(new MapEntry<String, List<V>>(key, map.get(key)));
        }

        return new ListUtils(returnables);
    }

    public V first() {
        if (values.size() < 1) {
            return null;
        }

        return values.get(0);
    }

    public V last() {
        if (values.size() < 1) {
            return null;
        }

        return values.get(values.size() - 1);
    }

    public String join(String delimiter) {
        return join(delimiter, new Converter<V, String>() {
            public String convert(V from) {
                return from.toString();
            }
        });
    }

    public String join(String delimiter, Converter<V, String> converter) {
        String joined = "";

        if (0 < values.size()) {
            joined += converter.convert(values.get(0));

            for (int i = 1; i < values.size(); i++) {
                joined += delimiter + converter.convert(values.get(i));
            }
        }

        return joined;
    }

    public List<V> values() {
        return values;
    }

    /**
     * Convert and return the resulting List as an array of type V.
     * Class arrayType must be explicitly defined so we're not using
     * items to determine the true array type.
     */
    @SuppressWarnings("unchecked")
    public V[] valuesToArray(Class<V> arrayType) {
        return values.toArray(
            (V[]) Array.newInstance(
                arrayType,
                values.size()
            )
        );
    }

    public static <T> ListUtils<T> fromMappedCount(int count, Converter<Integer, T> converter) {
        List<T> values = new ArrayList<T>();

        for (int i = 0; i < count; i++) {
            values.add(converter.convert(i));
        }

        return new ListUtils<T>(values);
    }

    public static <T> ListUtils<T> from(@NonNull T[] values) {
        return new ListUtils<T>(values);
    }

    public static <T> ListUtils<T> from(@NonNull List<T> values) {
        return new ListUtils<T>(values);
    }

    public static <T> ListUtils<T> from(@NonNull Set<T> values) {
        return new ListUtils<T>(values);
    }

    public static class MapEntry<K, V> implements Map.Entry<K, V> {

        private K key;
        private V value;

        public MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V v) {
            V old = value;
            this.value = v;
            return old;
        }
    }
}
