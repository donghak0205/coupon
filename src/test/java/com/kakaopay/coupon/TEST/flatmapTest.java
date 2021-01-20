package com.kakaopay.coupon.TEST;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

public class flatmapTest {


    @Test
    public void Test_A() {
        String[][] sample = new String[][]{
                {"a", "b"}, {"c", "d"}, {"e", "a"}, {"a", "h"}, {"i", "j"}
        };

        //without .flatMap()
        Stream<String> stream = Arrays.stream(sample)
                .flatMap(array -> Arrays.stream(array));


                //.filter(x -> "a".equals(x));

        stream.forEach(System.out::println);
    }
}
