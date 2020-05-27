/**
 * SAP Inc.
 * Copyright (c) 1972-2019 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: StreamUtils.java, v 0.1 Aug 11, 2019 6:24:50 PM richard.xu Exp $
 */
public class StreamUtils {

    /**
     * Stream 的基本使用
     */
    @Test
    public void test1() {

        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        System.out.println("numbers : " + numbers);

        List<Integer> copyIntegers = numbers.stream().map(i -> i * i).collect(Collectors.toList());
        System.out.println("copyIntegers : " + copyIntegers);

        List<Integer> filterIntegers = numbers.stream().filter(i -> i >= 5).collect(Collectors.toList());
        System.out.println("filterIntegers : " + filterIntegers);

        int x = 1;
        long y = 3;
        System.out.format("%s %s\n", x, y);

        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        long count = strings.stream().filter(str -> StringUtils.isBlank(str)).count();
        System.out.println("blank string number is :" + count);
        long count1 = strings.stream().filter(str -> str.length() == 3).count();
        System.out.println("length = 3 number is :" + count1);

        List<String> myList = Arrays.asList("a1", "a2", "b1", "c2", "c1");
        myList.stream().filter(s -> s.startsWith("c")).map(String::toUpperCase).forEach(System.out::println);
        myList.stream().filter(s -> s.startsWith("c")).map(String::toUpperCase).forEach(s -> {
            System.out.println("Upper case " + s);
        });
        myList.stream().filter(s -> s.startsWith("c")).map(String::toUpperCase).forEach(s -> System.out.println("Upper case " + s));
    }

    @Test
    public void test3() {
        // 每个中间函数的执行次数都是不一样的
        List<String> myList = Arrays.asList("a1", "a2", "b1", "c2", "c1");
        myList.stream().filter(s -> {
            System.out.println("filter: " + s);
            return s.startsWith("a");
        }).map(s -> {
            System.out.println("map: " + s);
            return s.toUpperCase();
        }).forEach(s -> System.out.println("forEach: " + s));

        // 测试match方法
        boolean result = myList.stream().anyMatch(s -> s.startsWith("d"));
        System.out.println(result);
        result = myList.stream().allMatch(s -> s.matches("[a-z0-9]*"));
        System.out.println(result);
        result = myList.stream().noneMatch(s -> s.matches("[A-Z0-9]*"));
        System.out.println(result);
    }

    @Test
    public void test2() {
        // Stream 的创建
        // 创建方式1
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        strings.stream().filter(str -> StringUtils.isNotBlank(str)).forEach(System.out::println);
        // 创建方式2
        Stream.of("abc", "", "bc", "efg", "abcd", "", "jkl").filter(str -> StringUtils.isNotBlank(str)).forEach(System.out::println);
    }

    @Test
    public void testMap() {
        List<Person> persons =
                Arrays.asList(new Person("Max", 18), new Person("Peter", 23), new Person("Pamela", 23), new Person("David", 12));

        List<String> nameList = persons.stream().map(Person::getName).collect(Collectors.toList());
        System.out.println("names are " + nameList);

        List<Person> filterdList = persons.stream().filter(person -> person.getName().startsWith("P")).collect(Collectors.toList());
        System.out.println(filterdList);


        Map<Integer, List<Person>> personsByAge = persons.stream().collect(Collectors.groupingBy(p -> p.getAge()));
        personsByAge.forEach((age, p) -> System.out.format("age %s: %s\n", age, p));

        // 去重
        List<Integer> list = persons.stream().map(p -> p.getAge()).distinct().collect(Collectors.toList());
        System.out.println(list);

        // 为了将stream元素转换为map，我们必须指定键和值如何映射。
        // 请记住，映射的键必须是惟一的，否则会抛出IllegalStateException。你可以将合并函数作为额外参数传递，以绕过异常:
        Map<Integer, String> map = persons.stream().filter(p -> (p.age > 18))
                .collect(Collectors.toMap(p -> p.getAge(), p -> p.getName(), (name1, name2) -> name1 + ";" + name2));
        System.out.println(map);
    }

    @Test
    public void testFlatMap() {
        List<Foo> foos = new ArrayList<>();

        // create foos
        IntStream.range(1, 4).forEach(i -> foos.add(new Foo("Foo" + i)));

        // create bars
        foos.forEach(f -> IntStream.range(1, 4).forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));

        // FlatMap接受一个函数，该函数必须返回对象stream。
        foos.stream().flatMap(foo -> foo.bars.stream()).forEach(b -> System.out.println(b.name));

    }
}


class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * Getter method for property <tt>name</tt>.
     * 
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>name</tt>.
     * 
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for property <tt>age</tt>.
     * 
     * @return property value of age
     */
    public int getAge() {
        return age;
    }

    /**
     * Setter method for property <tt>age</tt>.
     * 
     * @param age value to be assigned to property age
     */
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return name;
    }
}


class Foo {
    String name;
    List<Bar> bars = new ArrayList<>();

    Foo(String name) {
        this.name = name;
    }
}


class Bar {
    String name;

    Bar(String name) {
        this.name = name;
    }
}
