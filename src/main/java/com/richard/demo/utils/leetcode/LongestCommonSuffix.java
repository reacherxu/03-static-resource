package com.richard.demo.utils.leetcode;

import java.util.Arrays;

/**
 * 最长公共前缀
 *
 * 编写一个函数来查找字符串数组中的最长公共前缀。
 *
 * 思路 ：排序后的第一个和最后一个，就是最长的公共前缀
 *
 */
public class LongestCommonSuffix {
    public static String replaceSpace(String[] strs) {

        // 如果检查值不合法及就返回空串
        if (!checkStrs(strs)) {
            return "";
        }

        int len = strs.length;
        StringBuilder res = new StringBuilder();
        // 给字符串数组的元素按照升序排序(包含数字的话，数字会排在前面)
        Arrays.sort(strs);

        int m = strs[0].length();
        int n = strs[len - 1].length();
        int num = Math.min(m, n);
        for (int i = 0; i < num; i++) {
            if (strs[0].charAt(i) == strs[len - 1].charAt(i)) {
                res.append(strs[0].charAt(i));
            } else {
                break;
            }
        }
        return res.toString();

    }

    private static boolean checkStrs(String[] strs) {
        boolean flag = false;
        if (strs != null) {
            // 遍历strs检查元素值
            for (int i = 0; i < strs.length; i++) {
                if (strs[i] != null && strs[i].length() != 0) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    // 测试
    public static void main(String[] args) {
        String[] strs = {"customer", "car", "cat"};
        // String[] strs = { "customer", "car", null };//空串
        // String[] strs = {};//空串
        // String[] strs = null;//空串
        // System.out.println(LongestCommonSuffix.replaceSpace(strs));// c

        String[] strs2 = {"flower", "fl e", "flight", "flfnsdkfhsdhf"};
        System.out.println(LongestCommonSuffix.replaceSpace(strs2));// fl

    }
}

