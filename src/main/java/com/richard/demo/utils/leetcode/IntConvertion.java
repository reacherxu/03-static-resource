package com.richard.demo.utils.leetcode;

import org.junit.Test;

public class IntConvertion {

    /**
     * 编写一个函数，确定需要改变几个位才能将整数A转成整数B。
     * <p>
     * 输入：A = 29 （或者0b11101）, B = 15（或者0b01111）
     * 输出：2
     */
    @Test
    public void testAToB() {
        int a = 1, b = 2;
        int count = aToB(a, b);
        System.out.println(count);

        a = 29;
        b = 15;
        System.out.println(aToB(a, b));
    }

    /**
     * 本质考异或^ 以及位移操作>>>
     * 异或之后，数1的个数
     * 
     * @param a
     * @param b
     * @return
     */

    private int aToB(int a, int b) {
        int c = a ^ b;
        int count = 0;
        while (c != 0) {
            if ((c & 1) == 1) {
                count++;
            }
            c >>>= 1;
        }
        return count;
    }

    /**
     * 剑指 offer: 将一个字符串转换成一个整数(实现 Integer.valueOf(string)的功能，
     * 但是 string 不符合数字要求时返回 0)，要求不能使用字符串转换整数的库函数。
     * 数值为 0 或者字符串不是一个合法的数值则返回 0
     *
     * 思路 ： 除了符号之外，按位数*10 （'char'-'0' == 真实的位数）
     * -> 考到了ascii 码和数字之间的转换
     */
    @Test
    public void testStrToInt() {
        String num = "-42";
        int value = strToInt1(num);
        System.out.println(value);
    }

    /**
     * 函数 myAtoi(string s) 的算法如下：
     *
     * 空格：读入字符串并丢弃无用的前导空格（" "）
     * 符号：检查下一个字符（假设还未到字符末尾）为 '-' 还是 '+'。如果两者都不存在，则假定结果为正。
     * 转换：通过跳过前置零来读取该整数，直到遇到非数字字符或到达字符串的结尾。如果没有读取数字，则结果为0。
     * 舍入：如果整数数超过 32 位有符号整数范围 [−231, 231 − 1] ，需要截断这个整数，使其保持在这个范围内。具体来说，小于 −231 的整数应该被舍入为 −231 ，大于 231 −
     * 1 的整数应该被舍入为 231 − 1
     */
    @Test
    public void testStrToInt1() {
        String num = "-";
        int value = strToInt1(num);
        System.out.println(value);
        System.out.println(strToInt1("-91283472332"));
        System.out.println(strToInt1("+1"));
    }


    // 关键是 数值char - '0' 就等于真实的位数上的值
    private int strToInt(String num) {
        int result = 0;
        char[] chars = num.toCharArray();
        // 取正负值
        boolean isPositive = true;
        int start = 0;
        if (chars[0] == '-') {
            isPositive = false;
            start = 1;
        } else if (chars[0] == '+') {
            isPositive = true;
            start = 1;
        } else {
            isPositive = true;
            start = 0;
        }

        // 取每个位置上的值
        for (int i = start; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                int value = chars[i] - '0';
                result = result * 10 + value;
            } else {
                return 0;
            }
        }

        return isPositive ? result : -1 * result;
    }

    private int strToInt1(String num) {
        num = num.trim();
        char[] chars = num.toCharArray();
        // corner case1
        if (num.length() == 0) {
            return 0;
        }

        // 取正负值
        boolean isPositive = true;
        int start = 0;
        if (chars[0] == '-') {
            isPositive = false;
            start = 1;
        } else if (chars[0] == '+') {
            isPositive = true;
            start = 1;
        } else {
            isPositive = true;
            start = 0;
        }

        // corner case 2
        // 第一个不是数字则返回0
        // 没有第一个数字
        if (start == chars.length || !Character.isDigit(chars[start])) {
            return 0;
        }

        // 其余均是数字场景
        // 取每个位置上的值
        double result = 0;
        for (int i = start; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                int value = chars[i] - '0';
                result = result * 10 + value;
            } else {
                // 遇到第一个不是数字的停止
                break;
            }
        }

        // corner case 3
        int intResult = 0;
        if (result > Integer.MAX_VALUE && isPositive) {
            intResult = Integer.MAX_VALUE;
        } else if (result > (Integer.MAX_VALUE) && !isPositive) {
            intResult = Integer.MIN_VALUE;
        } else {
            intResult = Integer.valueOf((int) result);
            intResult = (isPositive ? intResult : -1 * intResult);
        }

        return intResult;
    }


    @Test
    public void testWeiyi() {
        System.out.println(1 >>> 1);
        System.out.println(2 >>> 1);
        System.out.println(3 >>> 1);
        System.out.println(4 >>> 1);
        System.out.println(Integer.MIN_VALUE);
        System.out.println(Integer.MAX_VALUE);
    }
}
