package com.richard.demo.utils.leetcode;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 贪心算法的本质：是选择每一阶段的局部最优，从而达到全局最优。
 */
@Slf4j
public class SellStock {

    public int maxProfit(int[] prices) {
        // 贪心
        if (prices == null || prices.length < 2) {
            return 0;
        }

        int minValue = Integer.MAX_VALUE; // 维护一个最小值
        int maxProfit = 0; // 维护最大利润
        for (int i = 0; i < prices.length; i++) {
            // 简洁写法 minValue = Math.min(minValue, prices[i]);
            // 走到第i天，回顾之前的天数，记录之前的最小值以及最大利润
            if (prices[i] < minValue) {
                minValue = prices[i];
            }

            // maxProfit = Math.max(maxProfit, prices[i] - minValue);
            if (prices[i] - minValue > maxProfit) {
                maxProfit = prices[i] - minValue;
            }

        }
        return maxProfit;
    }

    /**
     * 121.买卖股票的最佳时机
     * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
     *
     * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
     *
     * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
     *
     * 注意 ： 这里是买卖一次
     */
    @Test
    public void testMaxprofit() {
        int prices[] = {7, 5, 4, 3, 6, 1};
        log.info("max profit is {}", maxProfit(prices));
    }


    public int maxProfitTotal(int[] prices) {
        int sum = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                sum += Math.max(0, prices[i] - prices[i - 1]);
            }
        }
        return sum;
    }

    /**
     * 122.买卖股票的最佳时机 II
     * 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。
     * 在每一天，你可以决定是否购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票。你也可以先购买，然后在 同一天 出售。
     * 返回 你能获得的 最大 利润 。
     *
     * 注意 ： 这里可以有多次买卖
     * 思路 ： 画一个折线图，所有上升的求和就是最大利润
     */
    @Test
    public void testMaxprofitTotal() {
        int prices[] = {7, 1, 5, 3, 6, 4};
        log.info("max profit is {}", maxProfitTotal(prices));

        int prices2[] = {1, 2, 3, 4, 5};
        log.info("max profit is {}", maxProfitTotal(prices2));

        int prices3[] = {7, 6, 4, 3, 1};
        log.info("max profit is {}", maxProfitTotal(prices3));
    }


    @Test
    public void testMaxStep() {
        int prices[] = {2, 3, 1, 1, 4};
        log.info("result is {}", maxStep(prices));

        int prices1[] = {3, 2, 1, 0, 4};
        log.info("result is {}", maxStep(prices1));
    }

    /**
     * 55. 跳跃游戏
     * 给你一个非负整数数组 nums ，你最初位于数组的 第一个下标 。数组中的每个元素代表你在该位置可以跳跃的最大长度。
     * 判断你是否能够到达最后一个下标，如果可以，返回 true ；否则，返回 false 。
     *
     * 注意【0】表示能到达
     * 
     * @param nums
     * @return
     */
    public boolean maxStep(int[] nums) {
        if (nums.length == 0 || nums == null) {
            return false;
        }
        int maxPos = nums[0];
        for (int i = 0; i < nums.length; i++) {
            // note 要停止的地方
            if (i <= maxPos) {
                maxPos = Math.max(maxPos, i + nums[i]);
                if (maxPos >= nums.length - 1) {
                    return true;
                }
            }
        }
        return false;
    }


    public int jump(int[] nums) {
        if (nums.length == 0 || nums == null) {
            return 0;
        }

        // 记录每次到达的最大位置
        int maxPos = nums[0];
        int step = 1;
        for (int i = 0; i < nums.length; i++) {
            // note 要停止的地方
            if (i <= maxPos) {
                if (i + nums[i] > maxPos) {
                    maxPos = i + nums[i];
                    step++;
                }
                if (maxPos >= nums.length - 1) {
                    return step;
                }
            }
        }
        return 0;
    }

    @Test
    public void testJump() {
        int nums[] = {2, 3, 1, 1, 4};
        log.info("result is {}", jump(nums));

        int nums1[] = {2, 3, 1, 2, 4, 2, 3};
        log.info("result is {}", jump(nums1));
    }

    public int jump1(int[] nums) {
        int length = nums.length;
        int end = 0;
        int maxPosition = 0;
        int steps = 0;
        for (int i = 0; i < length - 1; i++) {
            maxPosition = Math.max(maxPosition, i + nums[i]);
            if (i == end) {
                end = maxPosition;
                steps++;
            }
        }
        return steps;
    }


}
