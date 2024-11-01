/**
 * SAP Inc.
 * Copyright (c) 1972-2018 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

/**
 * 
 * @author richard.xu03@sap.com
 * @version $Id: TestDemo.java, v 0.1 May 28, 2018 11:00:32 AM richard.xu Exp $
 */
public class TestDemo {

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
