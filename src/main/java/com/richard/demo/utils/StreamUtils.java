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
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import rx.Observable;
import rx.functions.Func1;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: StreamUtils.java, v 0.1 Aug 11, 2019 6:24:50 PM richard.xu Exp $
 */
public class StreamUtils {
    @Test
    public void testIsSameList() {
        List<String> list1 = Arrays.asList("a", "b", "1");
        List<String> list2 = Arrays.asList("a", "1", "b");
        System.out.println(
                list1.stream().sorted().collect(Collectors.joining()).equals(list2.stream().sorted().collect(Collectors.joining())));

    }

    /**
     * Stream 的基本使用
     */
    @Test
    public void test1() {

        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        System.out.println("numbers : " + numbers);

        // stream 自定义函数的练习
        // 自定义函数
        List<Double> list2 = Observable.from(new Integer[] {1, 2, 3, 4, 5, 6}).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer num) {
                return num % 2 == 1;
            }
        }).map(new Func1<Integer, Double>() {
            @Override
            public Double call(Integer num) {
                return Math.sqrt(num);
            }
        }).toList().toBlocking().single();
        System.out.println("list2 是" + list2);

        // lambda 的简易写法
        List<Double> list3 = Observable.from(new Integer[] {1, 2, 3, 4, 5, 6}).filter(num -> {
            return num % 2 == 1;
        }).map(num -> {
            return Math.sqrt(num);
        }).toList().toBlocking().single();
        System.out.println("list3 是" + list3);

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

    /**
     * map为一对一变换。
     * 一个对象 -> 另一个对象 or 一个数组 -> 另一个数组。
     */
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
    public void testBeanUtils() {
        Customer sheridan = new Customer("Sheridan");
        sheridan.setProperty1("pp1");
        sheridan.addOrder(new Order(1)).addOrder(new Order(2)).addOrder(new Order(3));

        Customer customer2 = new Customer("aa");
        BeanUtils.copyProperties(sheridan, customer2);
        System.out.println(customer2);
    }

    @Test
    public void testFlatMap() {
        // create foos
        Customer sheridan = new Customer("Sheridan");
        Customer ivanova = new Customer("Ivanova");
        Customer garibaldi = new Customer("Garibaldi");
        // create bars
        sheridan.addOrder(new Order(1)).addOrder(new Order(2)).addOrder(new Order(3));
        ivanova.addOrder(new Order(4)).addOrder(new Order(5));

        // FlatMap接受一个函数，该函数必须返回对象stream。
        // 方法1 用lambda 方式
        List<Customer> customers = Arrays.asList(sheridan, ivanova, garibaldi);
        customers.stream().flatMap(customer -> customer.getOrders().stream()).forEach(order -> {
            System.out.println(order.getId());
        });
        
        // 方法2 用匿名函数的方式
        // 与 flatMap 方法有关的两个重要概念应予注意：
        // 1.方法参数 Function 产生一个输出值流；
        // 2.生成的元素被“展平”为一个新的流。
        // 这样解决了两层for循环的代码
        Observable.from(customers).flatMap(new Func1<Customer, Observable<Order>>() {
            @Override
            public Observable<Order> call(Customer customer) {
                return Observable.from(customer.getOrders());
            }
        }).forEach(order -> {
            System.out.println(order.getId());
        });
    }
}


@ToString
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
class Customer {
    private String name;
    private String property1;
    private List<Order> orders = new ArrayList<>();

    public Customer(String name) {
        this.name = name;
    }

    public Customer addOrder(Order order) {
        orders.add(order);
        return this;
    }
}


@ToString
class Order {
    private int id;

    public Order(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
