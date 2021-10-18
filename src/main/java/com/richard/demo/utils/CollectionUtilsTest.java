/**
 * SAP Inc.
 * Copyright (c) 1972-2019 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: CollectionUtilsTest.java, v 0.1 May 29, 2019 4:40:25 PM richard.xu Exp $
 */
public class CollectionUtilsTest {

    /**
     * 两个集合元素是否相同
     */
    @Test
    public void isEqualCollection() {
        String[] arrayA = new String[] {"A", "B", "C"};
        String[] arrayB = new String[] {"A", "B", "C"};
        String[] arrayC = new String[] {"B", "C", "A"};

        // [A, C, E, G, H, K]
        List<String> listA = Arrays.asList(arrayA);
        List<String> listB = Arrays.asList(arrayB);
        List<String> listC = Arrays.asList(arrayC);
        System.out.println(CollectionUtils.isEqualCollection(listA, listB));
        System.out.println(CollectionUtils.isEqualCollection(listA, listC));
    }

    @Test
    public void operation() {
        String[] arrayA = new String[] {"A", "B", "C", "D", "E", "F"};
        String[] arrayB = new String[] {"B", "D", "F", "G", "H", "K"};
        // [A, C, E, G, H, K]
        List<String> listA = Arrays.asList(arrayA);
        List<String> listB = Arrays.asList(arrayB);

        // 说通俗点就去掉重复元素剩下的元素
        System.out.println("补集:" + CollectionUtils.disjunction(listA, listB));

        System.out.println("并集:" + CollectionUtils.union(listA, listB));
        System.out.println("交集:" + CollectionUtils.intersection(listA, listB));

        // listA扣除listB,剩下的元素
        // 差集:返回只存在于listA左集合独有的数据, 结果【C】
        System.out.println("差集:" + CollectionUtils.subtract(listA, listB));

        Set<String> setA = new HashSet<>();
        setA.add("5807de8d-aacf-48c3-b808-65f68bd7f75b");
        setA.add("b9a4bc54-a603-4526-be4c-f87c2694e613");// same id
        setA.add("6a2116e1-d14d-47aa-b7c0-5206953a36dd");
        Set<String> setB = new HashSet<>();
        setB.add("b9a4bc54-a603-4526-be4c-f87c2694e613");
        setB.add("c48bf9a4-816a-4d7e-a4e5-4f4aebc07b2b");
        setB.add("dffcdfe6-cc58-4b4e-af2c-c050a48299ec");
        System.out.println("setA - setB差集:" + CollectionUtils.subtract(setA, setB));

    }

    @Test
    public void testSet() {
        List<String> listA = Arrays.asList("1", "1", "2");
        List<String> listB = Arrays.asList("1", "3", "2");
        Set<String> set = new HashSet<String>();
        set.addAll(listA);
        set.addAll(listB);
        System.out.println("result set is :" + set);

        List<String> resultList = new ArrayList<String>(set);
        System.out.println("result list is :" + resultList);
    }
}
