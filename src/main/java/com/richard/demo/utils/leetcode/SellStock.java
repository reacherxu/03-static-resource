package com.richard.demo.utils.leetcode;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 *
 * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
 *
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
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

    @Test
    public void testMaxprofit() {
        int prices[] = {7, 5, 4, 3, 6, 1};
        log.info("max profit is {}", maxProfit(prices));

    }
}
