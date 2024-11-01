package com.richard.demo.utils.leetcode;

import java.util.HashSet;

public class LongestPalindrome {

    public static void main(String[] args) {
        // 最长回文串的长度
        String s = "abccccdd";
        System.out.println(longestPalindrome(s));// 7
        s = "aaabbbbbccccdd";
        System.out.println(longestPalindrome(s));// 13

        // 判断是否是回文串
        String test1 = "A man, a plan, a canal: Panama";
        System.out.println(isPalindrome(test1));// true
        test1 = "race a car";
        System.out.println(isPalindrome(test1));// false

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
     * 最长回文串的长度
     * 给定字符串，自己构造一个最长的回文串的长度
     */
    private static int longestPalindrome(String s) {
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

}
