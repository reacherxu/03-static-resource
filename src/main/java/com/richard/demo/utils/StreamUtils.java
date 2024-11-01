/**
 * SAP Inc.
 * Copyright (c) 1972-2019 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: StreamUtils.java, v 0.1 Aug 11, 2019 6:24:50 PM richard.xu Exp $
 */
@Slf4j
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

    @Test
    public void testObservable() {
        List<Person> persons = Arrays.asList(new Person("Max", 18), new Person("Maximam", 180), new Person("Peter", 23),
                new Person("Pamela", 23), new Person("David", 12));
        Observable.from(persons).flatMap(person -> Observable.fromCallable(() -> {
            if (person.getAge() > 100) {
                throw new RuntimeException("age is too big");
            }
            if (person.getAge() < 18) {
                throw new RuntimeException("age is too small");
            }
            return person;
        })).onErrorResumeNext(response -> Observable.<Person>empty()).forEach(person -> {
            System.out.println(person);
        });
    }


    /**
     * 获取以某属性为key，其他属性或者对应对象为value的Map集合
     * 
     * Function.identity()返回一个输出跟输入一样的Lambda表达式对象，等价于形如t -> t形式的Lambda表达式。
     * (m1,m2)-> m1此处的意思是当转换map过程中如果list中有两个相同id的对象，则map中存放的是第一个对象，此处可以根据项目需要自己写。
     */
    @Test
    public void testMap1() {
        List<Person> persons =
                Arrays.asList(new Person("Max", 18), new Person("Peter", 23), new Person("Pamela", 23), new Person("David", 12));
        Map<String, Person> map = persons.stream().collect(Collectors.toMap(Person::getName, Function.identity(), (m1, m2) -> m1));
        log.info(map.toString());
        Map<String, Integer> map2 = persons.stream().collect(Collectors.toMap(Person::getName, Person::getAge));
        log.info(map2.toString());
    }


    /**
     * 以某个属性进行分组的Map集合
     *
     * 以部门id为例，有时需要根据部门分组，筛选出不同部门下的人员
     */
    @Test
    public void testMap2() {
        List<Person> persons =
                Arrays.asList(new Person("Max", 18, "BJ"), new Person("Peter", 23, "BJ"), new Person("Pamela", 23, "BJ"),
                        new Person("David", 12, "SH"));

        // 没有工具的写法
        Map<Integer, List<Person>> ageMap = new HashMap<>();
        for (Person u : persons) {
            if (ageMap.containsKey(u.getAge())) {
                ageMap.get(u.getAge()).add(u);
            } else {
                List<Person> users1 = new ArrayList<>();
                users1.add(u);
                ageMap.put(u.getAge(), users1);
            }
        }
        log.info("没有工具的写法 {} ", ageMap.toString());

        // 简约的写法
        Map<Integer, List<Person>> ageMap2 = persons.stream().collect(Collectors.groupingBy(Person::getAge));
        log.info(" 简约的写法 {} ", ageMap2.toString());
        // 也可以组合形成key
        Map<String, List<Person>> ageMap2_1 = persons.stream().collect(Collectors.groupingBy(person -> {
            return person.getAddress() + person.getAge();
        }));
        log.info(" 简约的写法 {} ", ageMap2_1.toString());

        // 先筛选，再转化成map
        Map<Integer, List<Person>> ageMap3 = persons.stream().filter(p -> p.getAge() >= 18).collect(Collectors.groupingBy(Person::getAge));
        log.info(" filer之后的简约写法 {} ", ageMap3.toString());
    }

    @Test
    public void testMap3() {
        List<String> list = Arrays.asList("1", "2", "3", "1", "1");
        long count = list.stream().filter(p -> Integer.valueOf(p) < 3).count();
        log.info(" count的个数是 {} ", count);

        // 先筛选，再转化成map
        List<String> list1 = list.stream().filter(p -> Integer.valueOf(p) < 3).collect(Collectors.toList());
        log.info(" filer之后的简约写法 {} ", list1);
        List<String> list2 = list.stream().distinct().filter(p -> Integer.valueOf(p) < 3).collect(Collectors.toList());
        log.info(" filer之后的简约写法 {} ", list2);

    }

    /**
     * map为一对一变换。
     * 一个对象 -> 另一个对象 or 一个数组 -> 另一个数组。
     */
    @Test
    public void testObeservableMap() {
        List<Person> persons =
                Arrays.asList(new Person("Max", 18), new Person("Peter", 23), new Person("Pamela", 23), new Person("David", 12));
        Map<String, Person> personMap = new HashMap<>();
        // test reduce
        Observable.from(persons).flatMap(person -> Observable.fromCallable(() -> {
            return ImmutablePair.of(person.getName(), person);
        }).subscribeOn(Schedulers.io()).onErrorResumeNext(response -> Observable.<ImmutablePair<String, Person>>empty()))
                .reduce(personMap, (map, pair) -> {
                    map.put(pair.getLeft(), pair.getRight());
                    return map;
                }).toList().toBlocking().single();
        System.out.println("person map  is " + personMap.toString());

        // test map, 注意 key 不要重复，否则会覆盖
        // 收集原始Observable发射的所有数据项到一个Map（默认是HashMap）然后发射这个Map。
        // 你可以提供一个用于生成Map的Key的函数，还可以提供一个函数转换数据项到Map存储的值（默认数据项本身就是值）
        Map<Integer, Person> ageMap = Observable.from(persons).filter(person -> Objects.nonNull(person)).toMap(p -> {
            return p.getAge();
        }).toBlocking().single();
        System.out.println("person age map is " + ageMap.toString());

        // test toMultimap, value 是 Collection
        List<Person> persons2 = Arrays.asList(new Person("Max", 18), new Person("Max", 28), new Person("Peter", 23),
                new Person("Pamela", 23), new Person("David", 12));
        Map<Integer, Collection<Person>> ageMultiMap =
                Observable.from(persons2).filter(person -> Objects.nonNull(person)).toMultimap(Person::getAge).toBlocking().single();
        System.out.println("person age multimap is " + ageMultiMap.toString());


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
        // Customer sheridan = new Customer("Sheridan");
        // sheridan.setProperty1("pp1");
        // sheridan.addOrder(new Order(1)).addOrder(new Order(2)).addOrder(new Order(3));
        //
        // Customer customer2 = new Customer("aa");
        // BeanUtils.copyProperties(sheridan, customer2);
        // System.out.println(customer2);
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


@Getter
@Setter
class Person {
    String name;
    int age;
    String address;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }


    @Override
    public String toString() {
        return name + ":" + age + ":" + address;
    }
}
