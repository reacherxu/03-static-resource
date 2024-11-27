package com.richard.demo.utils.leetcode;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 贪心算法的本质：是选择每一阶段的局部最优，从而达到全局最优。
 */
@Slf4j
public class SellStock {
    /**
     * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
     *
     * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
     *
     * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
     */
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

    @Test
    public void testMaxprofit() {
        int prices[] = {7, 5, 4, 3, 6, 1};
        log.info("max profit is {}", maxProfit(prices));
    }

    @Test
    public void testMaxStep() {
        int prices[] = {2, 3, 1, 1, 4};
        log.info("result is {}", maxStep(prices));

        int prices1[] = {3, 2, 1, 0, 4};
        log.info("result is {}", maxStep(prices1));
    }

    /**
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


}
