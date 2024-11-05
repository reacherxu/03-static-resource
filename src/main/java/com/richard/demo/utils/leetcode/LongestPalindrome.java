package com.richard.demo.utils.leetcode;

import java.util.HashSet;

import org.junit.Test;

public class LongestPalindrome {

    // 给定字符序列，构造最长回文串的长度
    @Test
    public void testSelfBuiltLongestPalindrome() {
        String s = "abccccdd";
        System.out.println(selfBuiltLongestPalindrome(s));// 7
        s = "aaabbbbbccccdd";
        System.out.println(selfBuiltLongestPalindrome(s));// 13
    }

    // 判断是否是回文串
    @Test
    public void testIsPalindrome() {
        String test1 = "A man, a plan, a canal: Panama";
        System.out.println(isPalindrome(test1));// true
        test1 = "race a car";
        System.out.println(isPalindrome(test1));// false
    }

    // 给定字符串，寻找其中最长回文串
    @Test
    public void testLongestPalindrome() {
        String test2 = "abbc";
        System.out.println(longestPalindrome(test2));
    }

    public static void main(String[] args) {

    }

    /**
     * 判断是否是回文串， 忽略大小写，只考虑字母和数字字符
     * 将空字符串定义为有效的回文串
     * 
     * @param str
     * @return
     */
    private static boolean isPalindrome(String str) {
        if (str.length() == 0) {
            return true;
        }

        char[] charArray = str.toCharArray();
        int left = 0, right = charArray.length - 1;
        while (left < right) {
            if (Character.isLetterOrDigit(charArray[left]) && Character.isLetterOrDigit(charArray[right])) {
                if (Character.toLowerCase(charArray[left]) != Character.toLowerCase(charArray[right])) {
                    return false;
                }
                left++;
                right--;
            } else if (!Character.isLetterOrDigit(charArray[left])) {
                left++;
            } else if (!Character.isLetterOrDigit(charArray[right])) {
                right--;
            }
        }
        return true;
    }

    /**
     * 最长回文串的长度 - 强调自己构造
     * 给定字符串，自己构造一个最长的回文串的长度
     */
    private static int selfBuiltLongestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        char[] charArray = s.toCharArray();
        HashSet<Character> set = new HashSet<>();
        int count = 0;
        for (int i = 0; i < charArray.length; i++) {
            if (set.contains(charArray[i])) {
                set.remove(charArray[i]);
                count++;
            } else {
                set.add(charArray[i]);
            }
        }
        return set.isEmpty() ? count * 2 : count * 2 + 1;
    }

    // 寻找最长回文串
    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 1) {
            return "";
        }
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    public static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            --left;
            ++right;
        }
        return right - left - 1;
    }

}
