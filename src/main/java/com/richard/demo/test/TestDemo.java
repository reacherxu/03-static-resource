/**
 * SAP Inc.
 * Copyright (c) 1972-2018 All Rights Reserved.
 */
package com.richard.demo.test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.richard.demo.Person;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @author richard.xu03@sap.com
 * @version $Id: TestDemo.java, v 0.1 May 28, 2018 11:00:32 AM richard.xu Exp $
 */
@Slf4j
public class TestDemo {

    public static void fizzBuzz(int n) {
        // Write your code here
        for (int i = 1; i <= n; i++) {
            if (isMuti3(i) == true && isMuti5(i) == true) {
                System.out.println("FizzBuzz");
            } else if (isMuti3(i) == true && isMuti5(i) == false) {
                System.out.println("Fizz");
            } else if (isMuti3(i) == false && isMuti5(i) == true) {
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }

    }

    public static void main(String[] args) {
        // fizzBuzz(15);
        // System.out.println(isMuti3(3));
        List<Integer> num = Lists.newArrayList(2);
        System.out.println("value is " + minSum(num, 1));;
    }

    // TODO time exceed
    /**
     * @param num: the list of numbers
     * @param k: divided by 2 k times
     * @return: the minimum sum
     */
    public static int minSum(List<Integer> num, int k) {
        // Write your code here
        int sum = 0;
        for (int i = 0; i < k; i++) {
            Collections.sort(num);
            int max = num.get(num.size() - 1);
            int minimizeValue = max % 2 == 0 ? num.set(num.size() - 1, max / 2) : num.set(num.size() - 1, max / 2 + 1);
        }
        for (int i = 0; i < num.size(); i++) {
            sum += num.get(i);
        }
        return sum;
    }

    // passed
    /**
     * @param numbers: a list of integers
     * @return: return the number of non-unique integers
     */
    public static int countDuplicate(List<Integer> numbers) {
        // Write your code here
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < numbers.size(); i++) {
            map.put(numbers.get(i), map.getOrDefault(numbers.get(i), 0) + 1);
        }
        int count = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                count++;
            }
        }
        return count;
    }

    public static boolean isMuti3(int n) {
        return n % 3 == 0;
    }

    public static boolean isMuti5(int n) {
        return n % 5 == 0;
    }

    @Test
    public void test() {
        Integer i1 = 40;
        Integer i2 = new Integer(40);
        log.info("结果是 {}", i1 == i2);
        Integer i3 = 40;
        log.info("利用了包装类型的缓存机制 结果是 {}", i3 == i1);


        String str1 = "hello";
        String str2 = new String("hello");
        String str3 = "hello";
        // 使用 == 比较字符串的引用相等
        System.out.println(str1 == str2);
        System.out.println(str1 == str3);
        // 使用 equals 方法比较字符串的相等
        System.out.println(str1.equals(str2));
        System.out.println(str1.equals(str3));

        Object o = new String();
    }

    /*
     * src:源数组；    srcPos:源数组要复制的起始位置
     * dest:目的数组；   destPos:目的数组放置的起始位置；    length:复制的长度。
     */
    @Test
    public void testSystemArrayCopy() {
        int[] ids = { 1, 2, 3, 4, 5 };
        System.out.println(Arrays.toString(ids)); // [1, 2, 3, 4, 5]  

        // 测试自我复制  
        // 把从索引0开始的2个数字复制到索引为3的位置上  
        System.arraycopy(ids, 0, ids, 3, 2);
        System.out.println(Arrays.toString(ids)); // [1, 2, 3, 1, 2]  

        // 测试复制到别的数组上  
        // 将数据的索引1开始的3个数据复制到目标的索引为0的位置上  
        int[] ids2 = new int[6];
        System.arraycopy(ids, 1, ids2, 0, 3);
        System.out.println(Arrays.toString(ids2)); // [2, 3, 1, 0, 0, 0]  

        // 此功能要求  
        // 源的起始位置+长度不能超过末尾  
        // 目标起始位置+长度不能超过末尾  
        // 且所有的参数不能为负数  
        try {
            System.arraycopy(ids, 0, ids2, 0, ids.length + 1);
        } catch (IndexOutOfBoundsException ex) {
            // 发生越界异常，数据不会改变  
            System.out.println("拷贝发生异常：数据越界。");
        }
        System.out.println(Arrays.toString(ids2)); // [2, 3, 1, 0, 0, 0]  
        // 如果是类型转换问题  
        Object[] o1 = { 1, 2, 3, 4.5, 6.7 };
        Integer[] o2 = new Integer[5];
        System.out.println(Arrays.toString(o2)); // [null, null, null, null, null]  
        try {
            System.arraycopy(o1, 0, o2, 0, o1.length);
        } catch (ArrayStoreException ex) {
            // 发生存储转换，部分成功的数据会被复制过去  
            System.out.println("拷贝发生异常：数据转换错误，无法存储。");
        }
        // 从结果看，前面3个可以复制的数据已经被存储了。剩下的则没有  
        System.out.println(Arrays.toString(o2)); // [1, 2, 3, null, null]  
    }

    @Test
    public void testMapPut() {
        Map<String, String> map = new HashMap<>();
        map.putIfAbsent("key", "oldValue");
        // 如果key存在，则忽略put操作
        map.putIfAbsent("key", "newValue");
        String value = map.get("key");
        System.out.println(value);
    }

    @Test
    public void testMapForeach() {
        Map<String, String> map = new HashMap<>();
        map.putIfAbsent("key1", "value1");
        map.putIfAbsent("key2", "value1");
        map.putIfAbsent("key3", "value1");

        map.forEach((key, value) -> System.out.println(key + ":" + value));
    }

    /**
     * 很多程序员在调试代码时都喜欢 print 一些内容，这样看起来更直观，print 完之后又很容易忘记删除掉这些没用的内容，最终将代码提交到 remote，code review
     * 时又不得不删减这些内容重新提交，不但增加不必要的工作量，还让 log tree 的一些节点没有任何价值
     *
     * IntelliJ IDEA 提供 Evaluate and Log at Breakpoints 功能恰巧可以帮助我们解决这个问题
     */
    @Test
    public void testIDEALog() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (isInterested(random.nextInt(10))) {
                count++;
            }
        }
        System.out.printf("Found %d interested values%n", count);
    }

    @Test
    public void testSplit() {
        int count = 301;
        int split = 100;
        // prepare data
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            persons.add(new Person("name" + i, i, "address" + i));
        }

        // split data
        int groupSize = persons.size() / split + (persons.size() % split == 0 ? 0 : 1);
        System.out.println("group size is " + groupSize);
        for (int i = 0; i < groupSize; i++) {
            List<Person> subList = new ArrayList<>();
            System.out.println("first element is " + persons.get(i * split).toString());
            if (i == groupSize - 1) {
                subList = persons.subList(i * split, persons.size());
            } else {
                subList = persons.subList(i * split, (i + 1) * split);
            }
            System.out.println(subList.toString() + "\n");
        }
        System.out.printf("Found %d interested values%n", count);
    }

    private static boolean isInterested(int i) {
        return i % 2 == 0;
    }
}

