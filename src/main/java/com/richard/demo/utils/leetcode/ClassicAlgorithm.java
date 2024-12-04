package com.richard.demo.utils.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

/**
 * 递归算法
 */
@Slf4j
public class ClassicAlgorithm {

    // Q1 : Fibonacci ----------------------------------------------------------
    /**
     * 请你输出斐波那契数列的第 n 项。
     * n<=39
     *
     * 变种 "跳台阶问题" ： 一只青蛙一次可以跳上 1 级台阶，也可以跳上 2 级。求该青蛙跳上一个 n 级的台阶总共有多少种跳法。
     */
    @Test
    public void testFibonacci() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("任务1");
        // 递归方法会导致重复计算，效率低
        for (int j = 1; j < 10; j++) {
            System.out.print(fibonacci(j) + " ");
        }
        System.out.println();

        stopWatch.stop();
        stopWatch.start("任务2");
        // 动态规划,空间换时间
        for (int j = 1; j < 10; j++) {
            System.out.print(fibonacci2(j) + " ");
        }
        System.out.println();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        // 变态跳台阶问题:
        // 一只青蛙一次可以跳上 1 级台阶，也可以跳上 2 级……它也可以跳上 n 级。求该青蛙跳上一个 n 级的台阶总共有多少种跳法
        System.out.println(Math.pow(2, 5 - 1));
        // ava 中有三种移位运算符：
        // “<<” : 左移运算符，等同于乘 2 的 n 次方
        // “>>”: 右移运算符，等同于除 2 的 n 次方
        // “>>>” : 无符号右移运算符，不管移动前最高位是 0 还是 1，右移后左侧产生的空位部分都以 0 来填充。与>>类似。
        System.out.println(1 << 4);

    }

    // 递归方法会导致重复计算，效率低
    private int fibonacci(int j) {
        if (j == 0) {
            return 0;
        }
        if (j == 1 || j == 2) {
            return 1;
        }
        return fibonacci(j - 1) + fibonacci(j - 2);

    }

    // 动态规划,空间换时间
    //
    private int fibonacci2(int j) {
        if (j == 0) {
            return 0;
        }
        if (j == 1) {
            return 1;
        }

        // note : 多开一位，考虑起始位置
        int result[] = new int[j + 1];
        result[0] = 0;
        result[1] = 1;
        for (int i = 2; i <= j; i++) {
            result[i] = result[i - 1] + result[i - 2];
        }
        return result[j];
    }

    // Q2 : n个空格填数字，每个空格上的数字是规定的字符 ----------------------------------------------------------
    /**
     * 总结题型 ： 如果需要排列出所有的组合，可以使用递归
     * - 递归参数1是数组，参数2是当前位置，参数3是结果集
     * 一个人有 leftHours 个小时的工作时间，每天工作时间分配为 0-8 小时，求所有可能的工作时间分配
     */
    @Test
    public void testRecursive() {
        // 给定题目要求
        int totalHours = 28; // 表示总时间
        String pattern = "??8?8?";

        // 1. 计算需要填的空格，以及validation
        int n = 4;// 表示有4天需要分配
        int hours[] = new int[n];
        int pos = 0;
        int leftHours = 12; // 表示剩余需要分配的总时间

        // 2.递归生成所有的组合
        List<String> result = new ArrayList<>();
        generateWorkhours(hours, pos, result, leftHours);
        Collections.sort(result);

        // 3.位置替换
        List<String> sortedResult = new ArrayList<>();
        for (int j = 0; j < result.size(); j++) {
            String temp = result.get(j);
            StringBuilder sb = new StringBuilder();
            int index = 0;
            // 替换pattern
            for (int i = 0; i < pattern.length(); i++) {
                if (pattern.charAt(i) == '?') {
                    sb.append(temp.charAt(index));
                    index++;
                } else {
                    sb.append(pattern.charAt(i));
                }
            }
            sortedResult.add(sb.toString());
        }
        System.out.println(sortedResult);
    }


    private void generateWorkhours(int[] hours, int pos, List<String> result, int leftHours) {
        if (pos == hours.length) {
            int sum = 0;
            String str = "";
            for (int i = 0; i < hours.length; i++) {
                sum += hours[i];
                str += hours[i] + "";
            }
            if (sum == leftHours) {
                result.add(str);
            }
        } else {
            for (int i = 0; i <= 8; i++) {
                hours[pos] = i;
                generateWorkhours(hours, pos + 1, result, leftHours);
            }
        }
    }

    // Q3 : 二分查找 ----------------------------------------------------------

    @Test
    public void testBinarySearch() {
        int nums[] = {-1, 0, 3, 5, 9, 12};
        log.info("result is {}", search(nums, 0));
        log.info("result is {}", search(nums, 9));


        // 查找负数和正数中个数最多的
        int nums1[] = {0, 0, 0, 0};
        // 负整数就是找第一个大于0的数
        int negtivePos = searchBigger(nums1, 0);
        // 正整数就是找第一个大于1的数
        // TODO 这里有点问题
        int positivePos = searchBigger(nums1, 1);
        if (negtivePos == 0 && positivePos < nums1.length) {
            log.info("all positive {}", nums1.length);
        } else if (negtivePos == nums1.length - 1) {
            log.info("all negative  {}", nums1.length);
        } else if (negtivePos == 0 && positivePos == nums1.length) {
            log.info("all 0 {}", 0);
        } else {
            log.info("result is {}", Math.max(negtivePos, nums1.length - positivePos));
        }
        log.info("negative num {}, postive num {}", negtivePos, positivePos);
    }

    public int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            // note ： 这里是重点
            int mid = (right - left) / 2 + left;

            int midValue = nums[mid];
            if (midValue == target) {
                return mid;
            } else if (midValue < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    // 查找第一个大于等于给定值的元素
    public int searchBigger(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = (right - left) / 2 + left;
            int midValue = nums[mid];
            if (midValue >= target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

}
