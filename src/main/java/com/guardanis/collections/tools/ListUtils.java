package com.guardanis.collections.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtils<V> {

    public interface Filter<V> {
        public boolean isFilterMatched(V obj);
    }

    public interface Converter<V, T> {
        public T convert(V from);
    }

    public interface Reducer<T, V>{
        public T reduce(T last, V value);
    }

    public interface FAction<V> {
        public void executeFAction(V value);
    }

    private List<V> values;

    public ListUtils(V[] values){
        this(Arrays.asList(values));
    }

    public ListUtils(List<V> values){
        this.values = values;
    }

    public <T> ListUtils<T> map(Converter<V, T> converter){
        List<T> returnables = new ArrayList<T>();

        for(V value : values)
            returnables.add(converter.convert(value));

        return new ListUtils(returnables);
    }

    public ListUtils<Object> flatMap(){
        List<Object> returnables = new ArrayList<Object>();

        for(Object o : values)
            if(o instanceof List)
                returnables.addAll(new ListUtils((List) o)
                    .flatMap()
                    .values());
            else if(o instanceof Object[])
                returnables.addAll(new ListUtils((Object[]) o)
                        .flatMap()
                        .values());
            else returnables.add(o);

        return new ListUtils(returnables);
    }

    public <C> ListUtils<V> filter(Filter<V> filter){
        List<V> returnables = new ArrayList<V>();

        for(V value : values)
            if(filter.isFilterMatched(value))
                returnables.add(value);

        return new ListUtils(returnables);
    }

    public <T> T reduce(T initial, Reducer<T, V> reducer){
        for(V value : values)
            initial = reducer.reduce(initial, value);

        return initial;
    }

    public ListUtils<V> each(FAction action){
        for(V value : values)
            action.executeFAction(value);

        return this;
    }

    public ListUtils<V> take(int count){
        return take(count, 0);
    }

    public ListUtils<V> take(int count, int offset){
        List<V> returnables = new ArrayList<V>();

        for(int i = offset; i < offset + count && i < values.size(); i++)
            returnables.add(values.get(i));

        return new ListUtils(returnables);
    }

    public List<V> values(){
        return values;
    }

}
