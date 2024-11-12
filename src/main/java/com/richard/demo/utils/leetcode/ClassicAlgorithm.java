package com.richard.demo.utils.leetcode;

import org.junit.Test;

public class ClassicAlgorithm {
    /**
     * 请你输出斐波那契数列的第 n 项。
     * n<=39
     *
     * 变种 "跳台阶问题" ： 一只青蛙一次可以跳上 1 级台阶，也可以跳上 2 级。求该青蛙跳上一个 n 级的台阶总共有多少种跳法。
     */
    @Test
    public void testFibonacci() {
        for (int j = 1; j < 10; j++) {
            System.out.print(fibonacci(j) + " ");
        }

        // 变态跳台阶问题:
        // 一只青蛙一次可以跳上 1 级台阶，也可以跳上 2 级……它也可以跳上 n 级。求该青蛙跳上一个 n 级的台阶总共有多少种跳法
        System.out.println(Math.pow(2, 5 - 1));
        // ava 中有三种移位运算符：
        // “<<” : 左移运算符，等同于乘 2 的 n 次方
        // “>>”: 右移运算符，等同于除 2 的 n 次方
        // “>>>” : 无符号右移运算符，不管移动前最高位是 0 还是 1，右移后左侧产生的空位部分都以 0 来填充。与>>类似。
        System.out.println(1 << 4);

    }

    private int fibonacci(int j) {
        if (j == 0) {
            return 0;
        }
        if (j == 1 || j == 2) {
            return 1;
        }
        return fibonacci(j - 1) + fibonacci(j - 2);

    }
}
