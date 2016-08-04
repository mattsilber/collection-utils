package com.guardanis.collections.tools;

import android.content.Context;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ListUtilsTests {

    @Mock
    Context mockedContext;

    @Test
    public void testJoin() throws Exception {
        String original = "a,b,c,d";
        String target = "a, b, c, d";

        String actual = ListUtils.from(original.split(","))
                .join(", ");

        assert(actual.equals(target));
    }

    @Test
    public void testMultiple() throws Exception {
        String original = "1,2,3,4,5,6,7";

        int reduced = ListUtils.from(original.split(","))
                .map(new ListUtils.Converter<String, Integer>() {
                    public Integer convert(String from) {
                        return Integer.parseInt(from);
                    }
                }) // "1,2,3,4,5,6,7"
                .filter(new ListUtils.Filter<Integer>() {
                    public boolean isFilterMatched(Integer obj) {
                        return obj > 1 && obj < 7;
                    }
                }) // "2,3,4,5,6"
                .take(4)  // "2,3,4,5"
                .reduce(0, new ListUtils.Reducer<Integer, Integer>() {
                    public Integer reduce(Integer last, Integer value) {
                        return last + value;
                    }
                });

        assert(reduced == 14);
    }

}