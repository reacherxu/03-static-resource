package com.richard.demo.utils.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.Test;

import com.richard.demo.utils.JacksonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Closest numbers from a list of unsorted integers
 *
 * Given a list of distinct unsorted integers,
 * find the pair of elements that have the smallest absolute difference between them?
 * If there are multiple pairs, find them all.
 *
 * Examples:
 *
 * Input : arr[] = {10, 50, 12, 100}
 * Output : (10, 12)
 * The closest elements are 10 and 12
 * Input : arr[] = {5, 4, 3, 2}
 * Output : (2, 3), (3, 4), (4, 5)
 *
 * 思路 ：排序过后的列表，相邻节点差值一定最小
 * -> sort-> calculate-> find
 */
@Slf4j
public class ClosestPair {

    @Test
    public void testClosestPairs() {
        int arr[] = {10, 50, 12, 100};
        List<MutablePair<Integer, Integer>> pairs = findClosestPairs(arr);
        log.info(JacksonUtil.writeStr(pairs));

        int arr1[] = {5, 4, 3, 2};
        pairs = findClosestPairs(arr1);
        log.info(JacksonUtil.writeStr(pairs));

    }

    // 思考 ： 排序后相邻差值最小， 而不需要暴力走所有的值
    private List<MutablePair<Integer, Integer>> findClosestPairs(int[] arr) {
        List<MutablePair<Integer, Integer>> pairs = new ArrayList<>();
        if (arr.length < 2) {
            return pairs;
        }

        // 1. sort
        Arrays.sort(arr);
        // 2. get closest value
        int min = Math.abs(arr[1] - arr[0]);
        for (int i = 2; i < arr.length; i++) {
            if (min > Math.abs(arr[i] - arr[i - 1])) {
                min = Math.abs(arr[i] - arr[i - 1]);
            }
        }

        // 3. find pairs
        for (int i = 1; i < arr.length; i++) {
            if (min == Math.abs(arr[i] - arr[i - 1])) {
                pairs.add(new MutablePair<>(arr[i - 1], arr[i]));
            }
        }
        return pairs;
    }
}
