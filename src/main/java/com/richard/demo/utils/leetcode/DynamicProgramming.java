package com.richard.demo.utils.leetcode;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicProgramming {


    /**
     * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
     * 输入: [-2,1,-3,4,-1,2,1,-5,4],
     * 输出: 6
     * 解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
     */
    @Test
    public void q1() {
        int nums[] = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println(maxSubArray(nums));
    }


    /**
     * 最大连续子序和
     * 1. 问题拆解 ：第i个元素和第i-1个元素的关系
     * 主要是看f(i-1)是否大于0
     * 2. 定义状态 ：f(i)表示以第i个元素结尾的最大连续子序和
     * 3. 状态转移方程 ：f(i)=max{f(i-1)+nums[i],nums[i]}
     * 
     * @param nums
     * @return
     */
    private int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int len = nums.length;
        int[] dp = new int[len];

        // 初始化
        dp[0] = nums[0];
        int max = dp[0];
        int maxIndex = 0;
        for (int i = 1; i < len; i++) {
            dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
            if (dp[i] > max) {
                max = dp[i];
                maxIndex = i;
            }
        }
        log.info("maxIndex is {}", maxIndex);
        return max;
    }
}
