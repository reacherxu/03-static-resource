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
        String test2 = "abccccdd";
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
    public static int longestPalindrome(String s) {
        if (s == null || s.length() < 1) {
            return 0;
        }
        // 记录最大回文子串的长度
        // 一旦 s 非空，必然最大回文子串长度至少为 1
        int max_length = 0;
        String tempResult = null;
        for (int i = 0; i < s.length(); i++) {
            // 考察回文串长度是奇数的情况
            int k1 = expandPalindrome(s, i - 1, i + 1);
            // 考察回文串长度是偶数的情况
            int k2 = expandPalindrome(s, i, i + 1);

            // 计算两个情况下的长度
            int length1 = k1 * 2 + 1;
            int length2 = k2 * 2;

            // 更新最大值
            if (length1 > max_length) {
                max_length = length1;
                // tempResult = s.substring(i - k1, i + k1 + 1);
                // System.out.println("奇数的情况" + s.substring(i, i + 1) + " " + tempResult);
            }
            if (length2 > max_length) {
                max_length = length2;
                // tempResult = s.substring(i - k2 + 1, i + k2 + 1);
                // System.out.println("偶数的情况" + s.substring(i, i + 1) + " " + tempResult);
            }
        }

        return max_length;

    }

    // 辅助函数：从长度为 n 的字符串 s 的给定位置左右扩展寻找回文串。
    // 输入的 left 和 right 是扩展的左右起始位置。
    // 返回扩展经过的字符数 k .
    public static int expandPalindrome(String str, int left, int right) {
        int k = 0;
        char s[] = str.toCharArray();
        while (left >= 0 && right < str.length()) {
            if (s[left] == s[right]) {
                left--;
                right++;
                k++;
            } else {
                break;
            }
        }
        System.out.println(str.substring(left + 1, right));
        return k;
    }

    public static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            --left;
            ++right;
        }
        return right - left - 1;
    }

}
